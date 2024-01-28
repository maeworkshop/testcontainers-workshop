package com.maemresen.tc.spring.cloud.stream.kafka.controller;

import com.maemresen.tc.spring.cloud.stream.kafka.service.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/customer-order")
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    @GetMapping("/new/{orderNo}/{productName}")
    public void create(@PathVariable String orderNo, @PathVariable String productName) {
        customerOrderService.createOrder(orderNo, productName);
    }
}
