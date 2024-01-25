package com.maemresen.tc.spring.cloud.stream.kafka.message.producer;

import com.maemresen.tc.spring.cloud.stream.kafka.message.dto.OrderProcessMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomerOrderProcessMessageProducer {

    private final StreamBridge streamBridge;

    public boolean publish(final OrderProcessMessageDto orderProcessMessageDto) {
        return streamBridge.send("customerOrderProcessMessageProducer-out-0", orderProcessMessageDto);
    }
}
