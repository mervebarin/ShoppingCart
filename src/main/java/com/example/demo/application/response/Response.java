package com.example.demo.application.response;

import java.math.BigDecimal;

public class Response {
    private String status;
    private BigDecimal totalPriceOfShoppingCart;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalPriceOfShoppingCart() {
        return totalPriceOfShoppingCart;
    }

    public void setTotalPriceOfShoppingCart(BigDecimal totalPriceOfShoppingCart) {
        this.totalPriceOfShoppingCart = totalPriceOfShoppingCart;
    }
}