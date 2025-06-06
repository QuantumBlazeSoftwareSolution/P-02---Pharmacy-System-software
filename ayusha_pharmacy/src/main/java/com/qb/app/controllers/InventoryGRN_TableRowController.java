package com.qb.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class InventoryGRN_TableRowController implements Initializable {

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @FXML
    private Label labelID;
    @FXML
    private Label labelProduct;
    @FXML
    private Label labelCost;
    @FXML
    private Label labelQty;
    @FXML
    private Label labelAmount;

    public int productID;
    public int qty;
    public double cost;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setData(Integer id, String product, double costPrice, int itemQty) {
        setProductID(id);
        setQty(itemQty);
        setCost(costPrice);
        labelID.setText(String.valueOf(id));
        labelProduct.setText(product);
        labelCost.setText(String.format("Rs. %,.2f", costPrice));
        labelQty.setText(String.valueOf(itemQty));
        labelAmount.setText(String.format("Rs. %,.2f", costPrice * itemQty));
    }

    public int getProductID() {
        return productID;
    }

    private void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setProductQty(int i) {
        setQty(i);
        labelAmount.setText(String.format("Rs. %,.2f", getCost() * getQty()));
    }

}
