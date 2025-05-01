package com.gamestore.order.services;

import com.gamestore.order.DTOs.OrderPageResponseDto;
import com.gamestore.order.DTOs.OrderRequestDto;
import com.gamestore.order.DTOs.OrderSummaryResponseDto;

public interface OrderService {

    OrderSummaryResponseDto createOrderFromCart(Long userId, OrderRequestDto orderRequest);

    OrderSummaryResponseDto getOrderById(Long orderId, Long userId);

    OrderPageResponseDto getUserOrders(Long userId, int page, int size);

    OrderPageResponseDto getAllOrders(int page, int size);

    OrderSummaryResponseDto updateOrderStatus(Long orderId, String status);
}