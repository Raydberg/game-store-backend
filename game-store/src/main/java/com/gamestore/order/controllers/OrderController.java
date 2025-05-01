package com.gamestore.order.controllers;

import com.gamestore.config.helpers.AuthenticationHelper;
import com.gamestore.order.DTOs.OrderPageResponseDto;
import com.gamestore.order.DTOs.OrderRequestDto;
import com.gamestore.order.DTOs.OrderSummaryResponseDto;
import com.gamestore.order.services.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;
    private final AuthenticationHelper authHelper;

    /**
     * Crea una nueva orden a partir del carrito actual del usuario
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderSummaryResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequest) {
        Long userId = authHelper.getCurrentUserId();
        OrderSummaryResponseDto createdOrder = orderService.createOrderFromCart(userId, orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    /**
     * Obtiene todas las órdenes del usuario autenticado
     */
    @GetMapping("/my-orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderPageResponseDto> getMyOrders(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        Long userId = authHelper.getCurrentUserId();
        OrderPageResponseDto orders = orderService.getUserOrders(userId, page, size);
        return ResponseEntity.ok(orders);
    }

    /**
     * Obtiene el detalle de una orden específica del usuario autenticado
     */
    @GetMapping("/my-orders/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderSummaryResponseDto> getMyOrderById(@PathVariable Long orderId) {
        Long userId = authHelper.getCurrentUserId();
        OrderSummaryResponseDto order = orderService.getOrderById(orderId, userId);
        return ResponseEntity.ok(order);
    }

    /**
     * Obtiene todas las órdenes (solo admin)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderPageResponseDto> getAllOrders(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        OrderPageResponseDto orders = orderService.getAllOrders(page, size);
        return ResponseEntity.ok(orders);
    }

    /**
     * Obtiene el detalle de cualquier orden (solo admin)
     */
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderSummaryResponseDto> getOrderById(@PathVariable Long orderId) {
        OrderSummaryResponseDto order = orderService.getOrderById(orderId, null); // null indica que es admin
        return ResponseEntity.ok(order);
    }

    /**
     * Actualiza el estado de una orden (solo admin)
     */
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderSummaryResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        OrderSummaryResponseDto updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }
}