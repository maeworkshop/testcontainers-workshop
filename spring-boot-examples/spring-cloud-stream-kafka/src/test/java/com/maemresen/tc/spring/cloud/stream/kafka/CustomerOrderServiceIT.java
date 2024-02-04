package com.maemresen.tc.spring.cloud.stream.kafka;

import com.maemresen.tc.spring.cloud.stream.kafka.entity.CustomerOrder;
import com.maemresen.tc.spring.cloud.stream.kafka.service.CustomerOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class CustomerOrderServiceIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.1");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.3"));

    public static String PRODUCT_NAME = "PRODUCT_NAME_1";

    public static String ORDER_NO_1 = "ORDER_NO_1";

    @Autowired
    private CustomerOrderService customerOrderService;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.stream.kafka.binder.brokers", kafka::getBootstrapServers);
    }

    @Test
    void shouldSaveOrder() {
        boolean result = customerOrderService.createOrder(ORDER_NO_1, PRODUCT_NAME);
        assertThrows(Exception.class, () -> customerOrderService.createOrder(ORDER_NO_1, PRODUCT_NAME));
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            assertTrue(result, "Expected the message to be published successfully, but it was not.");

            final List<CustomerOrder> createdCustomers = customerOrderService.findAll();
            assertEquals(1, createdCustomers.size(), "Expected only one order has been created.");

            final CustomerOrder customerOrder = createdCustomers.getFirst();
            assertEquals(ORDER_NO_1, customerOrder.getOrderNo());
            assertEquals(PRODUCT_NAME, customerOrder.getProductName());
        });
    }
}
