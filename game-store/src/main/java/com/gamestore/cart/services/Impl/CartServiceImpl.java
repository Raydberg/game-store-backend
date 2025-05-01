package com.gamestore.cart.services.Impl;

import com.gamestore.cart.mappers.CartMapper;
import com.gamestore.cart.repository.CartRepository;
import com.gamestore.cart.services.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository  cartRepository;
    private final CartMapper cartMapper;

}
