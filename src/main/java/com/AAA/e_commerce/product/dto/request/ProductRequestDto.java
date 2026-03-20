package com.AAA.e_commerce.product.dto.request;

import java.math.BigDecimal;

public record ProductRequestDto(
        String name,
        BigDecimal price,
        long weight,
        String description,
        Long categoryId) {
}
