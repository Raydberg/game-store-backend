package com.gamestore.category.services.Impl;

import com.gamestore.category.mappers.CategoryMapper;
import com.gamestore.category.repository.CategoryRepository;
import com.gamestore.category.services.CategoryService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
}
