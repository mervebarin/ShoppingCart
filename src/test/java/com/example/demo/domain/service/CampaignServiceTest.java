package com.example.demo.domain.service;

import com.example.demo.domain.model.entity.Campaign;
import com.example.demo.domain.model.entity.Category;
import com.example.demo.domain.model.entity.Product;
import com.example.demo.domain.model.entity.ShoppingCart;
import com.example.demo.domain.model.enumtype.DiscountType;
import com.example.demo.domain.repository.CampaignRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CampaignServiceTest {

    private CampaignService campaignService;

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private DiscountCalculatorService discountCalculatorService;

    @Before
    public void setUp() {
        campaignService = new CampaignService(campaignRepository, discountCalculatorService);
    }

    @Test
    public void should_get_applicable_campaign_amount() {
        //given
        Category foodCategory = new Category("food");
        Category clothCategory = new Category("cloth");

        Product apple = new Product("apple", 3.99, foodCategory);
        Product almond = new Product("almond", 12.5, foodCategory);
        Product shirt = new Product("shirt", 75.0, clothCategory);

        ShoppingCart shoppingCart = new ShoppingCart();
        HashMap<Product, Integer> basket = shoppingCart.getBasket();
        basket.put(apple, 3);
        basket.put(almond, 1);
        basket.put(shirt, 1);

        Campaign firstFoodCampaign = new Campaign(foodCategory, 10.0, BigDecimal.valueOf(2), DiscountType.RATE);
        Campaign secondFoodCampaign = new Campaign(foodCategory, 1.0, BigDecimal.valueOf(2), DiscountType.AMOUNT);
        Campaign firstClothCampaign = new Campaign(clothCategory, 5.0, BigDecimal.ONE, DiscountType.RATE);
        Campaign secondClothCampaign = new Campaign(clothCategory, 5.0, BigDecimal.ONE, DiscountType.AMOUNT);

        List<Campaign> campaignList = Arrays.asList(firstFoodCampaign, secondFoodCampaign, firstClothCampaign, secondClothCampaign);

        when(campaignRepository.findByCategory_TitleIn(any())).thenReturn(campaignList);
        when(discountCalculatorService.calculateDiscountAmount(24.47, 10, DiscountType.RATE)).thenReturn(BigDecimal.valueOf(2.447));
        when(discountCalculatorService.calculateDiscountAmount(24.47, 1, DiscountType.AMOUNT)).thenReturn(BigDecimal.ONE);
        when(discountCalculatorService.calculateDiscountAmount(75, 5, DiscountType.RATE)).thenReturn(BigDecimal.valueOf(3.75));
        when(discountCalculatorService.calculateDiscountAmount(75, 5, DiscountType.AMOUNT)).thenReturn(BigDecimal.valueOf(5));

        //when
        BigDecimal applicableCampaignAmount = campaignService.getApplicableCampaignAmount(shoppingCart);

        //then
        assertThat(applicableCampaignAmount).isEqualTo(BigDecimal.valueOf(7.447));
    }
}