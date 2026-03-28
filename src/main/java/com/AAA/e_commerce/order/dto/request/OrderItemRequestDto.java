package com.AAA.e_commerce.order.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequestDto(
        @Positive(message = "Order item quantity should be positive") int quantity,
        @NotNull(message = "Product ID should be provided") Long productId) {}
