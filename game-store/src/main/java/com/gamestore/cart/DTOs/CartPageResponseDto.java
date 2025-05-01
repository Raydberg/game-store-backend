package com.gamestore.cart.DTOs;

import java.util.List;

public record CartPageResponseDto(
        List<CartResponseDto> dto,
        int page,
        long size
) {
}
