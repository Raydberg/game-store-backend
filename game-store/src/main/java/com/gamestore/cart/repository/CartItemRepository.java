package com.gamestore.cart.repository;

import com.gamestore.cart.model.Cart;
import com.gamestore.cart.model.CartItem;
import com.gamestore.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    void deleteByCartId(Long cartId);
}