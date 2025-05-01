package com.gamestore.product.services.Impl;

import com.gamestore.category.model.Category;
import com.gamestore.category.repository.CategoryRepository;
import com.gamestore.product.DTOs.ProductPageResponseDto;
import com.gamestore.product.DTOs.ProductRequestDto;
import com.gamestore.product.DTOs.ProductResponseDto;
import com.gamestore.product.mappers.ProductMapper;
import com.gamestore.product.model.Product;
import com.gamestore.product.repository.ProductRepository;
import com.gamestore.product.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public ProductPageResponseDto findAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductResponseDto> productsDto = productPage
                .stream()
                .map(productMapper::toProductResponseDto)
                .collect(Collectors.toList());
        return new ProductPageResponseDto(productsDto, page, productPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto findProductById(Long id) {
        var product = findExistOrThrow(id);
        return productMapper.toProductResponseDto(product);
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto) {
        var product = findExistOrThrow(id);
        productMapper.updateProductFromDto(dto, product);
        if (dto.categoryId() != null) {
            var category = categoryRepository.findById(dto.categoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        var updatedProduct = productRepository.save(product);
        return productMapper.toProductResponseDto(updatedProduct);
    }

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Category categories = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = productMapper.toProductEntity(dto);
        product.setCategory(categories);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponseDto(savedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        var product = findExistOrThrow(id);
        productRepository.delete(product);
    }

    private Product findExistOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductPageResponseDto findProductsByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;

        if (categoryId != null) {
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        List<ProductResponseDto> productsDto = productPage
                .stream()
                .map(productMapper::toProductResponseDto)
                .collect(Collectors.toList());

        return new ProductPageResponseDto(productsDto, page, productPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductPageResponseDto findProductsWithFilter(int page, int size, Long categoryId,
            BigDecimal minPrice, BigDecimal maxPrice, String sortBy, String sortDirection) {

        Pageable pageable = createPageableWithSort(page, size, sortBy, sortDirection);

        // Usar Specification para filtros dinámicos
        Specification<Product> spec = Specification.where(null);

        if (categoryId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId));
        }

        if (minPrice != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        List<ProductResponseDto> productsDto = productPage.stream()
                .map(productMapper::toProductResponseDto)
                .collect(Collectors.toList());

        return new ProductPageResponseDto(productsDto, page, productPage.getTotalElements());
    }

    private Pageable createPageableWithSort(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy != null ? sortBy : "id");
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        return PageRequest.of(page, size, sort);
    }

}
