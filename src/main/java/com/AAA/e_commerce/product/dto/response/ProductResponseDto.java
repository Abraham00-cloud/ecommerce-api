package com.AAA.e_commerce.product.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDto(
        Long id,
        String name,
        BigDecimal price,
        Long quantity,
        long weight,
        String description,
        String category,
        List<ProductImageResponseDto> images) {}
