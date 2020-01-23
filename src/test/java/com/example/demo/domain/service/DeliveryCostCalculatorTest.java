package com.example.demo.domain.service;

import com.example.demo.domain.model.entity.Category;
import com.example.demo.domain.model.entity.Product;
import com.example.demo.domain.model.entity.ShoppingCart;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DeliveryCostCalculatorTest {

    private static final Double COST_PER_DELIVERY = 5.99;
    private static final Double COST_PER_PRODUCT = 0.99;
    private static final double FIXED_COST = 2.99;

    private DeliveryCostCalculator deliveryCostCalculator;

    @Before
    public void setUp() {
        deliveryCostCalculator = new DeliveryCostCalculator(COST_PER_DELIVERY, COST_PER_PRODUCT, FIXED_COST);
    }

    @Test
    public void should_calculate() {
        //given
        Category food = new Category("food");
        Category cloth = new Category("cloth");

        Product apple = new Product("apple", 3.99, food);
        Product almond = new Product("almond", 12.5, food);
        Product shirt = new Product("shirt", 75.0, cloth);

        ShoppingCart shoppingCart = new ShoppingCart();
        HashMap<Product, Integer> basket = shoppingCart.getBasket();
        basket.put(apple, 3);
        basket.put(almond, 1);
        basket.put(shirt, 1);

        //when
        BigDecimal deliveryCost = deliveryCostCalculator.calculate(shoppingCart);

        //then
        assertThat(deliveryCost).isNotNull();
        assertThat(deliveryCost).isEqualTo(BigDecimal.valueOf(17.94));
    }
}