package com.gamestore.product.DTOs;

import java.util.List;

public record ProductPageResponseDto(
        List<ProductResponseDto> dto,
        int page,
        long size
) {
}
