package com.AAA.e_commerce.product.mapper;

import com.AAA.e_commerce.product.dto.request.ProductRequestDto;
import com.AAA.e_commerce.product.dto.response.ProductImageResponseDto;
import com.AAA.e_commerce.product.dto.response.ProductResponseDto;
import com.AAA.e_commerce.product.model.Category;
import com.AAA.e_commerce.product.model.Product;
import com.AAA.e_commerce.product.model.ProductImage;
import com.AAA.e_commerce.product.repository.CategoryRepository;
import com.AAA.e_commerce.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductMapper {

    public Product toProduct(ProductRequestDto requestDto) {
        Product product = new Product();
        product.setName(requestDto.name());
        product.setPrice(requestDto.price());
        product.setWeight(requestDto.weight());
        product.setDescription(requestDto.description());
        return product;

    }
    public ProductResponseDto toProductResponseDto(Product product) {
        List<ProductImageResponseDto> iamges = product.getImages()
                .stream()
                .map(img -> new ProductImageResponseDto(img.getId(), img.getUrl()))
                .toList();
        ProductResponseDto responseDto = new ProductResponseDto(product.getId(), product.getName(), product.getPrice(), product.getWeight(), product.getDescription(), product.getCategory().getName(), iamges);
        return responseDto;
    }


}
