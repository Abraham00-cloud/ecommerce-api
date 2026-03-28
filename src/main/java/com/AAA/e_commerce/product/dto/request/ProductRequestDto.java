package com.AAA.e_commerce.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ProductRequestDto(
        @NotBlank(message = "Product name is required") String name,
        @NotNull(message = "Price is required")
                @Positive(message = "Price must be greater than zero")
                BigDecimal price,
        @Positive(message = "Weight must be a positive number") long weight,
        @NotNull(message = "Quantity is required")
                @Positive(message = "Quantity must be a positive number")
                Long quantity,
        @NotBlank(message = "Description is required") String description,
        @NotNull(message = "Category ID is required") Long categoryId) {}
