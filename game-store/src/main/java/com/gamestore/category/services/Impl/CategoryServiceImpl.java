package com.gamestore.category.services.Impl;

import com.gamestore.category.DTOs.CategoryPageResponseDto;
import com.gamestore.category.DTOs.CategoryRequestDto;
import com.gamestore.category.DTOs.CategoryResponseDto;
import com.gamestore.category.mappers.CategoryMapper;
import com.gamestore.category.model.Category;
import com.gamestore.category.repository.CategoryRepository;
import com.gamestore.category.services.CategoryService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public CategoryPageResponseDto findAllCategory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<CategoryResponseDto> categoryDto = categoryPage
                .stream()
                .map(categoryMapper::toCategoryResponseDto)
                .collect(Collectors.toList());

        return new CategoryPageResponseDto(
                categoryDto, categoryPage.getTotalPages(), categoryPage.getTotalElements()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto findCategoryById(Long id) {
        Category categoryExist = findExistOrThrow(id);
        return categoryMapper.toCategoryResponseDto(categoryExist);
    }

    @Override
    @Transactional
    public CategoryResponseDto saveCategory(CategoryRequestDto dto) {
        Category category = categoryMapper.toCategoryEntity(dto);
        categoryRepository.saveAndFlush(category);
        return categoryMapper.toCategoryResponseDto(category);
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto) {
        Category categoryExist = findExistOrThrow(id);
        categoryMapper.updateCategoryFromDto(dto, categoryExist);
        Category categoryUpdate = categoryRepository.saveAndFlush(categoryExist);
        return categoryMapper.toCategoryResponseDto(categoryUpdate);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        var category = findCategoryById(id);
        categoryRepository.deleteById(id);
    }

    private Category findExistOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría con id " + id + " no encontrada"));
    }

}
