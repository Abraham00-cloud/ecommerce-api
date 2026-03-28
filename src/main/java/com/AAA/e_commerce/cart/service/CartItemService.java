package com.AAA.e_commerce.cart.service;

import com.AAA.e_commerce.cart.dto.request.CartItemRequestDto;
import com.AAA.e_commerce.cart.dto.response.CartItemResponseDto;
import com.AAA.e_commerce.cart.mapper.CartItemMapper;
import com.AAA.e_commerce.cart.model.Cart;
import com.AAA.e_commerce.cart.model.CartItem;
import com.AAA.e_commerce.cart.repository.CartItemRepository;
import com.AAA.e_commerce.cart.repository.CartRepository;
import com.AAA.e_commerce.product.model.Product;
import com.AAA.e_commerce.product.repository.ProductRepository;
import com.AAA.e_commerce.user.model.User;
import com.AAA.e_commerce.user.service.UserService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class CartItemService {
    private final CartItemMapper mapper;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final UserService userService;

    @Transactional
    public CartItemResponseDto addCartItem(CartItemRequestDto requestDto) {
        User user = userService.getAuthenticatedUser();
        Cart cart = user.getCart();
        Product product =
                productRepository
                        .findById(requestDto.product_id())
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Product not found"));
        if (requestDto.quantity() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
        }
        CartItem cartItem =
                cartItemRepository
                        .findByCartAndProduct(cart, product)
                        .orElseGet(
                                () -> {
                                    CartItem newItem = new CartItem();
                                    newItem.setCart(cart);
                                    newItem.setProduct(product);
                                    newItem.setQuantity(0);
                                    newItem.setUnitPrice(product.getPrice());
                                    return newItem;
                                });

        cartItem.updateQuantityAndPrice(requestDto.quantity(), product.getPrice());

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        cartService.reCalculateCartTotal(cart);
        return mapper.toCartItemResponseDto(savedCartItem);
    }

    public CartItemResponseDto updateQuantity(Long cartItemId, int quantity) {
        CartItem cartItem =
                cartItemRepository
                        .findById(cartItemId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "CartItem not found"));
        if (quantity <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
        }
        cartItem.setQuantity(quantity);

        cartItem.setTotalPrice(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));

        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        cartService.reCalculateCartTotal(cartItem.getCart());
        return mapper.toCartItemResponseDto(updatedCartItem);
    }

    public void removeCartItem(Long cartItemId) {
        CartItem cartItem =
                cartItemRepository
                        .findById(cartItemId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "CartItem not found"));
        Cart cart = cartItem.getCart();
        cartItemRepository.delete(cartItem);

        cartService.reCalculateCartTotal(cart);
    }
}
