package com.AAA.e_commerce.order.dto.response;

import com.AAA.e_commerce.order.model.OrderStatus;

import java.math.BigDecimal;
import java.util.Set;

public record OrderResponseDto(Long orderId, BigDecimal totalAmount, OrderStatus orderStatus, Set<OrderItemResponseDto> orderItems) {
}
