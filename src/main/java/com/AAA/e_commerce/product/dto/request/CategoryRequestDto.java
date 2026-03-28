package com.AAA.e_commerce.product.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(@NotBlank(message = "Category name is required") String name) {}
