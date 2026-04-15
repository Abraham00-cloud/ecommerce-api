package com.AAA.e_commerce.product.controller;

import com.AAA.e_commerce.product.dto.request.ProductRequestDto;
import com.AAA.e_commerce.product.dto.response.ProductResponseDto;
import com.AAA.e_commerce.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products")
@Tag(name = "Product Api", description = "Endpoints for managing products")
public class ProductController {
    private final ProductService service;

    @PostMapping
    @Operation(summary = "create Product", description = "create new product in the system")
    public ProductResponseDto createProduct(@RequestBody @Valid ProductRequestDto requestDto) {
        return service.createProduct(requestDto);
    }

    @GetMapping
    @Operation(summary = "get all products")
    public Page<ProductResponseDto> getAllProduct(@ParameterObject Pageable pageable) {
        return service.getAllProducts(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "get product by id")
    public ProductResponseDto getProduct(@PathVariable Long id) {
        return service.getProduct(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update product")
    public ProductResponseDto updateProduct(
            @PathVariable Long id, @RequestBody @Valid ProductRequestDto requestDto) {
        return service.updateProduct(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete product")
    public void deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
    }
}
