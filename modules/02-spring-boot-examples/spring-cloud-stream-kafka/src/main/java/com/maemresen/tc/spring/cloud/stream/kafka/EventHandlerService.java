package com.maemresen.tc.spring.cloud.stream.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventHandlerService {

    private final MessageRepository messageRepository;

    public void convertToUpperCase(final String message) {
        final String convertedMessage = Optional.ofNullable(message)
                .map(String::toUpperCase)
                .orElse(null);
        messageRepository.save(MessageEntity.builder()
                .id(UUID.randomUUID())
                .original(message)
                .converted(convertedMessage)
                .build());
    }
}
