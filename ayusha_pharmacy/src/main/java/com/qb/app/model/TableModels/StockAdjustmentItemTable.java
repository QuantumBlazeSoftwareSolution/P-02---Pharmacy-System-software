package com.qb.app.model.TableModels;

import com.qb.app.model.entity.Stock;

/**
 *
 * @author Vihanga
 */
public class StockAdjustmentItemTable {

    private String itemName;
    private Double qty;
    private Stock stock;

    public StockAdjustmentItemTable() {

    }

    public StockAdjustmentItemTable(Double qty, Stock stock) {
        this.itemName = stock.getProductId().getProduct();
        this.qty = qty;
        this.stock = stock;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

}
