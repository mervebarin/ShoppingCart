package com.example.demo.domain.service;

import com.example.demo.domain.model.entity.Product;
import com.example.demo.domain.model.entity.ShoppingCart;

import java.math.BigDecimal;
import java.util.HashMap;

public class DeliveryCostCalculator {

    private final double costPerDelivery;
    private final double costPerProduct;
    private final double fixedCost;

    public DeliveryCostCalculator(Double costPerDelivery, Double costPerProduct, double fixedCost) {
        this.costPerDelivery = costPerDelivery;
        this.costPerProduct = costPerProduct;
        this.fixedCost = fixedCost;
    }

    public BigDecimal calculate(ShoppingCart shoppingCart) {
        BigDecimal numberOfDeliveries = getDistinctCategoryAmount(shoppingCart.getBasket());
        BigDecimal numberOfProducts = getDistinctProductAmount(shoppingCart.getBasket());
        BigDecimal deliveryCost = BigDecimal.valueOf(costPerDelivery)
                .multiply(numberOfDeliveries)
                .add(BigDecimal.valueOf(costPerProduct)
                        .multiply(numberOfProducts))
                .add(BigDecimal.valueOf(fixedCost));
        shoppingCart.setDeliveryCost(deliveryCost.doubleValue());
        return deliveryCost;
    }

    private BigDecimal getDistinctCategoryAmount(HashMap<Product, Integer> basket) {
        return BigDecimal.valueOf(basket
                .keySet()
                .stream()
                .map(Product::getCategory)
                .distinct()
                .count());
    }

    private BigDecimal getDistinctProductAmount(HashMap<Product, Integer> basket) {
        return BigDecimal.valueOf(basket
                .keySet()
                .stream()
                .map(Product::getTitle)
                .distinct()
                .count());
    }
}