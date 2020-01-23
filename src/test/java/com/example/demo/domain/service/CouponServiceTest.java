package com.example.demo.domain.service;

import com.example.demo.domain.exception.BusinessException;
import com.example.demo.domain.model.entity.Coupon;
import com.example.demo.domain.model.enumtype.DiscountType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CouponServiceTest {

    private CouponService couponService;

    @Mock
    private DiscountCalculatorService discountCalculatorService;

    @Before
    public void setUp() {
        couponService = new CouponService(discountCalculatorService);
    }

    @Test
    public void should_get_applicable_coupon_amount_when_total_price_of_cart_is_bigger_than_min_purchase_price() {
        //given
        final double minPurchaseAmount = 50.0;
        final double couponPrice = 10.0;
        final BigDecimal totalPriceOfCart = BigDecimal.valueOf(100.0);

        Coupon coupon = new Coupon(minPurchaseAmount, couponPrice, DiscountType.AMOUNT);

        when(discountCalculatorService.calculateDiscountAmount(totalPriceOfCart, BigDecimal.valueOf(couponPrice), DiscountType.AMOUNT)).thenReturn(BigDecimal.TEN);

        //when
        BigDecimal applicableCouponAmount = couponService.getApplicableCouponAmount(coupon, totalPriceOfCart);

        //then
        assertThat(applicableCouponAmount).isNotNull();
        assertThat(applicableCouponAmount).isEqualTo(BigDecimal.TEN);

        verify(discountCalculatorService).calculateDiscountAmount(totalPriceOfCart, BigDecimal.valueOf(couponPrice), DiscountType.AMOUNT);
    }

    @Test
    public void should_throw_exception_when_coupon_price_is_less_than_total_price_of_cart() {
        //given
        final double minPurchaseAmount = 50.0;
        final double couponPrice = 10.0;
        final BigDecimal totalPriceOfCart = BigDecimal.TEN;

        Coupon coupon = new Coupon(minPurchaseAmount, couponPrice, DiscountType.AMOUNT);

        //when
        Throwable throwable = catchThrowable(() -> couponService.getApplicableCouponAmount(coupon, totalPriceOfCart));

        //then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(BusinessException.class).hasMessage("Shopping cart price for this coupon is: " + couponPrice);
    }

    @Test
    public void should_throw_exception_when_coupon_price_is_equal_to_total_price_of_cart() {
        //given
        final double minPurchaseAmount = 10.0;
        final double couponPrice = 10.0;
        final BigDecimal totalPriceOfCart = BigDecimal.TEN;

        Coupon coupon = new Coupon(minPurchaseAmount, couponPrice, DiscountType.AMOUNT);

        //when
        Throwable throwable = catchThrowable(() -> couponService.getApplicableCouponAmount(coupon, totalPriceOfCart));

        //then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(BusinessException.class).hasMessage("Shopping cart price for this coupon is: " + couponPrice);
    }
}