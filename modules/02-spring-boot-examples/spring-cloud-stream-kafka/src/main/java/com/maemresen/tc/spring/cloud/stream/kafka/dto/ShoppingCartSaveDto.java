package com.maemresen.tc.spring.cloud.stream.kafka.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartSaveDto {
    private Long id;
    private String username;
    private List<ShoppingCartItemSaveDto> shoppingCartItems;
}
