package com.AAA.e_commerce.cart.mapper;

import com.AAA.e_commerce.cart.dto.response.CartItemResponseDto;
import com.AAA.e_commerce.cart.dto.response.CartResponseDto;
import com.AAA.e_commerce.cart.model.Cart;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartMapper {
    private final CartItemMapper cartItemMapper;
    public CartResponseDto toCartResponseDto(Cart cart) {
        Set<CartItemResponseDto> cartItemResponseDto =cart.getCartItems() == null ? Collections.emptySet(): cart.getCartItems()
                .stream()
                .map(cartItemMapper::toCartItemResponseDto)
                .collect(Collectors.toSet());;
        return  new CartResponseDto(cartItemResponseDto, cart.getTotalAmount());
    }
}
