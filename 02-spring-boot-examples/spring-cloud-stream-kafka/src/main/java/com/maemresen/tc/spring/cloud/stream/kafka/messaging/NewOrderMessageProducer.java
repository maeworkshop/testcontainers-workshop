package com.maemresen.tc.spring.cloud.stream.kafka.messaging;

import com.maemresen.tc.spring.cloud.stream.kafka.dto.messaging.NewOrderMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NewOrderMessageProducer {

    private final StreamBridge streamBridge;

    public boolean publish(final NewOrderMessageDto newOrderMessageDto) {
        return streamBridge.send("newOrderMessageProducer-out-0", newOrderMessageDto);
    }
}
