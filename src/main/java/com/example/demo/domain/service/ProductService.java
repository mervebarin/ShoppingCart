package com.example.demo.domain.service;

import com.example.demo.domain.exception.BusinessException;
import com.example.demo.domain.model.entity.Product;
import com.example.demo.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found with given id: " + id));
    }
}