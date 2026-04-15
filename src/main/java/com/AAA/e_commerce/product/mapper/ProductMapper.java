package com.AAA.e_commerce.product.mapper;

import com.AAA.e_commerce.product.dto.request.ProductRequestDto;
import com.AAA.e_commerce.product.dto.response.ProductImageResponseDto;
import com.AAA.e_commerce.product.dto.response.ProductResponseDto;
import com.AAA.e_commerce.product.model.Product;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductMapper {

    public Product toProduct(ProductRequestDto requestDto) {
        Product product = new Product();
        product.setName(requestDto.name());
        product.setPrice(requestDto.price());
        product.setWeight(requestDto.weight());
        product.setQuantity(requestDto.quantity());
        product.setDescription(requestDto.description());
        return product;
    }

    public ProductResponseDto toProductResponseDto(Product product) {
        List<ProductImageResponseDto> images =
                product.getImages().stream()
                        .map(img -> new ProductImageResponseDto(img.getId(), img.getUrl()))
                        .toList();
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                product.getWeight(),
                product.getDescription(),
                product.getCategory().getName(),
                images);
    }
}
