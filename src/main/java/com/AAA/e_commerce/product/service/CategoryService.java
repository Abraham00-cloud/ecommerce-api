package com.AAA.e_commerce.product.service;

import com.AAA.e_commerce.product.dto.request.CategoryRequestDto;
import com.AAA.e_commerce.product.dto.response.CategoryResponseDto;
import com.AAA.e_commerce.product.mapper.CategoryMapper;
import com.AAA.e_commerce.product.model.Category;
import com.AAA.e_commerce.product.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryMapper mapper;
    private final CategoryRepository repository;

    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        Category category = mapper.toCategory(requestDto);
        return mapper.toCategoryResponseDto(category);
    }

    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        category.setName(requestDto.name());
        Category updatedCategory = repository.save(category);
        return mapper.toCategoryResponseDto(updatedCategory);
    }
    public CategoryResponseDto getCategory(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        return mapper.toCategoryResponseDto(category);
    }

    public List<CategoryResponseDto> getAllCategories() {
        List<CategoryResponseDto> allCategories = repository.findAll()
                .stream()
                .map(mapper::toCategoryResponseDto)
                .toList();
        return allCategories;
    }

    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }
}
