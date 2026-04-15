package com.AAA.e_commerce.product.repository;

import com.AAA.e_commerce.product.model.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Page<ProductImage> findByProductId(Long productId, Pageable pageable);
}
