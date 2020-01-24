package com.example.demo.domain.service;

import com.example.demo.domain.exception.BusinessException;
import com.example.demo.domain.model.entity.Coupon;
import com.example.demo.domain.model.enumtype.DiscountType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CouponService {

    private final DiscountCalculatorService discountCalculatorService;

    public CouponService(DiscountCalculatorService discountCalculatorService) {
        this.discountCalculatorService = discountCalculatorService;
    }

    public BigDecimal getApplicableCouponAmount(Coupon coupon, Double totalPriceOfCart) {
        DiscountType discountType = coupon.getDiscountType();
        Double couponPrice = coupon.getCouponPrice();

        validateCouponApplicable(BigDecimal.valueOf(totalPriceOfCart), BigDecimal.valueOf(coupon.getMinimumPurchasePrice()), BigDecimal.valueOf(couponPrice));
        return discountCalculatorService.calculateDiscountAmount(totalPriceOfCart, couponPrice, discountType);
    }

    private void validateCouponApplicable(BigDecimal totalPriceOfCart, BigDecimal minimumPurchasePrice, BigDecimal couponPrice) {
        if (totalPriceOfCart.compareTo(minimumPurchasePrice) <= 0) {
            throw new BusinessException("Shopping cart price for this coupon is: " + couponPrice);
        }
    }
}