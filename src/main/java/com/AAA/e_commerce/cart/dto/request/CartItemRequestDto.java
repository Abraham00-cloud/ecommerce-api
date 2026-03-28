package com.AAA.e_commerce.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemRequestDto(
        @NotNull(message = "product ID cannot be null") Long product_id,
        @Positive(message = "quantity cannot be negative") int quantity) {}
