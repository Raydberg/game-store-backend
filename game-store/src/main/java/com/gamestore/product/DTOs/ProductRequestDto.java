package com.gamestore.product.DTOs;

import java.math.BigDecimal;

public record ProductRequestDto(
        String name,
        String description,
        String imageUrl,
        BigDecimal price,
        Integer stock,
        Long categoryId
) {
}
