package com.qb.app.model.TableModels;

import com.qb.app.model.entity.Invoice;
import com.qb.app.model.entity.InvoiceItem;
import com.qb.app.model.entity.Product;
import javafx.beans.property.*;

public class ReportSaleDetailModel {

    private final IntegerProperty id;
    private final StringProperty invoiceNo;
    private final StringProperty product;
    private final DoubleProperty qty;
    private final DoubleProperty cost;
    private final DoubleProperty sale;
    private final DoubleProperty discount;
    private final DoubleProperty billDiscount;
    private final DoubleProperty profit;
    private final DoubleProperty unitPrice;
    private final double invoiceDiscount;

    public ReportSaleDetailModel(Invoice invoice, Product product, InvoiceItem item) {
        this.invoiceDiscount = invoice.getBillDiscount();
        this.id = new SimpleIntegerProperty(product.getId());
        this.invoiceNo = new SimpleStringProperty(String.format("INV-%06d", invoice.getId()));
        this.product = new SimpleStringProperty(product.getProduct());
        this.qty = new SimpleDoubleProperty(item.getQty());

        double costVal = item.getQty() * item.getCostPrice();
        double saleVal = item.getQty() * item.getSalePrice();
        double discountVal = item.getQty() * item.getDiscount();
        double profitVal = saleVal - costVal - discountVal;

        this.cost = new SimpleDoubleProperty(costVal);
        this.sale = new SimpleDoubleProperty(saleVal);
        this.discount = new SimpleDoubleProperty(discountVal);
        this.billDiscount = new SimpleDoubleProperty(invoice.getBillDiscount());
        this.profit = new SimpleDoubleProperty(profitVal);
        this.unitPrice = new SimpleDoubleProperty(item.getSalePrice());
    }

    // ─── Getters (Used Internally or Manually) ──────────────
    public int getId() {
        return id.get();
    }

    public double getInvoiceDiscount() {
        return this.invoiceDiscount;
    }

    public String getInvoiceNo() {
        return invoiceNo.get();
    }

    public String getProduct() {
        return product.get();
    }

    public double getQty() {
        return qty.get();
    }

    public double getCost() {
        return cost.get();
    }

    public double getSale() {
        return sale.get();
    }

    public double getDiscount() {
        return discount.get();
    }

    public double getProfit() {
        return profit.get();
    }

    public double getBillDiscount() {
        return billDiscount.get();
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    // ─── Property Getters (Used by TableView Bindings) ──────
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty invoiceNoProperty() {
        return invoiceNo;
    }

    public StringProperty productProperty() {
        return product;
    }

    public DoubleProperty qtyProperty() {
        return qty;
    }

    public DoubleProperty costProperty() {
        return cost;
    }

    public DoubleProperty saleProperty() {
        return sale;
    }

    public DoubleProperty discountProperty() {
        return discount;
    }

    public DoubleProperty billDiscountProperty() {
        return billDiscount;
    }

    public DoubleProperty profitProperty() {
        return profit;
    }

    public DoubleProperty unitPriceProperty() {
        return unitPrice;
    }
}
