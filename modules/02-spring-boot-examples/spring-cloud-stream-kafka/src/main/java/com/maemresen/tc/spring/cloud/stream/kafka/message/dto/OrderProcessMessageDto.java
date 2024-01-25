package com.maemresen.tc.spring.cloud.stream.kafka.message.dto;

import com.maemresen.tc.spring.cloud.stream.kafka.entity.constants.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderProcessMessageDto(Long id, OrderStatus status, LocalDateTime timestamp, String username) {
}
