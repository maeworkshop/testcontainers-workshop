package com.maemresen.tc.spring.cloud.stream.kafka.service;

import com.maemresen.tc.spring.cloud.stream.kafka.dto.messaging.NewOrderMessageDto;
import com.maemresen.tc.spring.cloud.stream.kafka.entity.CustomerOrder;
import com.maemresen.tc.spring.cloud.stream.kafka.messaging.NewOrderMessageProducer;
import com.maemresen.tc.spring.cloud.stream.kafka.repository.CustomerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepository;
    private final NewOrderMessageProducer newOrderMessageProducer;

    public boolean createOrder(final String orderNo, final String productName) {
        if (existsByOrderNo(orderNo)) {
            throw new IllegalArgumentException("Order No must be unique");
        }

        return newOrderMessageProducer.publish(NewOrderMessageDto.newBuilder()
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

    public boolean existsByOrderNo(final String orderNo) {
        return customerOrderRepository.existsByOrderNo(orderNo);
    }

    public List<CustomerOrder> findAll() {
        return customerOrderRepository.findAll();
    }

}
