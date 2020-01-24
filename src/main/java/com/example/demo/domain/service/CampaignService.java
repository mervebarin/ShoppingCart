package com.example.demo.domain.service;

import com.example.demo.domain.model.entity.Campaign;
import com.example.demo.domain.model.entity.Category;
import com.example.demo.domain.model.entity.Product;
import com.example.demo.domain.model.entity.ShoppingCart;
import com.example.demo.domain.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final DiscountCalculatorService discountCalculatorService;

    public CampaignService(CampaignRepository campaignRepository,
                           DiscountCalculatorService discountCalculatorService) {
        this.campaignRepository = campaignRepository;
        this.discountCalculatorService = discountCalculatorService;
    }

    public BigDecimal getApplicableCampaignAmount(ShoppingCart shoppingCart) {
        List<String> cartCategoryList = getBasketCategoryList(shoppingCart.getBasket());
        List<Campaign> campaignList = retrieveCampaignListByCategory(cartCategoryList);
        List<BigDecimal> applicableDiscountPriceList = new ArrayList<>(Collections.emptyList());
        campaignList
                .stream()
                .filter(campaign -> isProductCountValidForCampaign(shoppingCart, campaign))
                .collect(groupingBy(Campaign::getCategory))
                .forEach((category, campaignListOfCategory) ->
                        applicableDiscountPriceList.add(findMaximumDiscountAmountForCategory(shoppingCart, campaignListOfCategory)));
        return applicableDiscountPriceList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<String> getBasketCategoryList(HashMap<Product, Integer> basket) {
        List<String> basketCategoryList = new ArrayList<>(Collections.emptyList());
        basket.keySet().forEach(product -> {
            String categoryTitle = product.getCategory().getTitle();
            basketCategoryList.add(categoryTitle);
        });
        return basketCategoryList.stream().distinct().collect(Collectors.toList());
    }

    private List<Campaign> retrieveCampaignListByCategory(List<String> cartCategoryList) {
        return campaignRepository.findByCategory_TitleIn(cartCategoryList);
    }

    private boolean isProductCountValidForCampaign(ShoppingCart shoppingCart, Campaign campaign) {
        return BigDecimal.valueOf(getProductCountWithCategory(shoppingCart.getBasket(), campaign.getCategory()))
                .compareTo(campaign.getMinNumberOfProductsRequired()) >= 0;
    }

    private Integer getProductCountWithCategory(HashMap<Product, Integer> basket, Category category) {
        return basket
                .keySet()
                .stream()
                .filter(product -> category.getTitle().equals(product.getCategory().getTitle()))
                .map(basket::get)
                .reduce(0, Integer::sum);
    }

    private BigDecimal findMaximumDiscountAmountForCategory(ShoppingCart shoppingCart, List<Campaign> campaignListOfCategory) {
        List<BigDecimal> campaignDiscountAmountListWithinCategory = campaignListOfCategory
                .stream()
                .map(campaign -> getCampaignAmountForShoppingCart(shoppingCart, campaign))
                .collect(Collectors.toList());
        return Collections.max(campaignDiscountAmountListWithinCategory);
    }

    private BigDecimal getCampaignAmountForShoppingCart(ShoppingCart shoppingCart, Campaign campaign) {
        double totalProductPriceWithinCategory = getTotalProductPriceWithinCategory(shoppingCart.getBasket(), campaign.getCategory());
        return discountCalculatorService.calculateDiscountAmount(totalProductPriceWithinCategory, campaign.getCampaignPrice(), campaign.getDiscountType());
    }

    private double getTotalProductPriceWithinCategory(HashMap<Product, Integer> basket, Category category) {
        return basket
                .keySet()
                .stream()
                .filter(product -> category.getTitle().equals(product.getCategory().getTitle()))
                .map(product -> {
                    BigDecimal productUnitPrice = BigDecimal.valueOf(product.getPrice());
                    BigDecimal productQuantity = BigDecimal.valueOf(basket.get(product));
                    return productUnitPrice.multiply(productQuantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
    }
}