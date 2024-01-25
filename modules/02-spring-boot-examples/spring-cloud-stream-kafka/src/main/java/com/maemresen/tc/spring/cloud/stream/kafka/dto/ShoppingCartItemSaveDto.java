package com.maemresen.tc.spring.cloud.stream.kafka.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartItemSaveDto {
    private Long id;
    private Long productId;
    private Long productCount;
}
