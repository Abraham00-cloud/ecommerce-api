package com.AAA.e_commerce.cart.controller;

import com.AAA.e_commerce.cart.dto.response.CartResponseDto;
import com.AAA.e_commerce.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cart")
@Tag(name = "Cart api", description = "Endpoints for managing cart")
public class CartController {
    private CartService service;

    @Operation(summary = "create Cart")
    @PostMapping
    public CartResponseDto createCart() {
        return service.createCart();
    }

    @Operation(summary = "get user Cart")
    @GetMapping("/mine")
    public CartResponseDto getCart() {
        return service.getMyCart();
    }

    @Operation(summary = "delete Cart")
    @DeleteMapping("/clear")
    public void clearCart(Long cartId) {
        service.clearCart();
    }
}
