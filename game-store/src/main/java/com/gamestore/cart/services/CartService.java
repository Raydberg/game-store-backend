package com.gamestore.cart.services;

import com.gamestore.cart.DTOs.CartItemRequestDto;
import com.gamestore.cart.DTOs.CartResponseDto;

public interface CartService {

    CartResponseDto getUserCart(Long userId);

    CartResponseDto addItemToCart(Long userId, CartItemRequestDto itemDto);

    CartResponseDto updateCartItemQuantity(Long userId, Long itemId, int quantity);

    CartResponseDto removeCartItem(Long userId, Long itemId);

    void clearCart(Long userId);
}