package com.gamestore.category.mappers;

import com.gamestore.category.DTOs.CategoryRequestDto;
import com.gamestore.category.DTOs.CategoryResponseDto;
import com.gamestore.category.model.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    
    @Mapping(target = "active", source = "active")
    CategoryResponseDto toCategoryResponseDto(Category category);
    CategoryRequestDto toCategoryRequestDto(Category  category);
    Category toCategoryEntity(CategoryRequestDto categoryRequestDto);
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    void updateCategoryFromDto(CategoryRequestDto dtp, @MappingTarget Category category);
}
