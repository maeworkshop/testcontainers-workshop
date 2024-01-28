package com.maemresen.tc.spring.cloud.stream.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@SpringBootApplication
@RestController
public class SpringBootTcKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTcKafkaApplication.class, args);
    }

}
