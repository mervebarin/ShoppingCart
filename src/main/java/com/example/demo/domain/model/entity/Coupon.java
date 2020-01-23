package com.example.demo.domain.model.entity;

import com.example.demo.domain.model.enumtype.DiscountType;

import javax.persistence.*;

@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "min_purchase_price", nullable = false)
    private Double minimumPurchasePrice;

    @Column(name = "price", nullable = false)
    private Double couponPrice;

    @Column(name = "discount_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    public Coupon() {
    }

    public Coupon(Double minimumPurchasePrice, Double couponPrice, DiscountType discountType) {
        this.minimumPurchasePrice = minimumPurchasePrice;
        this.couponPrice = couponPrice;
        this.discountType = discountType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMinimumPurchasePrice() {
        return minimumPurchasePrice;
    }

    public void setMinimumPurchasePrice(Double minimumPurchasePrice) {
        this.minimumPurchasePrice = minimumPurchasePrice;
    }

    public Double getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(Double couponPrice) {
        this.couponPrice = couponPrice;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }
}
