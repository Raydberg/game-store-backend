package com.gamestore.category.controllers;

import com.gamestore.category.DTOs.CategoryPageResponseDto;
import com.gamestore.category.DTOs.CategoryRequestDto;
import com.gamestore.category.DTOs.CategoryResponseDto;
import com.gamestore.category.services.CategoryService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<CategoryPageResponseDto> getAllCartShop(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "La página debe ser 0 o mayor") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "El tamaño debe ser al menos 1") int size
    ) {
        var response = categoryService.findAllCategory(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryResponseDto> getCartById(@PathVariable Long id) {
        var response = categoryService.findCategoryById(id);
        return ResponseEntity.ok(response);
    }


    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDto> create(@RequestBody CategoryRequestDto dto) {
        var response = categoryService.saveCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable Long id, @RequestBody CategoryRequestDto dto) {
        var result = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
