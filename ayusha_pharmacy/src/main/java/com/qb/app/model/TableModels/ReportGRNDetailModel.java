package com.qb.app.model.TableModels;

public class ReportGRNDetailModel {
    private final int id;
    private final String productName;
    public final String genericName;
    private final double costPrice;
    private final double qty;
    private final double amount;

    // Original constructor (amount is calculated)
    public ReportGRNDetailModel(int id, String productName, double costPrice, double qty, String genericName) {
        this.id = id;
        this.productName = productName;
        this.costPrice = costPrice;
        this.qty = qty;
        this.amount = costPrice * qty;
        this.genericName=genericName;
    }

    // ✅ New constructor that accepts amount explicitly
    public ReportGRNDetailModel(int id, String productName, double costPrice, double qty, double amount, String genericName) {
        this.id = id;
        this.productName = productName;
        this.costPrice = costPrice;
        this.qty = qty;
        this.amount = amount;
        this.genericName=genericName;
    }

    public int getId() {
        return id;
    }

    public String getProduct() {
        return productName;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public double getQty() {
        return qty;
    }

    public double getSale() {
        return amount;
    }

    public double getAmount() {
        return amount;
    }

    public String getGenericName() {
        return genericName;
    }
}
