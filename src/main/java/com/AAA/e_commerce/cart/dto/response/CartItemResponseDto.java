package com.AAA.e_commerce.cart.dto.response;

import java.math.BigDecimal;

public record CartItemResponseDto(
        Long id,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        Long product_id,
        String productName) {}
