package com.AAA.e_commerce.cart.controller;

import com.AAA.e_commerce.cart.dto.request.CartItemRequestDto;
import com.AAA.e_commerce.cart.dto.response.CartItemResponseDto;
import com.AAA.e_commerce.cart.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cartItem")
@Validated
@Tag(name = "CartItem api", description = "Endpoints for managing cartItem")
public class CartItemController {
    private final CartItemService service;

    @Operation(summary = "add CartItem")
    @PostMapping("/add-item")
    public CartItemResponseDto addCartItem(@RequestBody @Valid CartItemRequestDto requestDto) {
        return service.addCartItem(requestDto);
    }

    @Operation(summary = "update quantity")
    @PatchMapping("/{cartItemId}")
    public CartItemResponseDto updateQuantity(
            @PathVariable Long cartItemId, @RequestParam @Positive int quantity) {
        return service.updateQuantity(cartItemId, quantity);
    }

    @Operation(summary = "remove cartItem")
    @DeleteMapping("/{cartItemId}")
    public void removeCartItem(@PathVariable Long cartItemId) {
        service.removeCartItem(cartItemId);
    }
}
