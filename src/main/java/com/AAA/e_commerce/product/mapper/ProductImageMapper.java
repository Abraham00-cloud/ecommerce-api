package com.AAA.e_commerce.product.mapper;

import com.AAA.e_commerce.product.dto.response.ProductImageResponseDto;
import com.AAA.e_commerce.product.model.ProductImage;

public class ProductImageMapper {
    public ProductImageResponseDto toProductImageResponseDto(ProductImage productImage) {
        ProductImageResponseDto productImageResponseDto = new ProductImageResponseDto(productImage.getId(), productImage.getUrl());
        return productImageResponseDto;
    }
}
