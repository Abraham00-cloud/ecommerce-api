package com.AAA.e_commerce.product.service;

import com.AAA.e_commerce.product.dto.request.ProductRequestDto;
import com.AAA.e_commerce.product.dto.response.ProductResponseDto;
import com.AAA.e_commerce.product.mapper.ProductMapper;
import com.AAA.e_commerce.product.model.Category;
import com.AAA.e_commerce.product.model.Product;
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
public class ProductService {
    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = mapper.toProduct(requestDto);
        Category category = categoryRepository.findById(requestDto.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        product.setCategory(category);

        Product savedProduct = repository.save(product);
        return mapper.toProductResponseDto(savedProduct);
    }

    public ProductResponseDto getProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found")) ;
        return mapper.toProductResponseDto(product);
    }
    public List<ProductResponseDto> getAllProducts() {
        return repository.findAll()
                .stream()
                .map(mapper::toProductResponseDto)
                .toList();
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
        Category category = categoryRepository.findById(requestDto.categoryId())
                .orElseThrow();

        product.setName(requestDto.name());
        product.setPrice(requestDto.price());
        product.setWeight(requestDto.weight());
        product.setDescription(requestDto.description());
        product.setCategory(category);

        Product updatedProduct = repository.save(product);
        return mapper.toProductResponseDto(product);
    }

    public void deleteProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
        repository.delete(product);


    }
}
