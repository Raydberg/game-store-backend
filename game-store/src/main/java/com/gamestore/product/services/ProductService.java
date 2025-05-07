package com.gamestore.product.services;

import com.gamestore.product.DTOs.ProductPageResponseDto;
import com.gamestore.product.DTOs.ProductRequestDto;
import com.gamestore.product.DTOs.ProductResponseDto;

import java.math.BigDecimal;

public interface ProductService {
    ProductPageResponseDto findAllProduct(int page, int size);
    
    ProductPageResponseDto findProductsByCategory(Long categoryId, int page, int size);
    
    ProductPageResponseDto findProductsWithFilter(int page, int size, Long categoryId, 
        BigDecimal minPrice, BigDecimal maxPrice, String sortBy, String sortDirection);

    ProductPageResponseDto findProductsWithFilterAdmin(int page, int size, Long categoryId, 
        BigDecimal minPrice, BigDecimal maxPrice, String sortBy, String sortDirection);

    ProductResponseDto findProductById(Long id);

    ProductResponseDto updateProduct(Long id, ProductRequestDto dto);

    ProductResponseDto createProduct(ProductRequestDto dto);

    void deleteProduct(Long id);
    ProductResponseDto toggleProductStatus(Long id, boolean active);
}