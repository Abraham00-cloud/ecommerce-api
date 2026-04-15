package com.AAA.e_commerce.product.service;

import com.AAA.e_commerce.product.dto.response.ProductImageResponseDto;
import com.AAA.e_commerce.product.mapper.ProductImageMapper;
import com.AAA.e_commerce.product.model.Product;
import com.AAA.e_commerce.product.model.ProductImage;
import com.AAA.e_commerce.product.repository.ProductImageRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class ProductImageService {
    private final ProductImageRepository imageRepository;
    private final ProductImageMapper mapper;
    private final ProductService productService;

    public ProductImageResponseDto addImages(Long productId, String url) {
        Product product = productService.getProductById(productId);
        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setUrl(url);
        String filename = url.substring(url.lastIndexOf("/") + 1);
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);

        image.setFileName(filename);
        image.setFileType(fileType);
        product.getImages().add(image);

        imageRepository.save(image);
        productService.saveProduct(product);

        return mapper.toProductImageResponseDto(image);
    }

    public Page<ProductImageResponseDto> getImagesByProduct(Long productId, Pageable pageable) {
        Product product = productService.getProductById(productId);
        Page<ProductImage> images = imageRepository.findByProductId(productId, pageable);
        return images.map(mapper::toProductImageResponseDto);
    }

    @Transactional
    public void deleteImage(Long imageId) {
        ProductImage image =
                imageRepository
                        .findById(imageId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Image not found"));
        image.getProduct().getImages().remove(image);
        imageRepository.delete(image);
    }
}
