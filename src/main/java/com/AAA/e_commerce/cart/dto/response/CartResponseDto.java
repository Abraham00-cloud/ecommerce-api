package com.AAA.e_commerce.cart.dto.response;

import com.AAA.e_commerce.cart.model.CartItem;

import java.math.BigDecimal;
import java.util.Set;

public record CartResponseDto(Set<CartItemResponseDto> cartItems, BigDecimal totalAmount) {
}
