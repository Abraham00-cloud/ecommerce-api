package com.AAA.e_commerce.order.controller;

import com.AAA.e_commerce.order.dto.response.OrderResponseDto;
import com.AAA.e_commerce.order.model.OrderStatus;
import com.AAA.e_commerce.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Service
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/order")
@Validated
@Tag(name = "Order api", description = "Endpoints for managing Orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "create Order")
    @PostMapping("/checkout/purchase")
    public OrderResponseDto createOrder() {
        return orderService.createOrder();
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "get all Orders for a user")
    @GetMapping("/mine")
    public Page<OrderResponseDto> getUserOrders(@ParameterObject Pageable pageable) {
        return orderService.getMyOrders(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "get all Order")
    @GetMapping("/all")
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "get Order by id")
    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "update Order status")
    @PatchMapping("/{orderId}/status")
    public OrderResponseDto updateStatus(
            @PathVariable Long orderId,
            @RequestBody @NotNull(message = "Status is required") OrderStatus status) {
        return orderService.updateStatus(orderId, status);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "cancel Order")
    @DeleteMapping("/{orderId}")
    public OrderResponseDto cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}
