package com.AAA.e_commerce.order.controller;

import com.AAA.e_commerce.order.dto.response.OrderResponseDto;
import com.AAA.e_commerce.order.model.OrderStatus;
import com.AAA.e_commerce.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@RestController
@AllArgsConstructor
@RequestMapping("/api/order")
@Tag(name = "Order api", description = "Endpoints for managing Orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "create Order")
    @PostMapping("/checkout/{cartId}")
    public OrderResponseDto createOrder(@PathVariable Long cartId) {
        return orderService.createOrder(cartId);
    }

    @Operation(summary = "get all Order")
    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(summary = "get Order by id")
    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @Operation(summary = "update Order status")
    @PatchMapping("/{orderId}/status")
    public OrderResponseDto updateStatus(@PathVariable Long orderId, @RequestBody OrderStatus status){
        return orderService.updateStatus(orderId, status);
    }

    @Operation(summary = "cancel Order")
    @DeleteMapping("/{orderId}")
    public OrderResponseDto cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}
