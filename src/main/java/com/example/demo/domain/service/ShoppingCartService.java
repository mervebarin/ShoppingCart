package com.example.demo.domain.service;

import com.example.demo.application.response.Response;
import com.example.demo.domain.exception.BusinessException;
import com.example.demo.domain.model.entity.Category;
import com.example.demo.domain.model.entity.Coupon;
import com.example.demo.domain.model.entity.Product;
import com.example.demo.domain.model.entity.ShoppingCart;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
public class ShoppingCartService {

    private static final double COST_PER_DELIVERY = 5.0;
    private static final double COST_PER_PRODUCT = 1.0;
    private static final double FIXED_COST = 2.99;

    private final ProductService productService;
    private final CouponService couponService;
    private final CampaignService campaignService;

    public ShoppingCartService(ProductService productService,
                               CouponService couponService,
                               CampaignService campaignService) {
        this.productService = productService;
        this.couponService = couponService;
        this.campaignService = campaignService;
    }

    public Response prepareShoppingCartForCheckout(HashMap<Long, Integer> productIdQuantityMap, Coupon coupon) {
        ShoppingCart shoppingCart = new ShoppingCart();
        productIdQuantityMap.forEach((productId, productQuantity) -> addItem(shoppingCart, productService.getProduct(productId), productQuantity));
        shoppingCart.setPrice(getInitialCartPrice(shoppingCart.getBasket()));
        applyDiscount(shoppingCart);
        applyCoupon(shoppingCart, coupon);
        DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculator(COST_PER_DELIVERY, COST_PER_PRODUCT, FIXED_COST);
        deliveryCostCalculator.calculate(shoppingCart);
        print(shoppingCart);
        Response response = new Response();
        response.setStatus("success");
        response.setTotalPriceOfShoppingCart(BigDecimal.valueOf(shoppingCart.getPrice()));
        return response;
    }

    public void addItem(ShoppingCart shoppingCart, Product product, Integer quantity) {
        HashMap<Product, Integer> shoppingCartBasket = shoppingCart.getBasket();
        shoppingCartBasket.put(product, quantity);
        shoppingCart.setBasket(shoppingCartBasket);
    }

    public void applyDiscount(ShoppingCart shoppingCart) {
        BigDecimal discountAmount = campaignService.getApplicableCampaignAmount(shoppingCart);
        shoppingCart.setCampaignDiscount(discountAmount.doubleValue());
        shoppingCart.setPrice(BigDecimal.valueOf(shoppingCart.getPrice()).subtract(discountAmount).doubleValue());
        shoppingCart.setCampaignDiscountApplied(true);
    }

    public void applyCoupon(ShoppingCart shoppingCart, Coupon coupon) {
        validateCampaignDiscountApplied(shoppingCart.isCampaignDiscountApplied());
        BigDecimal discountAmount = couponService.getApplicableCouponAmount(coupon, shoppingCart.getPrice());
        shoppingCart.setCouponDiscount(discountAmount.doubleValue());
        shoppingCart.setPrice(BigDecimal.valueOf(shoppingCart.getPrice()).subtract(discountAmount).doubleValue());
    }

    private void validateCampaignDiscountApplied(boolean isCampaignDiscountApplied) {
        if (!isCampaignDiscountApplied) {
            throw new BusinessException("Campaign discount should apply first.");
        }
    }

    private Double getInitialCartPrice(HashMap<Product, Integer> basket) {
        return basket
                .entrySet()
                .stream()
                .map(this::calculateProductPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
    }

    private BigDecimal calculateProductPrice(Map.Entry<Product, Integer> productBigDecimalEntry) {
        return BigDecimal.valueOf(productBigDecimalEntry.getKey().getPrice()).multiply(BigDecimal.valueOf(productBigDecimalEntry.getValue()));
    }

    public void print(ShoppingCart shoppingCart) {
        BigDecimal initialCartPrice = BigDecimal.valueOf(getInitialCartPrice(shoppingCart.getBasket()));
        BigDecimal campaignDiscountAmount = BigDecimal.valueOf(shoppingCart.getCampaignDiscount());
        BigDecimal couponDiscountAmount = BigDecimal.valueOf(shoppingCart.getCouponDiscount());

        Map<Category, List<Product>> groupedProductsByCategories = shoppingCart.getBasket().keySet().stream().collect(groupingBy(Product::getCategory));
        System.out.println("\n -------------ShoppingCart Details------------- \n");
        groupedProductsByCategories.forEach((category, products) -> products.forEach(product -> System.out.println(printProduct(product, shoppingCart.getBasket().get(product)))));

        System.out.println("TotalPriceOfCartBeforeDiscounts: " + initialCartPrice + ", TotalDiscountAmount: " + campaignDiscountAmount.add(couponDiscountAmount));
        System.out.println("TotalPrice: " + shoppingCart.getPrice() + ", DeliveryCost: " + shoppingCart.getDeliveryCost());
    }

    private String printProduct(Product product, Integer integer) {
        BigDecimal productUnitPrice = BigDecimal.valueOf(product.getPrice());
        BigDecimal productQuantity = BigDecimal.valueOf(integer);

        return product.toString()
                + ", TotalPriceOfProduct: "
                + productUnitPrice.multiply(productQuantity);
    }
}