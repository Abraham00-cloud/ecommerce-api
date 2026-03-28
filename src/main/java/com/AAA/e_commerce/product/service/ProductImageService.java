package com.AAA.e_commerce.product.service;

import com.AAA.e_commerce.product.dto.response.ProductImageResponseDto;
import com.AAA.e_commerce.product.mapper.ProductImageMapper;
import com.AAA.e_commerce.product.model.Product;
import com.AAA.e_commerce.product.model.ProductImage;
import com.AAA.e_commerce.product.repository.ProductImageRepository;
import com.AAA.e_commerce.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class ProductImageService {
    private final ProductImageRepository repository;
    private final ProductRepository productRepository;
    private final ProductImageMapper mapper;

    public ProductImageResponseDto addImages(Long productId, String url) {
        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Product not found"));
        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setUrl(url);
        String filename = url.substring(url.lastIndexOf("/") + 1);
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);

        image.setFileName(filename);
        image.setFileType(fileType);
        product.getImages().add(image);

        repository.save(image);
        productRepository.save(product);

        return mapper.toProductImageResponseDto(image);
    }

    public List<ProductImageResponseDto> getImagesByProduct(Long productId) {
        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Product not found"));
        List<ProductImageResponseDto> images =
                product.getImages().stream().map(mapper::toProductImageResponseDto).toList();
        return images;
    }

    @Transactional
    public void deleteImage(Long imageId) {
        ProductImage image =
                repository
                        .findById(imageId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Image not found"));
        image.getProduct().getImages().remove(image);
        repository.delete(image);
    }
}
