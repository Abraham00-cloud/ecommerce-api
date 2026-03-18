package com.AAA.e_commerce.product.repository;

import com.AAA.e_commerce.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
