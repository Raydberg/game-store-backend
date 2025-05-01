package com.gamestore.product.services.Impl;

import com.gamestore.product.mappers.ProductMapper;
import com.gamestore.product.repository.ProductRepository;
import com.gamestore.product.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
}
