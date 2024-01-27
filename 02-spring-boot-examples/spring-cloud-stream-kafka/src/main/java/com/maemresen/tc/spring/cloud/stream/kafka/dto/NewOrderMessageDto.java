package com.maemresen.tc.spring.cloud.stream.kafka.dto;

import lombok.Builder;

@Builder
public record NewOrderMessageDto(Long customerId, String orderNo, String productName) {
}
