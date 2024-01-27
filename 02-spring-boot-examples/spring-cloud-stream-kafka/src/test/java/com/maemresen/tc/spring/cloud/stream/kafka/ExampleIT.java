package com.maemresen.tc.spring.cloud.stream.kafka;

import com.maemresen.tc.spring.cloud.stream.kafka.dto.NewOrderMessageDto;
import com.maemresen.tc.spring.cloud.stream.kafka.entity.CustomerOrder;
import com.maemresen.tc.spring.cloud.stream.kafka.repository.CustomerOrderRepository;
import com.maemresen.tc.spring.cloud.stream.kafka.service.OrderManagementService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class ExampleIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>();

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.3.arm64"));

    public static Long CUSTOMER_ID_1 = 1L;
    public static String ORDER_NO_1 = "ORDER_NO_1";
    public static String PRODUCT_NAME_1 = "PRODUCT_NAME_1";

    public static Long CUSTOMER_ID_2 = 2L;
    public static String ORDER_NO_2 = "ORDER_NO_2";
    public static String PRODUCT_NAME_2 = "PRODUCT_NAME_2";

    @Autowired
    private InputDestination input;

    @Autowired
    private OutputDestination output;

    @Autowired
    private OrderManagementService orderManagementService;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.stream.kafka.binder.brokers", kafka::getBootstrapServers);
    }

    @Test
    void shouldSaveOrder() {
        input.send(new GenericMessage<>(NewOrderMessageDto.builder()
                .customerId(CUSTOMER_ID_1)
                .orderNo(ORDER_NO_1)
                .productName(PRODUCT_NAME_1)
                .build()), "new-order-message");

        Awaitility.await().atMost(10, TimeUnit.SECONDS);
        boolean customerExists = customerOrderRepository.findAll().stream()
                .anyMatch(customerOrder -> customerOrder.getCustomerId().equals(CUSTOMER_ID_1) &&
                        customerOrder.getOrderNo().equals(ORDER_NO_1) &&
                        customerOrder.getProductName().equals(PRODUCT_NAME_1) &&
                        customerOrder.getStatus().equals("PENDING"));
        assertTrue(customerExists);
    }

    @Test
    void shouldCreateCreateShoppingCart() {
        final long orderCount = 1L;

        final CustomerOrder customerOrder = orderManagementService.saveOrder
                (NewOrderMessageDto.builder()
                        .customerId(CUSTOMER_ID_2)
                        .orderNo(ORDER_NO_2)
                        .productName(PRODUCT_NAME_2)
                        .build());
        orderManagementService.processOrder(customerOrder.getId());
        Awaitility.await().atMost(5, TimeUnit.SECONDS);
        final Message<byte[]> orderProcessMessageDto = this.output.receive(10 * 1000, "order-process-message");
        assertNotNull(orderProcessMessageDto);
        System.out.println(new String(orderProcessMessageDto.getPayload()));
    }
}
