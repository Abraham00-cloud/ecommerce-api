package com.AAA.e_commerce.product.controller;

import com.AAA.e_commerce.product.dto.response.ProductImageResponseDto;
import com.AAA.e_commerce.product.service.ProductImageService;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products/{productId}/images")
@Validated
public class ProductImageController {
    private final ProductImageService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductImageResponseDto addImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) {
        return service.addImages(productId, file);
    }

    @GetMapping
    public Page<ProductImageResponseDto> getImages(
            @PathVariable Long productId, @ParameterObject Pageable pageable) {
        return service.getImagesByProduct(productId, pageable);
    }

    @DeleteMapping("/{imageId}")
    public void deleteImage(@PathVariable Long imageId) {
        service.deleteImage(imageId);
    }
}