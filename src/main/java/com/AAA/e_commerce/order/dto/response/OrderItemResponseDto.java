package com.AAA.e_commerce.order.dto.response;

import java.math.BigDecimal;

public record OrderItemResponseDto(Long id, int quantity, BigDecimal unitPrice, BigDecimal totalPrice, Long productId, String productName) {
}
