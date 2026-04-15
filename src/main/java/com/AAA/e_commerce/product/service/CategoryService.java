package com.AAA.e_commerce.product.service;

import com.AAA.e_commerce.product.dto.request.CategoryRequestDto;
import com.AAA.e_commerce.product.dto.response.CategoryResponseDto;
import com.AAA.e_commerce.product.mapper.CategoryMapper;
import com.AAA.e_commerce.product.model.Category;
import com.AAA.e_commerce.product.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryMapper mapper;
    private final CategoryRepository categoryRepository;

    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        Category category = mapper.toCategory(requestDto);
        return mapper.toCategoryResponseDto(category);
    }

    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto) {
        Category category =
                categoryRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Category not found"));
        category.setName(requestDto.name());
        Category updatedCategory = categoryRepository.save(category);
        return mapper.toCategoryResponseDto(updatedCategory);
    }

    public CategoryResponseDto getCategory(Long id) {
        Category category =
                categoryRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Category not found"));
        return mapper.toCategoryResponseDto(category);
    }

    public Page<CategoryResponseDto> getAllCategories(Pageable pageable) {
        Page<Category> allCategories = categoryRepository.findAll(pageable);
        return allCategories.map(mapper::toCategoryResponseDto);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository
                .findById(categoryId)
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, "Category not found"));
    }
}
