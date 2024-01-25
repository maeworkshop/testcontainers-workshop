package com.maemresen.tc.spring.cloud.stream.kafka.repository;

import com.maemresen.tc.spring.cloud.stream.kafka.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByExternalId(final Long inventoryId);
}
