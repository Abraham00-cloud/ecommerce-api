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
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CartItemService {
    private final CartItemMapper mapper;
    private final CartItemRepository repository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    public CartItemResponseDto addCartItem(Long cartId, CartItemRequestDto requestDto) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
        Product product = productRepository.findById(requestDto.product_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found") );
        if (requestDto.quantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
        }
        CartItem existingCartItem = repository.findByCartAndProduct(cart, product);

        if (requestDto.quantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
        }
        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + requestDto.quantity();
            existingCartItem.setQuantity(newQuantity);
            existingCartItem.setTotalPrice(BigDecimal.valueOf(newQuantity).multiply(existingCartItem.getUnitPrice()));
            CartItem updatedCartItem = repository.save(existingCartItem);

            cartService.reCalculateCartTotal(cart);
            return mapper.toCartItemResponseDto(updatedCartItem);


        }

        CartItem cartItem = mapper.toCartItem(requestDto);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setUnitPrice(product.getPrice());
        cartItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(requestDto.quantity())));
        cartItem.setQuantity(requestDto.quantity());

        CartItem savedCartItem = repository.save(cartItem);

        cartService.reCalculateCartTotal(cart);
        return mapper.toCartItemResponseDto(savedCartItem);
    }

    public CartItemResponseDto updateQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = repository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CartItem not found"));
        if (quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
        }
        cartItem.setQuantity(quantity);

        cartItem.setTotalPrice(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));

        CartItem updatedCartItem = repository.save(cartItem);
        cartService.reCalculateCartTotal(cartItem.getCart());
        return mapper.toCartItemResponseDto(updatedCartItem);
    }

    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = repository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CartItem not found"));
        Cart cart = cartItem.getCart();
        repository.delete(cartItem);

        cartService.reCalculateCartTotal(cart);

    }

}
