package com.maemresen.tc.spring.cloud.stream.kafka.service;

import com.maemresen.tc.spring.cloud.stream.kafka.dto.NewOrderMessageDto;
import com.maemresen.tc.spring.cloud.stream.kafka.dto.OrderProcessMessageDto;
import com.maemresen.tc.spring.cloud.stream.kafka.entity.CustomerOrder;
import com.maemresen.tc.spring.cloud.stream.kafka.messaging.producer.OrderProcessMessageProducer;
import com.maemresen.tc.spring.cloud.stream.kafka.repository.CustomerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderManagementService {

    private final CustomerOrderRepository customerOrderRepository;

    private final OrderProcessMessageProducer orderProcessMessageProducer;

    public CustomerOrder saveOrder(final NewOrderMessageDto newOrderMessageDto) {
        return customerOrderRepository.save(CustomerOrder.builder()
                .customerId(newOrderMessageDto.customerId())
                .orderNo(newOrderMessageDto.orderNo())
                .productName(newOrderMessageDto.productName())
                .status("PENDING")
                .build());
    }

    public void processOrder(final Long id) {
        final CustomerOrder customerOrder = customerOrderRepository.findById(id).orElseThrow();
        customerOrder.setStatus("COMPLETED");
        customerOrderRepository.save(customerOrder);
        orderProcessMessageProducer.publish(OrderProcessMessageDto.builder()
                .orderId(id)
                .customerId(id)
                .orderNo(customerOrder.getOrderNo())
                .productName(customerOrder.getProductName())
                .build());
    }
}
