package com.gamestore.cart.DTOs;

import java.math.BigDecimal;

public record CartItemResponseDto(
        Long id,
        Long productId,
        String productName,
        String productImage,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subtotal
) {
}
