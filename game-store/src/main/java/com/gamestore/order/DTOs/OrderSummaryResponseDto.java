package com.gamestore.order.DTOs;

import com.gamestore.address.DTOs.AddressResponseDto;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderSummaryResponseDto(
    Long id,
    Long userId,
    String userEmail,
    String userName,
    AddressResponseDto shippingAddress,
    List<OrderItemResponseDto> items,
    BigDecimal subtotal,
    BigDecimal total,
    int itemCount,
    String status,
    PaymentResponseDto payment,
    Instant createdAt
) {}