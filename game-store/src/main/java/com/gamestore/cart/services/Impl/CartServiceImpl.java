package com.gamestore.cart.services.Impl;

import com.gamestore.auth.repository.UserRepository;
import com.gamestore.cart.DTOs.CartItemRequestDto;
import com.gamestore.cart.DTOs.CartResponseDto;
import com.gamestore.cart.mappers.CartMapper;
import com.gamestore.cart.model.Cart;
import com.gamestore.cart.model.CartItem;
import com.gamestore.cart.repository.CartItemRepository;
import com.gamestore.cart.repository.CartRepository;
import com.gamestore.cart.services.CartService;
import com.gamestore.product.model.Product;
import com.gamestore.product.repository.ProductRepository;
import com.gamestore.users.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    public CartResponseDto getUserCart(Long userId) {
        Cart cart = findOrCreateCart(userId);
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId().longValue());
        return cartMapper.toCartResponseDto(cart, items);
    }

    @Override
    @Transactional
    public CartResponseDto addItemToCart(Long userId, CartItemRequestDto itemDto) {
        Cart cart = findOrCreateCart(userId);

        Product product = productRepository.findById(itemDto.productId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemDto.productId()));

        if (product.getStock() < itemDto.quantity()) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + product.getStock());
        }

        CartItem existingItem = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + itemDto.quantity();
            if (product.getStock() < newQuantity) {
                throw new RuntimeException("Stock insuficiente para la cantidad total. Disponible: " + product.getStock());
            }
            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);
        } else {
            // Crear nuevo item
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(itemDto.quantity())
                    .build();
            cartItemRepository.save(newItem);
        }

        List<CartItem> updatedItems = cartItemRepository.findByCartId(cart.getId().longValue());
        return cartMapper.toCartResponseDto(cart, updatedItems);
    }

    @Override
    @Transactional
    public CartResponseDto updateCartItemQuantity(Long userId, Long itemId, int quantity) {
        Cart cart = findOrCreateCart(userId);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item del carrito no encontrado: " + itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("El item no pertenece al carrito del usuario");
        }

        if (item.getProduct().getStock() < quantity) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + item.getProduct().getStock());
        }

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            // Actualizar cantidad
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        List<CartItem> updatedItems = cartItemRepository.findByCartId(cart.getId().longValue());
        return cartMapper.toCartResponseDto(cart, updatedItems);
    }

    @Override
    @Transactional
    public CartResponseDto removeCartItem(Long userId, Long itemId) {
        Cart cart = findOrCreateCart(userId);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item del carrito no encontrado: " + itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("El item no pertenece al carrito del usuario");
        }

        cartItemRepository.delete(item);

        List<CartItem> updatedItems = cartItemRepository.findByCartId(cart.getId().longValue());
        return cartMapper.toCartResponseDto(cart, updatedItems);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElse(null);

        if (cart != null) {
            cartItemRepository.deleteByCartId(cart.getId().longValue());
        }
    }


    private Cart findOrCreateCart(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userId));

        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .createdAt(Instant.now())
                            .items(new ArrayList<>())
                            .build();
                    return cartRepository.save(newCart);
                });
    }
}