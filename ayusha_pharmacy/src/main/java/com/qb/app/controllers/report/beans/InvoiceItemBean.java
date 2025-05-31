package com.qb.app.controllers.report.beans;

import java.io.Serializable;

public class InvoiceItemBean implements Serializable {

    private String productName;
    private String unitPrice;
    private String qty;
    private String amount;

    public InvoiceItemBean() {
    }

    public InvoiceItemBean(String productName, String unitPrice, String qty, String amount) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
