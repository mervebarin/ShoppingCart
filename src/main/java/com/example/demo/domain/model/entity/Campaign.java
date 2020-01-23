package com.example.demo.domain.model.entity;

import com.example.demo.domain.model.enumtype.DiscountType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "campaign")
public class Campaign {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "campaign_price", nullable = false)
    private Double campaignPrice;

    @Column(name = "min_product_amount", nullable = false)
    private BigDecimal minNumberOfProductsRequired;

    @Column(name = "discount_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    public Campaign() {
    }

    public Campaign(Category category, Double campaignPrice, BigDecimal minNumberOfProductsRequired, DiscountType discountType) {
        this.category = category;
        this.campaignPrice = campaignPrice;
        this.minNumberOfProductsRequired = minNumberOfProductsRequired;
        this.discountType = discountType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getCampaignPrice() {
        return campaignPrice;
    }

    public void setCampaignPrice(Double campaignPrice) {
        this.campaignPrice = campaignPrice;
    }

    public BigDecimal getMinNumberOfProductsRequired() {
        return minNumberOfProductsRequired;
    }

    public void setMinNumberOfProductsRequired(BigDecimal minNumberOfProductsRequired) {
        this.minNumberOfProductsRequired = minNumberOfProductsRequired;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }
}