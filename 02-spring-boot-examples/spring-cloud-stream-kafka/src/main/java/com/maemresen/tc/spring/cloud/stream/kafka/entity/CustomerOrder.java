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

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "order_id")
    private String orderNo;

    @Column(name = "product_name")
    private String productName;

    private String status;
}
