package com.gamestore.cart.mappers;

import com.gamestore.cart.DTOs.CartItemResponseDto;
import com.gamestore.cart.DTOs.CartResponseDto;
import com.gamestore.cart.model.Cart;
import com.gamestore.cart.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "id", source = "cart.id")
    @Mapping(target = "userId", source = "cart.user.id")
    @Mapping(target = "items", source = "cartItems")
    @Mapping(target = "total", expression = "java(calculateTotal(cartItems))")
    @Mapping(target = "itemCount", expression = "java(countItems(cartItems))")
    CartResponseDto toCartResponseDto(Cart cart, List<CartItem> cartItems);

    @Mapping(target = "id", source = "cartItem.id")
    @Mapping(target = "productId", source = "cartItem.product.id")
    @Mapping(target = "productName", source = "cartItem.product.name")
    @Mapping(target = "productImage", source = "cartItem.product.imageUrl")
    @Mapping(target = "unitPrice", source = "cartItem.product.price")
    @Mapping(target = "quantity", source = "cartItem.quantity")
    @Mapping(target = "subtotal", expression = "java(calculateSubtotal(cartItem))")
    CartItemResponseDto toCartItemResponseDto(CartItem cartItem);

    default BigDecimal calculateTotal(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(this::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    default BigDecimal calculateSubtotal(CartItem item) {
        if (item == null || item.getProduct() == null) {
            return BigDecimal.ZERO;
        }
        return item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    default int countItems(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}