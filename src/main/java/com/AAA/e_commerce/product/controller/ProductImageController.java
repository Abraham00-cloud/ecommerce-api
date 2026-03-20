package com.AAA.e_commerce.product.controller;

import com.AAA.e_commerce.product.dto.response.ProductImageResponseDto;
import com.AAA.e_commerce.product.service.ProductImageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products/{productId}/images")
public class ProductImageController {
    private final ProductImageService service;

    @PostMapping
    public ProductImageResponseDto addImage(@PathVariable Long productId, @RequestBody String url) {
        return service.addImages(productId, url);
    }

    @GetMapping
    public List<ProductImageResponseDto> getImages(@PathVariable Long productID) {
        return service.getImagesByProduct(productID);
    }

    @DeleteMapping
    public void deleteImage(Long imageId) {
        service.deleteImage(imageId);
    }
}
