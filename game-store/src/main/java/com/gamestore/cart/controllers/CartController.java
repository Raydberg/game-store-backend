package com.gamestore.cart.controllers;

import com.gamestore.cart.DTOs.CartItemRequestDto;
import com.gamestore.cart.DTOs.CartResponseDto;
import com.gamestore.cart.services.CartService;
import com.gamestore.config.helpers.AuthenticationHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final AuthenticationHelper authHelper;

    /**
     * Obtiene el carrito del usuario autenticado
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponseDto> getMyCart() {
        Long userId = authHelper.getCurrentUserId();
        CartResponseDto cart = cartService.getUserCart(userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Agrega un producto al carrito
     */
    @PostMapping("/items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponseDto> addItemToCart(@Valid @RequestBody CartItemRequestDto dto) {
        Long userId = authHelper.getCurrentUserId();
        CartResponseDto updatedCart = cartService.addItemToCart(userId, dto);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * Actualiza la cantidad de un producto en el carrito
     */
    @PutMapping("/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponseDto> updateCartItem(
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        Long userId = authHelper.getCurrentUserId();
        CartResponseDto updatedCart = cartService.updateCartItemQuantity(userId, itemId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * Elimina un producto del carrito
     */
    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponseDto> removeCartItem(@PathVariable Long itemId) {
        Long userId = authHelper.getCurrentUserId();
        CartResponseDto updatedCart = cartService.removeCartItem(userId, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * Vacía el carrito
     */
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> clearCart() {
        Long userId = authHelper.getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}