package com.example.demo.domain.service;

import com.example.demo.domain.model.enumtype.DiscountType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DiscountCalculatorService {

    public BigDecimal calculateDiscountAmount(BigDecimal price, BigDecimal discountAmount, DiscountType discountType) {
        if (DiscountType.AMOUNT.equals(discountType) && isDiscountAmountValid(price, discountAmount)) {
            return discountAmount;
        }

        if (DiscountType.RATE.equals(discountType)) {
            return price.multiply(discountAmount).divide(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    private boolean isDiscountAmountValid(BigDecimal price, BigDecimal discountAmount) {
        return discountAmount.compareTo(price) < 0;
    }
}