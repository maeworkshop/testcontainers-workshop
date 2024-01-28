package com.maemresen.tc.spring.cloud.stream.kafka;

import com.maemresen.tc.spring.cloud.stream.kafka.service.CustomerOrderService;
import org.awaitility.Awaitility;
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

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
class CustomerOrderServiceIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.1");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.3"));

    public static String PRODUCT_NAME = "PRODUCT_NAME_1";

    public static String ORDER_NO = "ORDER_NO_1";

    @Autowired
    private CustomerOrderService customerOrderService;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.stream.kafka.binder.brokers", kafka::getBootstrapServers);
    }

    @Test
    void shouldSaveOrder() {
        boolean result = customerOrderService.createOrder(ORDER_NO, PRODUCT_NAME);
        Awaitility.await().atMost(10, TimeUnit.SECONDS);

        assertTrue(result, "Expected the message to be published successfully, but it was not.");
        assertTrue(customerOrderService.findByOrderNo(ORDER_NO).isPresent(), "Expected to find an order with Order No: " + ORDER_NO + ", but no such order was saved.");
    }
}
