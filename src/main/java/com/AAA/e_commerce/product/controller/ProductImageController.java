package com.AAA.e_commerce.product.controller;

import com.AAA.e_commerce.product.dto.response.ProductImageResponseDto;
import com.AAA.e_commerce.product.service.ProductImageService;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products/{productId}/images")
@Validated
public class ProductImageController {
    private final ProductImageService service;

    @PostMapping
    public ProductImageResponseDto addImage(
            @PathVariable Long productId, @RequestBody @NotBlank String url) {
        return service.addImages(productId, url);
    }

    @GetMapping
    public List<ProductImageResponseDto> getImages(@PathVariable Long productId) {
        return service.getImagesByProduct(productId);
    }

    @DeleteMapping("/{imageId}")
    public void deleteImage(Long imageId) {
        service.deleteImage(imageId);
    }
}
