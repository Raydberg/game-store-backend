package com.gamestore.product.controllers;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.gamestore.product.DTOs.ProductPageResponseDto;
import com.gamestore.product.DTOs.ProductRequestDto;
import com.gamestore.product.DTOs.ProductResponseDto;
import com.gamestore.product.services.ProductService;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<ProductPageResponseDto> getAllProducts(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "La página debe ser 0 o mayor") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "El tamaño debe ser al menos 1") int size) {
        var response = productService.findAllProduct(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        var response = productService.findProductById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto dto) {
        var response = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductRequestDto dto) {
        var response = productService.updateProduct(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category")
    public ResponseEntity<ProductPageResponseDto> getAllProductsWithCategory(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "La página debe ser 0 o mayor") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "El tamaño debe ser al menos 1") int size) {

        ProductPageResponseDto response;
        if (categoryId != null) {
            response = productService.findProductsByCategory(categoryId, page, size);
        } else {
            response = productService.findAllProduct(page, size);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<ProductPageResponseDto> getAllProductsWithFilters(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "La página debe ser 0 o mayor") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "El tamaño debe ser al menos 1") int size) {

        ProductPageResponseDto response = productService.findProductsWithFilter(
                page, size, categoryId, minPrice, maxPrice, sortBy, sortDirection);

        return ResponseEntity.ok(response);
    }
}
