package com.gamestore.category.services;

import com.gamestore.category.DTOs.CategoryPageResponseDto;
import com.gamestore.category.DTOs.CategoryRequestDto;
import com.gamestore.category.DTOs.CategoryResponseDto;

public interface CategoryService {
    CategoryPageResponseDto findAllActiveCategories(int page, int size);
    
    CategoryPageResponseDto findAllCategories(int page, int size);

    CategoryResponseDto findCategoryById(Long id);

    CategoryResponseDto saveCategory(CategoryRequestDto dto);

    CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto);

    void deleteCategory(Long id);
    
    CategoryResponseDto toggleCategoryStatus(Long id, boolean active);
}
