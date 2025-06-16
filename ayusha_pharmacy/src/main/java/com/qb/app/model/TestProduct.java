package com.qb.app.model;

public class TestProduct {

    public String item_id;
    public String item_name;
    public String item_generic;
    public String unit_price;
    public String balance_stock;
    public String amount;

    public TestProduct(String item_id, String item_name, String item_generic, String unit_price, String balance_stock, String amount) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_generic = item_generic;
        this.unit_price = unit_price;
        this.balance_stock = balance_stock;
        this.amount = amount;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_generic() {
        return item_generic;
    }

    public void setItem_generic(String item_generic) {
        this.item_generic = item_generic;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getBalance_stock() {
        return balance_stock;
    }

    public void setBalance_stock(String balance_stock) {
        this.balance_stock = balance_stock;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    
}
