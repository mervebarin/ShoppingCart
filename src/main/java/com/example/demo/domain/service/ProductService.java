package com.example.demo.domain.service;

import com.example.demo.domain.exception.BusinessException;
import com.example.demo.domain.model.entity.Category;
import com.example.demo.domain.model.entity.Product;
import com.example.demo.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));
    }

    public List<String> getProductCategoryList(List<String> productCategoryList, Category category) {
        if (category != null) {
            productCategoryList.add(category.getTitle());
            getProductCategoryList(productCategoryList, category.getParentCategory());
        }
        return productCategoryList.stream().distinct().collect(Collectors.toList());
    }
}