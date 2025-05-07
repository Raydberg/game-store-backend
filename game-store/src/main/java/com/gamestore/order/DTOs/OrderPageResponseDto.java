package com.gamestore.order.DTOs;

import java.util.List;

public record OrderPageResponseDto(
    List<OrderSummaryResponseDto> orders,
    int currentPage,
    long totalItems,
    int totalPages
) {}