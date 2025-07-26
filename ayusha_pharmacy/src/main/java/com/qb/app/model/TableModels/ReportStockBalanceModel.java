package com.qb.app.model.TableModels;

public class ReportStockBalanceModel {
    private final int id;
    private final String brand;
    private final String product;
    private final double qty;
    private final double costPrice;
    private final double salePrice;
    private final double profit;

    public ReportStockBalanceModel(int id, String brand, String product, double qty, double costPrice, double salePrice) {
        this.id = id;
        this.brand = brand;
        this.product = product;
        this.qty = qty;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
        this.profit = (salePrice * qty) - (costPrice * qty);
    }

    public int getId() { return id; }
    public String getBrand() { return brand; }
    public String getProduct() { return product; }
    public double getQty() { return qty; }
    public double getCostPrice() { return costPrice; }
    public double getSalePrice() { return salePrice; }
    public double getProfit() { return profit; }
}
