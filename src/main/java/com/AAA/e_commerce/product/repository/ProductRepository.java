package com.AAA.e_commerce.product.repository;

import com.AAA.e_commerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    
}
