package com.maemresen.tc.spring.cloud.stream.kafka.repository;

import com.maemresen.tc.spring.cloud.stream.kafka.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    Optional<CustomerOrder> findByOrderNo(final String orderNo);

    boolean existsByOrderNo(final String orderNo);
}
