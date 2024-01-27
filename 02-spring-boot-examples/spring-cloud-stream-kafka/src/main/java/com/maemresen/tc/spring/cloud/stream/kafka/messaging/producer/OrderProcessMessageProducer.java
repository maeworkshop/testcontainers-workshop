package com.maemresen.tc.spring.cloud.stream.kafka.messaging.producer;

import com.maemresen.tc.spring.cloud.stream.kafka.dto.OrderProcessMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderProcessMessageProducer {

    private final StreamBridge streamBridge;

    public boolean publish(final OrderProcessMessageDto orderProcessMessageDto) {
        return streamBridge.send("orderProcessMessageProducer-out-0", orderProcessMessageDto);
    }
}
