package com.gamestore.cart.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {
    private Long id;
    private Long userId;
    List<CartItemResponseDto> items;
    BigDecimal total;
    int itemCount;
}
