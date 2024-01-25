package com.maemresen.tc.spring.cloud.stream.kafka.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "external_id", unique = true)
    private Long externalId;

    @Column(nullable = false)
    private Long  stock;

    @Column(name = "stock_updated", nullable = false)
    private LocalDateTime stockUpdated;
}
