package com.maemresen.tc.spring.cloud.stream.kafka;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class MessageEntity {

    @Id
    private UUID id;

    private String original;

    private String converted;
}
