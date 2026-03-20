package com.AAA.e_commerce.cart.mapper;

import com.AAA.e_commerce.cart.dto.request.CartItemRequestDto;
import com.AAA.e_commerce.cart.dto.response.CartItemResponseDto;
import com.AAA.e_commerce.cart.model.CartItem;
import org.springframework.stereotype.Service;

@Service
public class CartItemMapper {
    public CartItem toCartItem(CartItemRequestDto requestDto) {
        CartItem cartItem = new  CartItem();
        cartItem.setQuantity(requestDto.quantity());
        return cartItem;
    }

    public CartItemResponseDto toCartItemResponseDto(CartItem cartItem) {
        return new CartItemResponseDto(cartItem.getId(),cartItem.getQuantity(), cartItem.getUnitPrice(), cartItem.getTotalPrice(),cartItem.getProduct().getId(), cartItem.getProduct().getName());
    }
}
