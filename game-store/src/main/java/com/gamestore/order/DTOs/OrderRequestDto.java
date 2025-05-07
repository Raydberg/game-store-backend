package com.gamestore.order.DTOs;

public record OrderRequestDto(
        Long addressId,
        String notes,
        String paymentMethod
) {
}
