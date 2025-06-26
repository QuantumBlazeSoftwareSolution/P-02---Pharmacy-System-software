
package com.qb.app.controllers.report;

import com.qb.app.model.entity.Product;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


public class ReportGrn_TableRowController implements Initializable {

    public double getProductQty() {
        return productQty;
    }

    public void setProductQty(double productQty) {
        this.productQty = productQty;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }



    public double getProductCost() {
        return productCost;
    }

    public void setProductCost(double productCost) {
        this.productCost = productCost;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(double productAmount) {
        this.productAmount = productAmount;
    }

    @FXML
    private Label id;
    @FXML
    private Label ProductName;
    @FXML
    private Label CostPrice;
    @FXML
    private Label Qty;
    @FXML
    private Label Amount;
    
    private int productId;
    private double productQty;
    private double productCost;
    private String productName;
    private double productAmount;
    private Product product;

 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setData(Product product, Integer id, String productName, double costPrice, double itemQty) {
        
        setProduct(product);
        setProductId(id);
        setProductName(productName);
        setProductCost(costPrice);
        setProductQty(itemQty);
        setProductAmount(costPrice * itemQty);
     
        this.id.setText(String.valueOf(id));
        ProductName.setText(productName);
        CostPrice.setText(String.format("Rs. %,.2f", costPrice));
        Qty.setText(String.valueOf(itemQty));
        Amount.setText(String.format("Rs. %,.2f", costPrice * itemQty));
    }
    
}
