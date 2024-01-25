package com.maemresen.tc.spring.cloud.stream.kafka.message.consumer;

import com.maemresen.tc.spring.cloud.stream.kafka.message.dto.StockUpdatedMessageDto;
import com.maemresen.tc.spring.cloud.stream.kafka.service.OrderManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class StockUpdateMessageConsumer implements Consumer<StockUpdatedMessageDto> {

    private final OrderManagementService orderManagementService;

    @Override
    public void accept(final StockUpdatedMessageDto stockUpdatedMessageDto) {
        orderManagementService.updateProductStock(stockUpdatedMessageDto);
    }
}
