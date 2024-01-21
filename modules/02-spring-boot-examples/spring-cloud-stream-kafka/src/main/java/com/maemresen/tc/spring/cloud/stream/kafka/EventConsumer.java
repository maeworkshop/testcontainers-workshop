package com.maemresen.tc.spring.cloud.stream.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventConsumer implements Consumer<String> {

    private final EventHandlerService eventHandlerService;

    @Override
    public void accept(String message) {
        eventHandlerService.convertToUpperCase(message);
    }
}
