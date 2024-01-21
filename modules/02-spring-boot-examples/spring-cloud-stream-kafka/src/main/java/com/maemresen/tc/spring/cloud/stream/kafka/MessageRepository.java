package com.maemresen.tc.spring.cloud.stream.kafka;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
}
