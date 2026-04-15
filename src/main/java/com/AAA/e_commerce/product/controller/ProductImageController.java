package com.AAA.e_commerce.product.controller;

import com.AAA.e_commerce.product.dto.response.ProductImageResponseDto;
import com.AAA.e_commerce.product.service.ProductImageService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products/{productId}/images")
@Validated
public class ProductImageController {
    private final ProductImageService service;

    @PostMapping
    public ProductImageResponseDto addImage(
            @PathVariable Long productId, @RequestBody @NotBlank String url) {
        return service.addImages(productId, url);
    }

    @GetMapping
    public Page<ProductImageResponseDto> getImages(
            @PathVariable Long productId, @ParameterObject Pageable pageable) {
        return service.getImagesByProduct(productId, pageable);
    }

    @DeleteMapping("/{imageId}")
    public void deleteImage(Long imageId) {
        service.deleteImage(imageId);
    }
}
