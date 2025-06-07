package com.qb.app.controllers.report.beans;

public class GrnItemBean {

    public String pId;
    public String pName;
    public String pGeneric;
    public String pCost;
    public String qty;
    public String itemTotal;

    public GrnItemBean(String pId, String pName, String pGeneric, String pCost, String qty, String itemTotal) {
        this.pId = pId;
        this.pName = pName;
        this.pGeneric = pGeneric;
        this.pCost = pCost;
        this.qty = qty;
        this.itemTotal = itemTotal;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpGeneric() {
        return pGeneric;
    }

    public void setpGeneric(String pGeneric) {
        this.pGeneric = pGeneric;
    }

    public String getpCost() {
        return pCost;
    }

    public void setpCost(String pCost) {
        this.pCost = pCost;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(String itemTotal) {
        this.itemTotal = itemTotal;
    }
}
