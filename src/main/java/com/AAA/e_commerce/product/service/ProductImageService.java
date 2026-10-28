package com.AAA.e_commerce.product.service;

import com.AAA.e_commerce.cloudinary.service.CloudinaryService;
import com.AAA.e_commerce.product.dto.response.ProductImageResponseDto;
import com.AAA.e_commerce.product.mapper.ProductImageMapper;
import com.AAA.e_commerce.product.model.Product;
import com.AAA.e_commerce.product.model.ProductImage;
import com.AAA.e_commerce.product.repository.ProductImageRepository;
import com.AAA.e_commerce.user.model.Role;
import com.AAA.e_commerce.user.model.User;
import com.AAA.e_commerce.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile; // Imported MultipartFile
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class ProductImageService {
    private final ProductImageRepository imageRepository;
    private final ProductImageMapper mapper;
    private final ProductService productService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public ProductImageResponseDto addImages(Long productId, MultipartFile file) {
        Product product = productService.getProductById(productId);
        validateProductOwnership(product);

        String url = cloudinaryService.uploadImage(file, "shopease/products");

        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setUrl(url);

        String originalFilename = file.getOriginalFilename();
        String filename = (originalFilename != null && !originalFilename.isBlank())
                ? originalFilename
                : url.substring(url.lastIndexOf("/") + 1);

        String fileType = file.getContentType() != null
                ? file.getContentType()
                : "unknown";

        image.setFileName(filename);
        image.setFileType(fileType);
        product.getImages().add(image);

        ProductImage savedImage = imageRepository.save(image);
        productService.saveProduct(product);

        return mapper.toProductImageResponseDto(savedImage);
    }

    public Page<ProductImageResponseDto> getImagesByProduct(Long productId, Pageable pageable) {
        productService.getProductById(productId);
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

        validateProductOwnership(image.getProduct());

        image.getProduct().getImages().remove(image);
        imageRepository.delete(image);
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
                    "You do not have permission to manage images for this product");
        }
    }
}