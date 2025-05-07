package com.gamestore.order.mappers;

import com.gamestore.address.DTOs.AddressResponseDto;
import com.gamestore.address.mappers.AddressMapper;
import com.gamestore.order.DTOs.OrderItemResponseDto;
import com.gamestore.order.DTOs.OrderSummaryResponseDto;
import com.gamestore.order.DTOs.PaymentResponseDto;
import com.gamestore.order.model.Order;
import com.gamestore.order.model.OrderItem;
import com.gamestore.order.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface OrderMapper {

    @Mapping(target = "id", source = "order.id")
    @Mapping(target = "userId", source = "order.user.id")
    @Mapping(target = "userEmail", source = "order.user.email")
    @Mapping(target = "userName", expression = "java(order.getUser().getFirstName() + ' ' + order.getUser().getLastName())")
    @Mapping(target = "shippingAddress", source = "order.address")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "subtotal", expression = "java(calculateSubtotal(items))")
    @Mapping(target = "total", source = "order.totalAmount")
    @Mapping(target = "itemCount", expression = "java(countItems(items))")
    @Mapping(target = "status", source = "order.status")
    @Mapping(target = "payment", source = "payment")
    @Mapping(target = "createdAt", source = "order.createdAt")
    OrderSummaryResponseDto toOrderSummaryResponseDto(Order order, List<OrderItem> items, Payment payment);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "productId", source = "item.product.id")
    @Mapping(target = "productName", source = "item.product.name")
    @Mapping(target = "productImage", source = "item.product.imageUrl")
    @Mapping(target = "unitPrice", source = "item.unitPrice")
    @Mapping(target = "quantity", source = "item.quantity")
    @Mapping(target = "subtotal", expression = "java(calculateOrderItemSubtotal(item))")
    OrderItemResponseDto toOrderItemResponseDto(OrderItem item);

    @Mapping(target = "id", source = "payment.id")
    @Mapping(target = "amount", source = "payment.amount")
    @Mapping(target = "method", source = "payment.method")
    @Mapping(target = "paidAt", source = "payment.paidAt")
    PaymentResponseDto toPaymentResponseDto(Payment payment);

    default BigDecimal calculateSubtotal(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(this::calculateOrderItemSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    default BigDecimal calculateOrderItemSubtotal(OrderItem item) {
        if (item == null || item.getUnitPrice() == null) {
            return BigDecimal.ZERO;
        }
        return item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    default int countItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        return items.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }
}