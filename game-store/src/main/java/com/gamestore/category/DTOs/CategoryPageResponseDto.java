package com.gamestore.category.DTOs;

import java.util.List;

public record CategoryPageResponseDto(
        List<CategoryResponseDto> dto,
        int page,
        long size
) {
}
