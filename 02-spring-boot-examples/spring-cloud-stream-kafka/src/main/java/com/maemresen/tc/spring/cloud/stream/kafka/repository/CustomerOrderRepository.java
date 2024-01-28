package com.maemresen.tc.spring.cloud.stream.kafka.repository;

import com.maemresen.tc.spring.cloud.stream.kafka.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
}
