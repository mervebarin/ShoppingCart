package com.example.demo.domain.service;

import com.example.demo.application.response.Response;
import com.example.demo.domain.exception.BusinessException;
import com.example.demo.domain.model.entity.Category;
import com.example.demo.domain.model.entity.Coupon;
import com.example.demo.domain.model.entity.Product;
import com.example.demo.domain.model.entity.ShoppingCart;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartServiceTest {
    private ShoppingCartService shoppingCartService;

    @Mock
    private ProductService productService;

    @Mock
    private CouponService couponService;

    @Mock
    private CampaignService campaignService;

    @Before
    public void setUp() {
        shoppingCartService = new ShoppingCartService(
                productService,
                couponService,
                campaignService
        );
    }

    @Test
    public void should_prepare_shopping_cart_for_checkout() {
        //given
        HashMap<Long, Integer> productIdQuantityMap = new HashMap<>();
        productIdQuantityMap.put(1L, 5);
        productIdQuantityMap.put(2L, 1);

        Coupon coupon = new Coupon();
        Category food = new Category("food");

        Product apple = new Product("apple", 3.0, food);
        apple.setId(1L);

        Product almond = new Product("almond", 15.0, food);
        almond.setId(2L);

        when(productService.getProduct(1L)).thenReturn(apple);
        when(productService.getProduct(2L)).thenReturn(almond);
        when(campaignService.getApplicableCampaignAmount(any())).thenReturn(BigDecimal.TEN);
        when(couponService.getApplicableCouponAmount(any(Coupon.class), any())).thenReturn(BigDecimal.ONE);

        //when
        Response response = shoppingCartService.prepareShoppingCartForCheckout(productIdQuantityMap, coupon);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo("success");
    }

    @Test
    public void should_throw_exception_when_apply_coupon_calls_before_campaign_discount() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCampaignDiscountApplied(false);

        //when
        Throwable throwable = catchThrowable(() -> shoppingCartService.applyCoupon(shoppingCart, new Coupon()));

        //then
        assertThat(throwable).isNotNull().isInstanceOf(BusinessException.class).hasMessage("Campaign discount should apply first.");
    }
}