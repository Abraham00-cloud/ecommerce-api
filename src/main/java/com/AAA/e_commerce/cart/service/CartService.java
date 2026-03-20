package com.AAA.e_commerce.cart.service;

import com.AAA.e_commerce.cart.dto.response.CartResponseDto;
import com.AAA.e_commerce.cart.mapper.CartMapper;
import com.AAA.e_commerce.cart.model.Cart;
import com.AAA.e_commerce.cart.model.CartItem;
import com.AAA.e_commerce.cart.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CartService {
    public final CartRepository cartRepository;
    public final CartMapper cartMapper;
    public CartResponseDto createCart() {
        Cart cart = new Cart();
        cart.setTotalAmount(BigDecimal.ZERO);
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponseDto(savedCart);
    }

    public CartResponseDto getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
        return cartMapper.toCartResponseDto(cart);
    }

    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
        cart.getCartItems().forEach(item -> item.setCart(null));
        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);

        cartRepository.save(cart);
    }

    public void reCalculateCartTotal(Cart cart) {
        BigDecimal total = cart.getCartItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(total);
        cartRepository.save(cart);
    }

}
