package com.maemresen.tc.spring.cloud.stream.kafka.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "order_no", unique = true)
    private String orderNo;

    @Column(name = "product_name")
    private String productName;
}
