package com.gamestore.order.services.Impl;

import com.gamestore.order.mappers.OrderMapper;
import com.gamestore.order.repository.OrderRepository;
import com.gamestore.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
}
