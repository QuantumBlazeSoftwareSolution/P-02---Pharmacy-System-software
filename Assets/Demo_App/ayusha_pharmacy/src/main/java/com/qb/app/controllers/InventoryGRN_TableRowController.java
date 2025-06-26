package com.qb.app.controllers;

import com.qb.app.model.entity.Product;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class InventoryGRN_TableRowController implements Initializable {

    private Inventory_grnController GrnController;

    public double getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(double itemAmount) {
        this.itemAmount = itemAmount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

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
    public double qty;
    public double cost;
    public Product product;
    public double itemAmount;
    private Node rootNode;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setData(
            Product product,
            Integer id,
            String productName,
            double costPrice,
            double itemQty,
            Node node,
            Inventory_grnController controller) {
        this.GrnController = controller;
        this.rootNode = node;
        setProduct(product);
        setProductID(id);
        setQty(itemQty);
        setCost(costPrice);
        setItemAmount(costPrice * itemQty);
        labelID.setText(String.valueOf(id));
        labelProduct.setText(productName);
        labelCost.setText(String.format("Rs. %,.2f", costPrice));
        labelQty.setText(String.valueOf(itemQty));
        labelAmount.setText(String.format("Rs. %,.2f", getItemAmount()));
    }

    public int getProductID() {
        return productID;
    }

    private void setProductID(int productID) {
        this.productID = productID;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public void setProductQty(double qty) {
        setQty(qty);
        labelQty.setText(String.valueOf(qty));
        labelAmount.setText(String.format("Rs. %,.2f", getCost() * getQty()));
    }

    public Node getRootNode() {
        return rootNode;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            GrnController.removeInvoiceItem(this);
        }
    }

}
