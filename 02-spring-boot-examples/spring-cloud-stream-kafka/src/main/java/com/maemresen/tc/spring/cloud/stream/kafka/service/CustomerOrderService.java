package com.maemresen.tc.spring.cloud.stream.kafka.service;

import com.maemresen.tc.spring.cloud.stream.kafka.dto.messaging.NewOrderMessageDto;
import com.maemresen.tc.spring.cloud.stream.kafka.entity.CustomerOrder;
import com.maemresen.tc.spring.cloud.stream.kafka.messaging.NewOrderMessageProducer;
import com.maemresen.tc.spring.cloud.stream.kafka.repository.CustomerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepository;
    private final NewOrderMessageProducer newOrderMessageProducer;

    public void createOrder(final String orderNo, final String productName) {
        newOrderMessageProducer.publish(NewOrderMessageDto.newBuilder()
                .setOrderNo(orderNo)
                .setProductName(productName)
                .build());
    }

    public CustomerOrder saveOrder(final NewOrderMessageDto newOrderMessageDto) {
        return customerOrderRepository.save(CustomerOrder.builder()
                .orderNo(newOrderMessageDto.getOrderNo())
                .productName(newOrderMessageDto.getProductName())
                .build());
    }
}
