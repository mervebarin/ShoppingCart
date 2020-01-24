package com.example.demo.domain.service;

import com.example.demo.domain.model.enumtype.DiscountType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DiscountCalculatorServiceTest {

    private DiscountCalculatorService discountCalculatorService;

    @Before
    public void setUp() {
        discountCalculatorService = new DiscountCalculatorService();
    }

    @Test
    public void should_calculate_discount_for_discount_type_rate() {
        //given
        final double price = 15;
        final double discountAmount = 3;

        //when
        BigDecimal calculatedDiscountAmount = discountCalculatorService.calculateDiscountAmount(price, discountAmount, DiscountType.RATE);

        //then
        assertThat(calculatedDiscountAmount).isNotNull();
        assertThat(calculatedDiscountAmount).isEqualTo(BigDecimal.valueOf(0.45));
    }

    @Test
    public void should_calculate_discount_for_discount_type_amount() {
        //given
        final double price = 15;
        final double discountAmount = 3;

        //when
        BigDecimal calculatedDiscountAmount = discountCalculatorService.calculateDiscountAmount(price, discountAmount, DiscountType.AMOUNT);

        //then
        assertThat(calculatedDiscountAmount).isNotNull();
        assertThat(calculatedDiscountAmount).isEqualTo(BigDecimal.valueOf(3.0));
    }

    @Test
    public void should_return_zero_when_discount_type_unknown() {
        //given
        final double price = 15;
        final double discountAmount = 3;

        //when
        BigDecimal calculatedDiscountAmount = discountCalculatorService.calculateDiscountAmount(price, discountAmount, null);

        //then
        assertThat(calculatedDiscountAmount).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void should_return_zero_when_discount_amount_is_bigger_than_price() {
        //given
        final double price = 15;
        final double discountAmount = 20;

        //when
        BigDecimal calculatedDiscountAmount = discountCalculatorService.calculateDiscountAmount(price, discountAmount, null);

        //then
        assertThat(calculatedDiscountAmount).isEqualTo(BigDecimal.ZERO);
    }
}