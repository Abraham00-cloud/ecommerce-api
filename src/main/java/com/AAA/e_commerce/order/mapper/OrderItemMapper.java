package com.AAA.e_commerce.order.mapper;

import com.AAA.e_commerce.order.dto.request.OrderItemRequestDto;
import com.AAA.e_commerce.order.dto.response.OrderItemResponseDto;
import com.AAA.e_commerce.order.model.Order;
import com.AAA.e_commerce.order.model.OrderItem;
import org.springframework.stereotype.Service;

@Service
public class OrderItemMapper {
    public OrderItem toOrderItem(OrderItemRequestDto requestDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(requestDto.quantity());
        return orderItem;
    }
    public OrderItemResponseDto toResponseDto(OrderItem orderItem) {
        return new OrderItemResponseDto(orderItem.getId(), orderItem.getQuantity(), orderItem.getUnitPrice(), orderItem.getTotalPrice(), orderItem.getProduct().getId(), orderItem.getProduct().getName());
    }
}
