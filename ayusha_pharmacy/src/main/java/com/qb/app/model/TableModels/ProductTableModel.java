package com.qb.app.model.TableModels;

public class ProductTableModel {

    private final Integer id;
    private final String productName;
    private final String genericName;
    private final String brandName;
    private final Double salePrice;
    private final Double costPrice;
    private final String unit;
    private final Double measure;
    private final Double discount;
    private final String status;

    public ProductTableModel(Integer id, String productName, String genericName,
            String brandName, Double salePrice, Double costPrice,
            String unit, Double measure, Double discount, String status) {
        this.id = id;
        this.productName = productName;
        this.genericName = (genericName == null || genericName.trim().isEmpty()) ? "N/A" : genericName;
        this.brandName = brandName;
        this.salePrice = salePrice;
        this.costPrice = costPrice;
        this.unit = unit;
        this.measure = measure;
        this.discount = discount;
        this.status = status;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getGenericName() {
        return genericName;
    }

    public String getBrandName() {
        return brandName;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public String getUnit() {
        return unit;
    }

    public Double getMeasure() {
        return measure;
    }

    public Double getDiscount() {
        return discount;
    }

    public String getStatus() {
        return status;
    }
}
