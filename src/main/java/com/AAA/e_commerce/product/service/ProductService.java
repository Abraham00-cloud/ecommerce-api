package com.AAA.e_commerce.product.service;

import com.AAA.e_commerce.product.dto.request.ProductRequestDto;
import com.AAA.e_commerce.product.dto.response.ProductResponseDto;
import com.AAA.e_commerce.product.mapper.ProductMapper;
import com.AAA.e_commerce.product.model.Category;
import com.AAA.e_commerce.product.model.Product;
import com.AAA.e_commerce.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final CategoryService categoryService;

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = mapper.toProduct(requestDto);
        Category category = categoryService.getCategoryById(requestDto.categoryId());
        product.setCategory(category);

        Product savedProduct = repository.save(product);
        return mapper.toProductResponseDto(savedProduct);
    }

    public ProductResponseDto getProduct(Long id) {
        Product product =
                repository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "product not found"));
        return mapper.toProductResponseDto(product);
    }

    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage = repository.findAll(pageable);
        return productPage.map(mapper::toProductResponseDto);
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        Product product =
                repository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "product not found"));
        Category category = categoryService.getCategoryById(requestDto.categoryId());

        product.setName(requestDto.name());
        product.setPrice(requestDto.price());
        product.setWeight(requestDto.weight());
        product.setDescription(requestDto.description());
        product.setQuantity(requestDto.quantity());
        product.setCategory(category);

        Product updatedProduct = repository.save(product);
        return mapper.toProductResponseDto(product);
    }

    public void deleteProduct(Long id) {
        Product product =
                repository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "product not found"));
        repository.delete(product);
    }

    public Product getProductById(Long productId) {
        Product product =
                repository
                        .findById(productId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Product not found"));
        return product;
    }

    public void saveProduct(Product product) {
        Product savedProduct = repository.save(product);
    }
}
