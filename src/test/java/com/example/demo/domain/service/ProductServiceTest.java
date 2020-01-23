package com.example.demo.domain.service;

import com.example.demo.domain.exception.BusinessException;
import com.example.demo.domain.model.entity.Product;
import com.example.demo.domain.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Before
    public void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    public void should_get_product() {
        //given
        final Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product()));

        //when
        Product product = productService.getProduct(productId);

        //then
        assertThat(product).isNotNull();
    }

    @Test
    public void should_throw_exception_when_product_not_found() {
        //given
        final Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //when
        Throwable throwable = catchThrowable(() -> productService.getProduct(productId));

        //then
        assertThat(throwable).isInstanceOf(BusinessException.class).hasMessage("Product not found with given id: " + productId);
    }
}