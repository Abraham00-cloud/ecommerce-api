package com.AAA.e_commerce.product.mapper;

import com.AAA.e_commerce.product.dto.request.CategoryRequestDto;
import com.AAA.e_commerce.product.dto.response.CategoryResponseDto;
import com.AAA.e_commerce.product.model.Category;
import com.AAA.e_commerce.product.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryMapper {
    private final CategoryRepository categoryRepository;

    public Category toCategory(CategoryRequestDto requestDto) {
        Category category = new Category();
        category.setName(requestDto.name());
        categoryRepository.save(category);
        return category;
    }

    public CategoryResponseDto toCategoryResponseDto(Category category) {
        CategoryResponseDto responseDto =
                new CategoryResponseDto(category.getId(), category.getName());
        return responseDto;
    }
}
