package com.AAA.e_commerce.cart.controller;

import com.AAA.e_commerce.cart.dto.request.CartItemRequestDto;
import com.AAA.e_commerce.cart.dto.response.CartItemResponseDto;
import com.AAA.e_commerce.cart.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cartItem")
@Tag(name = "CartItem api", description = "Endpoints for managing cartItem")
public class CartItemController {
    private final CartItemService service;

    @Operation(summary = "create CartItem")
    @PostMapping("/{cartId}")
    public CartItemResponseDto addCartItem(@PathVariable Long cartId, @RequestBody CartItemRequestDto requestDto) {
        return service.addCartItem(cartId, requestDto);
    }

    @Operation(summary = "update quantity")
    @PatchMapping("/{cartItemId}")
    public CartItemResponseDto updateQuantiy(@PathVariable Long cartItemId,@RequestBody int quantity) {
        return service.updateQuantity(cartItemId, quantity);
    }

    @Operation(summary = "remove cartItem")
    @DeleteMapping("/{cartItemId}")
    public void removeCartItem(@PathVariable Long cartItemId) {
        service.removeCartItem(cartItemId);
    }
}
