package com.AAA.e_commerce.cart.controller;

import com.AAA.e_commerce.cart.dto.response.CartResponseDto;
import com.AAA.e_commerce.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cart")
@Tag(name = "Cart api", description = "Endpoints for managing cart")
public class CartController {
    private CartService service;

    @Operation(summary = "create Cart")
    @PostMapping
    public CartResponseDto createCart() {
        return service.createCart();
    }

    @Operation(summary = "get Cart")
    @GetMapping("/{cartId}")
    public CartResponseDto getCart(@PathVariable Long cartId) {
        return service.getCart(cartId);
    }

    @Operation(summary = "delete Cart")
    @DeleteMapping("/{cartId}")
    public void clearCart(@PathVariable Long cartId) {
        service.clearCart(cartId);
    }
}
