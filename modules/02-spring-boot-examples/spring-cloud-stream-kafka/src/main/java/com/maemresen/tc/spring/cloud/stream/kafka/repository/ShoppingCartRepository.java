package com.maemresen.tc.spring.cloud.stream.kafka.repository;

import com.maemresen.tc.spring.cloud.stream.kafka.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
}