package com.gamestore.order.services.Impl;

import com.gamestore.address.model.UserAddress;
import com.gamestore.address.repository.AddressRepository;
import com.gamestore.auth.repository.UserRepository;
import com.gamestore.cart.model.Cart;
import com.gamestore.cart.model.CartItem;
import com.gamestore.cart.repository.CartItemRepository;
import com.gamestore.cart.repository.CartRepository;
import com.gamestore.cart.services.CartService;
import com.gamestore.order.DTOs.OrderItemResponseDto;
import com.gamestore.order.DTOs.OrderPageResponseDto;
import com.gamestore.order.DTOs.OrderRequestDto;
import com.gamestore.order.DTOs.OrderSummaryResponseDto;
import com.gamestore.order.enums.OrderStatus;
import com.gamestore.order.mappers.OrderMapper;
import com.gamestore.order.model.Order;
import com.gamestore.order.model.OrderItem;
import com.gamestore.order.model.Payment;
import com.gamestore.order.repository.OrderItemRepository;
import com.gamestore.order.repository.OrderRepository;
import com.gamestore.order.repository.PaymentRepository;
import com.gamestore.order.services.OrderService;
import com.gamestore.users.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderMapper orderMapper;
    private final CartService cartService;

    @Override
    @Transactional
    public OrderSummaryResponseDto createOrderFromCart(Long userId, OrderRequestDto orderRequest) {
        // 1. Obtener el usuario y validar
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Obtener la dirección de envío
        UserAddress address = addressRepository.findById(orderRequest.addressId())
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        // Verificar que la dirección pertenece al usuario
        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("La dirección no pertenece al usuario");
        }

        // 3. Obtener el carrito actual del usuario
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // 4. Crear la orden
        BigDecimal totalAmount = calculateCartTotal(cartItems);

        Order order = Order.builder()
                .user(user)
                .address(address)
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING.name())
                .createdAt(Instant.now())
                .items(new ArrayList<>())
                .build();

        Order savedOrder = orderRepository.save(order);

        // 5. Crear los items de la orden
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .order(savedOrder)
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .unitPrice(cartItem.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);

        // 6. Crear el pago asociado
        Payment payment = Payment.builder()
                .order(savedOrder)
                .amount(totalAmount)
                .method(orderRequest.paymentMethod())
                .paidAt(Instant.now())
                .build();

        paymentRepository.save(payment);

        // 7. Vaciar el carrito después de crear la orden
        cartService.clearCart(userId);

        // 8. Devolver el resumen de la orden
        return orderMapper.toOrderSummaryResponseDto(savedOrder, orderItems, payment);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderSummaryResponseDto getOrderById(Long orderId, Long userId) {
        Order order;
        if (userId == null) {
            // Si userId es null, asumimos que es un admin quien solicita la orden
            order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        } else {
            // Si hay userId, verificamos que la orden pertenezca al usuario
            order = orderRepository.findByIdAndUserId(orderId, userId)
                    .orElseThrow(() -> new RuntimeException("Orden no encontrada o no pertenece al usuario"));
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        Payment payment = paymentRepository.findByOrderId(orderId).orElse(null);

        return orderMapper.toOrderSummaryResponseDto(order, orderItems, payment);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPageResponseDto getUserOrders(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> ordersPage = orderRepository.findByUserId(userId, pageable);

        List<OrderSummaryResponseDto> orderSummaries = ordersPage.getContent().stream()
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    Payment payment = paymentRepository.findByOrderId(order.getId()).orElse(null);
                    return orderMapper.toOrderSummaryResponseDto(order, items, payment);
                })
                .collect(Collectors.toList());

        return new OrderPageResponseDto(
                orderSummaries,
                ordersPage.getNumber(),
                ordersPage.getTotalElements(),
                ordersPage.getTotalPages());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPageResponseDto getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> ordersPage = orderRepository.findAll(pageable);

        List<OrderSummaryResponseDto> orderSummaries = ordersPage.getContent().stream()
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    Payment payment = paymentRepository.findByOrderId(order.getId()).orElse(null);
                    return orderMapper.toOrderSummaryResponseDto(order, items, payment);
                })
                .collect(Collectors.toList());

        return new OrderPageResponseDto(
                orderSummaries,
                ordersPage.getNumber(),
                ordersPage.getTotalElements(),
                ordersPage.getTotalPages());
    }

    @Override
    @Transactional
    public OrderSummaryResponseDto updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // Validar el status - usando enum para evitar errores
        try {
            OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de orden no válido: " + status);
        }

        order.setStatus(status.toUpperCase());
        Order updatedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        Payment payment = paymentRepository.findByOrderId(orderId).orElse(null);

        return orderMapper.toOrderSummaryResponseDto(updatedOrder, orderItems, payment);
    }

    // Método auxiliar para calcular el total del carrito
    private BigDecimal calculateCartTotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderSummaryResponseDto getLastOrderByUser(Long userId) {
        Order lastOrder = orderRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new RuntimeException("No se encontraron órdenes para el usuario"));

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(lastOrder.getId());
        Payment payment = paymentRepository.findByOrderId(lastOrder.getId()).orElse(null);

        return orderMapper.toOrderSummaryResponseDto(lastOrder, orderItems, payment);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPageResponseDto getLatestOrdersForAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Order> latestOrders = orderRepository.findLatestOrdersForAllUsers(pageable);

        List<OrderSummaryResponseDto> orderSummaries = latestOrders.stream()
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    Payment payment = paymentRepository.findByOrderId(order.getId()).orElse(null);
                    return orderMapper.toOrderSummaryResponseDto(order, items, payment);
                })
                .collect(Collectors.toList());

        // Como no tenemos un Page<> directamente, calculamos el total de elementos
        // contando usuarios distintos con órdenes
        long totalUsers = userRepository.count(); // Esto es una aproximación

        return new OrderPageResponseDto(
                orderSummaries,
                page,
                totalUsers,
                (int) Math.ceil((double) totalUsers / size));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPageResponseDto getRecentOrders(int page, int size) {
        // Ordenar por fecha de creación descendente (más recientes primero)
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> ordersPage = orderRepository.findAll(pageable);

        List<OrderSummaryResponseDto> orderSummaries = ordersPage.getContent().stream()
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    Payment payment = paymentRepository.findByOrderId(order.getId()).orElse(null);
                    return orderMapper.toOrderSummaryResponseDto(order, items, payment);
                })
                .collect(Collectors.toList());

        return new OrderPageResponseDto(
                orderSummaries,
                ordersPage.getNumber(),
                ordersPage.getTotalElements(),
                ordersPage.getTotalPages());
    }
}