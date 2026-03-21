package com.AAA.e_commerce.order.mapper;

import com.AAA.e_commerce.order.dto.response.OrderItemResponseDto;
import com.AAA.e_commerce.order.dto.response.OrderResponseDto;
import com.AAA.e_commerce.order.model.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;
    public OrderResponseDto toOrderResponseDto(Order order) {
        Set<OrderItemResponseDto> orderItemResponse = order.getOrderItems()
                .stream()
                .map(orderItemMapper::toResponseDto)
                .collect(Collectors.toSet());
        return new OrderResponseDto(order.getId(), order.getTotalAmount(), order.getOrderStatus(), orderItemResponse);
    }
}
