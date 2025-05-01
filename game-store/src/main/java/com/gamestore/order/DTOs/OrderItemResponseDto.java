package com.gamestore.order.DTOs;

import java.math.BigDecimal;

public record OrderItemResponseDto(
    Long id,
    Long productId,
    String productName,
    String productImage,
    BigDecimal unitPrice,
    Integer quantity,
    BigDecimal subtotal
) {}