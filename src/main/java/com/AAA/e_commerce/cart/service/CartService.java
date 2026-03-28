package com.AAA.e_commerce.cart.service;

import com.AAA.e_commerce.cart.dto.response.CartResponseDto;
import com.AAA.e_commerce.cart.mapper.CartMapper;
import com.AAA.e_commerce.cart.model.Cart;
import com.AAA.e_commerce.cart.model.CartItem;
import com.AAA.e_commerce.cart.repository.CartRepository;
import com.AAA.e_commerce.user.model.User;
import com.AAA.e_commerce.user.service.UserService;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private UserService userService;

    public CartResponseDto createCart() {
        Cart cart = new Cart();
        cart.setTotalAmount(BigDecimal.ZERO);
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponseDto(savedCart);
    }

    public CartResponseDto getMyCart() {
        User user = userService.getAuthenticatedUser();
        Cart cart = user.getCart();

        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not initialized");
        }
        return cartMapper.toCartResponseDto(cart);
    }

    public void clearCart() {
        User user = userService.getAuthenticatedUser();
        Cart cart = user.getCart();
        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for this user");
        }
        cart.getCartItems().forEach(item -> item.setCart(null));
        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);

        cartRepository.save(cart);
    }

    public void reCalculateCartTotal(Cart cart) {
        BigDecimal total =
                cart.getCartItems().stream()
                        .map(CartItem::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(total);
        cartRepository.save(cart);
    }
}
