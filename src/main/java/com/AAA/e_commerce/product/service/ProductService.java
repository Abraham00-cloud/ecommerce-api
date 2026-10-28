package com.AAA.e_commerce.product.service;

import com.AAA.e_commerce.product.dto.request.ProductRequestDto;
import com.AAA.e_commerce.product.dto.response.ProductResponseDto;
import com.AAA.e_commerce.product.mapper.ProductMapper;
import com.AAA.e_commerce.product.model.Category;
import com.AAA.e_commerce.product.model.Product;
import com.AAA.e_commerce.product.repository.ProductRepository;
import com.AAA.e_commerce.user.model.Role;
import com.AAA.e_commerce.user.model.User;
import com.AAA.e_commerce.user.service.UserService;
import jakarta.transaction.Transactional;
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
    private final UserService userService;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = mapper.toProduct(requestDto);

        User currentUser = userService.getAuthenticatedUser();
        product.setVendor(currentUser);

        Category category = categoryService.getCategoryById(requestDto.categoryId());
        product.setCategory(category);

        Product savedProduct = repository.save(product);
        return mapper.toProductResponseDto(savedProduct);
    }

    public ProductResponseDto getProduct(Long id) {
        Product product = getProductById(id);
        return mapper.toProductResponseDto(product);
    }

    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage = repository.findAll(pageable);
        return productPage.map(mapper::toProductResponseDto);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        Product product = getProductById(id);
        validateProductOwnership(product);

        Category category = categoryService.getCategoryById(requestDto.categoryId());

        product.setName(requestDto.name());
        product.setPrice(requestDto.price());
        product.setWeight(requestDto.weight() != null ? requestDto.weight() : 0L);
        product.setDescription(requestDto.description());
        product.setQuantity(requestDto.quantity());
        product.setCategory(category);

        Product updatedProduct = repository.save(product);
        return mapper.toProductResponseDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        validateProductOwnership(product);

        repository.delete(product);
    }

    public Product getProductById(Long productId) {
        return repository
                .findById(productId)
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, "Product not found"));
    }

    public void saveProduct(Product product) {
        repository.save(product);
    }

    private void validateProductOwnership(Product product) {
        User currentUser = userService.getAuthenticatedUser();

        boolean isOwner =
                product.getVendor() != null
                        && product.getVendor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You do not have permission to modify or delete this product");
        }
    }
}