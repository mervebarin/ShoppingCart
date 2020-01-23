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

        Campaign foodCampaign = new Campaign(foodCategory, 10.0, BigDecimal.valueOf(2), DiscountType.RATE);
        Campaign clothCampaign = new Campaign(clothCategory, 5.0, BigDecimal.ONE, DiscountType.RATE);

        List<Campaign> campaignList = Arrays.asList(clothCampaign, foodCampaign);

        when(campaignRepository.findByCategory_TitleIn(any())).thenReturn(campaignList);
        when(discountCalculatorService.calculateDiscountAmount(any(), any(), any())).thenReturn(BigDecimal.valueOf(2.447));

        //when
        BigDecimal applicableCampaignAmount = campaignService.getApplicableCampaignAmount(shoppingCart);

        //then
        assertThat(applicableCampaignAmount).isEqualTo(BigDecimal.valueOf(4.894));
    }
}