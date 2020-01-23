package com.example.demo.domain.model.enumtype;

public enum DiscountType {

    RATE("RATE"),
    AMOUNT("AMOUNT");

    private final String value;

    DiscountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}