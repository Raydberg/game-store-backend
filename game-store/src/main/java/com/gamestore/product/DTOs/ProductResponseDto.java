package com.gamestore.product.DTOs;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponseDto(
        Integer id,
        String name,
        String description,
        String imageUrl,
        BigDecimal price,
        Integer stock,
        Instant createdAt,
        String category,
        boolean active
) {

}