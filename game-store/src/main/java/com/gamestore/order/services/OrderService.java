package com.gamestore.order.services;

import com.gamestore.order.DTOs.OrderPageResponseDto;
import com.gamestore.order.DTOs.OrderRequestDto;
import com.gamestore.order.DTOs.OrderSummaryResponseDto;

import java.util.List;

public interface OrderService {
    /**
     * Crea una orden a partir del carrito del usuario
     * @param userId ID del usuario
     * @param orderRequest datos de la orden (dirección, método de pago)
     * @return Resumen de la orden creada
     */
    OrderSummaryResponseDto createOrderFromCart(Long userId, OrderRequestDto orderRequest);
    
    /**
     * Obtiene el detalle de una orden específica
     * @param orderId ID de la orden
     * @param userId ID del usuario (para validación)
     * @return Detalle de la orden
     */
    OrderSummaryResponseDto getOrderById(Long orderId, Long userId);
    
    /**
     * Obtiene todas las órdenes de un usuario
     * @param userId ID del usuario
     * @param page número de página
     * @param size tamaño de página
     * @return Lista paginada de órdenes del usuario
     */
    OrderPageResponseDto getUserOrders(Long userId, int page, int size);
    
    /**
     * Obtiene todas las órdenes (solo admin)
     * @param page número de página
     * @param size tamaño de página
     * @return Lista paginada de todas las órdenes
     */
    OrderPageResponseDto getAllOrders(int page, int size);
    
    /**
     * Actualiza el estado de una orden
     * @param orderId ID de la orden
     * @param status Nuevo estado
     * @return Orden actualizada
     */
    OrderSummaryResponseDto updateOrderStatus(Long orderId, String status);
    
    /**
     * Obtiene la última orden realizada por un usuario específico
     * @param userId ID del usuario
     * @return Detalle de la última orden del usuario o null si no tiene órdenes
     */
    OrderSummaryResponseDto getLastOrderByUser(Long userId);
    
    /**
     * Obtiene las últimas órdenes de todos los usuarios
     * @param page número de página
     * @param size tamaño de página
     * @return Lista paginada con la última orden de cada usuario
     */
    OrderPageResponseDto getLatestOrdersForAllUsers(int page, int size);
    
    /**
     * Obtiene la orden más reciente de toda la tienda
     * @return Detalle de la orden más reciente
     */
        OrderPageResponseDto getRecentOrders(int page, int size);

}