package com.maemresen.tc.spring.cloud.stream.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class SpringBootTcKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTcKafkaApplication.class, args);
    }
}
