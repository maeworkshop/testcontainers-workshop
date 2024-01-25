package com.maemresen.tc.spring.cloud.stream.kafka;

import com.maemresen.tc.spring.cloud.stream.kafka.dto.ShoppingCartItemSaveDto;
import com.maemresen.tc.spring.cloud.stream.kafka.dto.ShoppingCartSaveDto;
import com.maemresen.tc.spring.cloud.stream.kafka.entity.Product;
import com.maemresen.tc.spring.cloud.stream.kafka.message.dto.StockUpdatedMessageDto;
import com.maemresen.tc.spring.cloud.stream.kafka.repository.ProductRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class ExampleIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>();

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.3.arm64"));

    private static final long PRODUCT1_EXTERNAL_PRODUCT_ID = 1;
    private static final long PRODUCT1_NEW_STOCK = 10L;
    private static final LocalDateTime PRODUCT1_STOCK_UPDATE_TIMESTAMP = LocalDateTime.now();
    private static final StockUpdatedMessageDto PRODUCT1_STOCK_UPDATED_MESSAGE_DTO = StockUpdatedMessageDto.builder()
            .id(PRODUCT1_EXTERNAL_PRODUCT_ID)
            .newStock(PRODUCT1_NEW_STOCK)
            .updated(PRODUCT1_STOCK_UPDATE_TIMESTAMP)
            .build();

    private static final long PRODUCT2_EXTERNAL_PRODUCT_ID = 1;
    private static final long PRODUCT2_NEW_STOCK = 10L;
    private static final LocalDateTime PRODUCT2_STOCK_UPDATE_TIMESTAMP = LocalDateTime.now();
    private static final StockUpdatedMessageDto PRODUCT2_STOCK_UPDATED_MESSAGE_DTO = StockUpdatedMessageDto.builder()
            .id(PRODUCT2_EXTERNAL_PRODUCT_ID)
            .newStock(PRODUCT2_NEW_STOCK)
            .updated(PRODUCT2_STOCK_UPDATE_TIMESTAMP)
            .build();


    @Autowired
    private InputDestination input;

    @Autowired
    private OutputDestination output;

    @Autowired
    private OrderManagementService orderManagementService;

    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.stream.kafka.binder.brokers", kafka::getBootstrapServers);
//        registry.add("spring.cloud.stream.bindings.customerOrderProcessMessageConsumer-in-0.destination", () -> "customer-order-process-message");
//        registry.add("spring.cloud.stream.bindings.customerOrderProcessMessageConsumer-in-0.group", () -> "1");
//        registry.add("spring.cloud.stream.bindings.customerOrderProcessMessageConsumer-in-0.content-type", () -> "application/json");
    }

    @Test
    void shouldUpdateProductStock() {
        input.send(new GenericMessage<>(PRODUCT1_STOCK_UPDATED_MESSAGE_DTO), "stock-update-message");

        Awaitility.await().atMost(10, TimeUnit.SECONDS);
        Optional<Product> product = productRepository.findByExternalId(PRODUCT1_EXTERNAL_PRODUCT_ID);
        assertTrue(product.isPresent());
        assertEquals(PRODUCT1_NEW_STOCK, product.get().getStock());
    }

    @Test
    void shouldCreateCreateShoppingCart() {
        final long orderCount = 1L;
        orderManagementService.updateProductStock(PRODUCT2_STOCK_UPDATED_MESSAGE_DTO);
        orderManagementService.createShoppingCart(ShoppingCartSaveDto.builder()
                .username("maemresen")
                .shoppingCartItems(List.of(ShoppingCartItemSaveDto.builder()
                        .productId(PRODUCT2_EXTERNAL_PRODUCT_ID)
                        .productCount(orderCount)
                        .build()))
                .build());
        Awaitility.await().atMost(5, TimeUnit.SECONDS);
        final Message<byte[]> orderProcessMessageDto = this.output.receive(10 * 1000, "customer-order-process-message");
        assertNotNull(orderProcessMessageDto);
    }
}
