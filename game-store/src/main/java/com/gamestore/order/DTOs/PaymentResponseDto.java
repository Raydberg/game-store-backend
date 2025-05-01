package com.gamestore.order.DTOs;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponseDto(
    Long id,
    BigDecimal amount,
    String method,
    Instant paidAt
) {}