package com.AAA.e_commerce.product.controller;

import com.AAA.e_commerce.product.dto.request.CategoryRequestDto;
import com.AAA.e_commerce.product.dto.response.CategoryResponseDto;
import com.AAA.e_commerce.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Category Api", description = "Endpoints for managing Product Category")
public class CategoryController {
    private final CategoryService service;

    @Operation(summary = "create Category")
    @PostMapping
    public CategoryResponseDto createCategory(@RequestBody @Valid CategoryRequestDto requestDto) {
        return service.createCategory(requestDto);
    }

    @Operation(summary = "get Category")
    @GetMapping("/{id}")
    public CategoryResponseDto getCategory(@PathVariable Long id) {
        return service.getCategory(id);
    }

    @Operation(summary = "get all Categories")
    @GetMapping
    public Page<CategoryResponseDto> getAllCategories(@ParameterObject Pageable pageable) {
        return service.getAllCategories(pageable);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update Category")
    public CategoryResponseDto updateCategory(
            @PathVariable Long id, @RequestBody @Valid CategoryRequestDto requestDto) {
        return service.updateCategory(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete Category")
    public void deleteCategory(@PathVariable Long id) {
        service.deleteCategory(id);
    }
}
