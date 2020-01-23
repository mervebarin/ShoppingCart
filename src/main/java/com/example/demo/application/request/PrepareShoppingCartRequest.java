package com.example.demo.application.request;

import com.example.demo.domain.model.entity.Coupon;

import java.util.HashMap;

public class PrepareShoppingCartRequest {

    private HashMap<Long, Integer> productIdQuantityMap;
    private Coupon coupon;

    public HashMap<Long, Integer> getProductIdQuantityMap() {
        return productIdQuantityMap;
    }

    public void setProductIdQuantityMap(HashMap<Long, Integer> productIdQuantityMap) {
        this.productIdQuantityMap = productIdQuantityMap;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}