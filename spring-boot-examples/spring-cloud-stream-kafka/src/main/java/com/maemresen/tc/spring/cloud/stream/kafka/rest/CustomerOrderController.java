package com.maemresen.tc.spring.cloud.stream.kafka.rest;

import com.maemresen.tc.spring.cloud.stream.kafka.entity.CustomerOrder;
import com.maemresen.tc.spring.cloud.stream.kafka.service.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/customer-order")
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    @GetMapping("new/{orderNo}/{productName}")
    public boolean create(@PathVariable String orderNo, @PathVariable String productName) {
        return customerOrderService.createOrder(orderNo, productName);
    }

    @GetMapping
    public List<CustomerOrder> findAll() {
        return customerOrderService.findAll();
    }
}
