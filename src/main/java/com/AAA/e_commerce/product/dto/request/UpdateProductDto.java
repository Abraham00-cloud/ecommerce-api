package com.AAA.e_commerce.product.dto.request;

import java.math.BigDecimal;

public record UpdateProductDto(
        String name,
        BigDecimal price,
        long weight,
        String description,
        Long categoryId
) {
}
