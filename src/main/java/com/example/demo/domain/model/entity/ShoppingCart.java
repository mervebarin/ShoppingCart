package com.example.demo.domain.model.entity;

import java.util.HashMap;

public class ShoppingCart {

    private HashMap<Product, Integer> basket;
    private Double price;
    private Double couponDiscount;
    private Double campaignDiscount;
    private Double deliveryCost;
    private boolean campaignDiscountApplied;

    public ShoppingCart() {
        basket = new HashMap<>();
    }

    public HashMap<Product, Integer> getBasket() {
        return basket;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setBasket(HashMap<Product, Integer> basket) {
        this.basket = basket;
    }

    public Double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(Double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public Double getCampaignDiscount() {
        return campaignDiscount;
    }

    public void setCampaignDiscount(Double campaignDiscount) {
        this.campaignDiscount = campaignDiscount;
    }

    public boolean isCampaignDiscountApplied() {
        return campaignDiscountApplied;
    }

    public boolean isCampaignDiscountNotApplied() {
        return !isCampaignDiscountApplied();
    }

    public void setCampaignDiscountApplied(boolean campaignDiscountApplied) {
        this.campaignDiscountApplied = campaignDiscountApplied;
    }

    public Double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(Double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }
}