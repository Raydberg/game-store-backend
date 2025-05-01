package com.gamestore.order.DTOs;

import java.util.List;

public record OrderPageResponseDto(
        List<OrderResponseDto> dto,
        int page,
        long size
) {
}
