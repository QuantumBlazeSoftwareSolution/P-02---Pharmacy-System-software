/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.model.TableModels;

import com.qb.app.model.DefaultAPI;
import com.qb.app.model.entity.Stock;

/**
 *
 * @author Vihanga
 */
public class StockAdjustmentTable {

    private String stockId;
    private String itemName;
    private String qty;
    private String costPrice;
    private String salePrice;
    private Stock stock;

    public StockAdjustmentTable() {
    }

    public StockAdjustmentTable(Stock stock) {
        this.stock = stock;

        this.stockId = stock.getId().toString();
        this.itemName = stock.getProductId().getProduct();
        this.qty = String.format("%.2f", stock.getQty() / stock.getProductId().getMeasure());
        this.costPrice = String.format(DefaultAPI.currencyFloatFormat, stock.getProductId().getCostPrice());
        this.salePrice = String.format(DefaultAPI.currencyFloatFormat, stock.getProductId().getSalePrice());
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
