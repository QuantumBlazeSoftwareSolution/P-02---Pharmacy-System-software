package com.qb.app.model;

import java.util.List;

public class TestBrand {
    
    private String brandName;
    private List<TestProduct> products;

    public TestBrand(String brandName, List<TestProduct> products) {
        this.brandName = brandName;
        this.products = products;
    }

    public String getBrandName() {
        return brandName;
    }

    public List<TestProduct> getProducts() {
        return products;
    }
}