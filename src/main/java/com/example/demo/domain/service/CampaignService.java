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
    private final ProductService productService;
    private final DiscountCalculatorService discountCalculatorService;

    public CampaignService(CampaignRepository campaignRepository,
                           ProductService productService,
                           DiscountCalculatorService discountCalculatorService) {
        this.campaignRepository = campaignRepository;
        this.productService = productService;
        this.discountCalculatorService = discountCalculatorService;
    }

    public BigDecimal getApplicableCampaignAmount(ShoppingCart shoppingCart) {
        List<String> cartCategoryList = getShoppingCartCategoryList(shoppingCart.getBasket());
        List<Campaign> campaignList = campaignRepository.findByCategory_TitleIn(cartCategoryList);
        List<BigDecimal> discountPriceList = new ArrayList<>(Collections.emptyList());
        campaignList
                .stream()
                .filter(campaign -> isProductCountValidForCampaign(shoppingCart, campaign))
                .collect(groupingBy(Campaign::getCategory))
                .forEach((category, campaignListOfCategory) -> discountPriceList.add(findMaximumDiscountAmountForCategory(shoppingCart, campaignListOfCategory)));
        return discountPriceList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<String> getShoppingCartCategoryList(HashMap<Product, Integer> basket) {
        List<String> shoppingCartCategoryList = new ArrayList<>(Collections.emptyList());
        basket.keySet().forEach(product -> shoppingCartCategoryList.addAll(productService.getProductCategoryList(shoppingCartCategoryList, product.getCategory())));
        return shoppingCartCategoryList;
    }

    private boolean isProductCountValidForCampaign(ShoppingCart shoppingCart, Campaign campaign) {
        return BigDecimal.valueOf(getProductCountWithCategory(shoppingCart.getBasket(), campaign.getCategory()))
                .compareTo(campaign.getMinNumberOfProductsRequired()) >= 0;
    }

    private Integer getProductCountWithCategory(HashMap<Product, Integer> basket, Category category) {
        return basket
                .keySet()
                .stream()
                .filter(product -> categoryFilter(category, product))
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
        BigDecimal campaignAmount = BigDecimal.valueOf(campaign.getCampaignPrice());
        BigDecimal totalProductPriceWithinCategory = getTotalProductPriceWithinCategory(shoppingCart.getBasket(), campaign.getCategory());
        return discountCalculatorService.calculateDiscountAmount(totalProductPriceWithinCategory, campaignAmount, campaign.getDiscountType());
    }

    private BigDecimal getTotalProductPriceWithinCategory(HashMap<Product, Integer> basket, Category category) {
        return basket
                .keySet()
                .stream()
                .filter(product -> categoryFilter(category, product))
                .map(product -> BigDecimal.valueOf(product.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean categoryFilter(Category category, Product product) {
        return productService.getProductCategoryList(new ArrayList<>(Collections.emptyList()), product.getCategory())
                .stream()
                .anyMatch(productCategoryTitle -> productCategoryTitle.equals(category.getTitle()));
    }
}