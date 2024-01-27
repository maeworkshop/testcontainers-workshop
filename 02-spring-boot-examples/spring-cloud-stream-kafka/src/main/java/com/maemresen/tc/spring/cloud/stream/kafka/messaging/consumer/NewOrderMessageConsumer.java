package com.maemresen.tc.spring.cloud.stream.kafka.messaging.consumer;

import com.maemresen.tc.spring.cloud.stream.kafka.dto.NewOrderMessageDto;
import com.maemresen.tc.spring.cloud.stream.kafka.service.OrderManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewOrderMessageConsumer implements Consumer<NewOrderMessageDto> {

    private final OrderManagementService orderManagementService;

    @Override
    public void accept(final NewOrderMessageDto newOrderMessageDto) {
        orderManagementService.saveOrder(newOrderMessageDto);
    }
}
