package com.AAA.e_commerce.product.controller;

import com.AAA.e_commerce.product.dto.request.CategoryRequestDto;
import com.AAA.e_commerce.product.dto.response.CategoryResponseDto;
import com.AAA.e_commerce.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@AllArgsConstructor
@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Api",description = "Endpoints for managing Product Category")
public class CategoryController {
    private final CategoryService service;

    @Operation(summary = "create Category")
    @PostMapping
    public CategoryResponseDto createCategory(@RequestBody CategoryRequestDto requestDto) {
        return service.createCategory(requestDto);
    }

    @Operation(summary = "get Category")
    @GetMapping("/{id}")
    public CategoryResponseDto getCategory(@PathVariable Long id) {
        return service.getCategory(id);
    }

    @Operation(summary = "get all Categories")
    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        return service.getAllCategories();
    }

    @PutMapping("/{id}")
    @Operation(summary = "update Category")
    public CategoryResponseDto updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDto requestDto) {
        return service.updateCategory(id, requestDto);
    }

    @DeleteMapping
    @Operation(summary = "delete Category")
    public void  deleteCategory(Long id) {
        service.deleteCategory(id);
    }
}
