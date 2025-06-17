/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.controllers.report.beans;

import com.qb.app.model.TestProduct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ravis
 */

public class BrandBean {

    public String brands;
    public List<ProductBean> products;
    public String brandCount;
    public Map<String, Object> subreportParams;

    public BrandBean(String brands, List<ProductBean> products, String brandCount, String totalAmount, String totalQty) {
        this.brands = brands;
        this.products = products;
        this.brandCount = brandCount;

        Map<String, Object> params = new HashMap<>();
        params.put("Total_Amount", totalAmount);
        params.put("TotalStock", totalQty);

        setSubreportParams(params);
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public List<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBean> products) {
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
