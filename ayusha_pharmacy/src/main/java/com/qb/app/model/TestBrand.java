package com.qb.app.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestBrand {

    public String brands;
    public List<TestProduct> products;
    public String brandCount;
    public Map<String, Object> subreportParams;

    public TestBrand(String brands, List<TestProduct> products, String brandCount) {
        this.brands = brands;
        this.products = products;
        this.brandCount = brandCount;

        Map<String, Object> params = new HashMap<>();
        params.put("Total_Amount", "LKR. 23,500.00 | " + brands);
        params.put("TotalStock", "23 | " + brands);

        setSubreportParams(params);
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public List<TestProduct> getProducts() {
        return products;
    }

    public void setProducts(List<TestProduct> products) {
        this.products = products;
    }

    public String getBrandCount() {
        return brandCount;
    }

    public void setBrandCount(String brandCount) {
        this.brandCount = brandCount;
    }

    public Map<String, Object> getSubreportParams() {
        return subreportParams;
    }

    public final void setSubreportParams(Map<String, Object> subreportParams) {
        this.subreportParams = subreportParams;
    }

}
