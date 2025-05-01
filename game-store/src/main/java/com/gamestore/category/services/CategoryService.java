package com.gamestore.category.services;

import com.gamestore.category.DTOs.CategoryPageResponseDto;
import com.gamestore.category.DTOs.CategoryRequestDto;
import com.gamestore.category.DTOs.CategoryResponseDto;

public interface CategoryService {
    CategoryPageResponseDto findAllCategory(int page, int size);

    CategoryResponseDto findCategoryById(Long id);

    CategoryResponseDto saveCategory(CategoryRequestDto dto);

    CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto);

    void deleteCategory(Long id);
}
