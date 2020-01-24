package com.example.demo.domain.service;

import com.example.demo.domain.model.enumtype.DiscountType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DiscountCalculatorService {

    public BigDecimal calculateDiscountAmount(double price, double discountAmount, DiscountType discountType) {
        if (DiscountType.AMOUNT.equals(discountType) && isDiscountAmountValid(BigDecimal.valueOf(price), BigDecimal.valueOf(discountAmount))) {
            return BigDecimal.valueOf(discountAmount);
        }

        if (DiscountType.RATE.equals(discountType)) {
            return BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(discountAmount)).divide(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    private boolean isDiscountAmountValid(BigDecimal price, BigDecimal discountAmount) {
        return discountAmount.compareTo(price) < 0;
    }
}