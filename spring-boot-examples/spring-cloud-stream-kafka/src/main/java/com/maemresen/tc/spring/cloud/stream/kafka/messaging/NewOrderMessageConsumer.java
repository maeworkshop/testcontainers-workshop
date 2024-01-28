package com.maemresen.tc.spring.cloud.stream.kafka.messaging;

import com.maemresen.tc.spring.cloud.stream.kafka.dto.messaging.NewOrderMessageDto;
import com.maemresen.tc.spring.cloud.stream.kafka.service.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewOrderMessageConsumer implements Consumer<NewOrderMessageDto> {

    private final CustomerOrderService customerOrderService;

    @Override
    public void accept(final NewOrderMessageDto newOrderMessageDto) {
        customerOrderService.saveOrder(newOrderMessageDto);
    }
}
