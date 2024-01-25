package com.maemresen.tc.spring.cloud.stream.kafka;

import com.maemresen.tc.spring.cloud.stream.kafka.entity.constants.OrderStatus;
import com.maemresen.tc.spring.cloud.stream.kafka.message.dto.OrderProcessMessageDto;
import com.maemresen.tc.spring.cloud.stream.kafka.message.producer.CustomerOrderProcessMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@SpringBootApplication
public class SpringBootTcKafkaApplication implements CommandLineRunner {

    private final CustomerOrderProcessMessageProducer customerOrderProcessMessageProducer;


    public static void main(String[] args) {
        SpringApplication.run(SpringBootTcKafkaApplication.class, args);
    }

    @Override
    public void run(String... args) {
        customerOrderProcessMessageProducer.publish(OrderProcessMessageDto.builder()
                .id(1L)
                .username("maemresen")
                .status(OrderStatus.PENDING)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
