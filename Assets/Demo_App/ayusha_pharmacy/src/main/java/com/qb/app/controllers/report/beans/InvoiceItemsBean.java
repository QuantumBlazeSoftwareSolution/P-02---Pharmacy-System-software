
package com.qb.app.controllers.report.beans;

/**
 *
 * @author ravis
 */

public class InvoiceItemsBean {

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getInv_item() {
        return inv_item;
    }

    public void setInv_item(String inv_item) {
        this.inv_item = inv_item;
    }

    public String getInv_cost() {
        return inv_cost;
    }

    public void setInv_cost(String inv_cost) {
        this.inv_cost = inv_cost;
    }

    public String getInv_sale() {
        return inv_sale;
    }

    public void setInv_sale(String inv_sale) {
        this.inv_sale = inv_sale;
    }

    public String getInv_qty() {
        return inv_qty;
    }

    public void setInv_qty(String inv_qty) {
        this.inv_qty = inv_qty;
    }

    public String getInv_disc() {
        return inv_disc;
    }

    public void setInv_disc(String inv_disc) {
        this.inv_disc = inv_disc;
    }

    public String getInv_amount() {
        return inv_amount;
    }

    public void setInv_amount(String inv_amount) {
        this.inv_amount = inv_amount;
    }

    public String getInv_profit() {
        return inv_profit;
    }

    public void setInv_profit(String inv_profit) {
        this.inv_profit = inv_profit;
    }
 private String product_id;
 private String inv_item;
 private String inv_cost;
 private String inv_sale;
 private String inv_qty;
 private String inv_disc;
 private String inv_amount;
 private String inv_profit;
 public InvoiceItemsBean(
         String pid,String invid,String cost,String sale,String qty,String disc,String amount,String profit) {
        this.product_id = pid;
        this.inv_item =invid ;
        this.inv_cost =cost ;
        this.inv_sale =sale ;
        this.inv_disc =disc ;
        this.inv_qty =qty ;
        this.inv_amount =amount ;
        this.inv_profit =profit ;
     
    }
    
}
