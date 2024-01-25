package com.maemresen.tc.spring.cloud.stream.kafka.message.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record StockUpdatedMessageDto(Long id, Long newStock, LocalDateTime updated) {
}
