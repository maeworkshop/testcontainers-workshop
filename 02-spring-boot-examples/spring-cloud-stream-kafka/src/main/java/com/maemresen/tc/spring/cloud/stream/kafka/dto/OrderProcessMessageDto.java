package com.maemresen.tc.spring.cloud.stream.kafka.dto;

import lombok.Builder;

@Builder
public record OrderProcessMessageDto(Long orderId, Long customerId, String orderNo, String productName) {
}
