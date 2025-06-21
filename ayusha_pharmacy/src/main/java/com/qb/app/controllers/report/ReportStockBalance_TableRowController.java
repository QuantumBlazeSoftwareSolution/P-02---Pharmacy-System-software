/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers.report;

import com.qb.app.model.entity.Brand;
import com.qb.app.model.entity.Product;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author ravis
 */
public class ReportStockBalance_TableRowController implements Initializable {

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @FXML
    private Label ID;
    @FXML
    private Label Brand;
    @FXML
    private Label Product;
    @FXML
    private Label Qty;
    @FXML
    private Label CostPrice;
    @FXML
    private Label SalePrice;
    @FXML
    private Label Profit;

    /**
     * Initializes the controller class.
     */
    
    private Product product;
    private Brand brand;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
       public void setData(Product product,Brand brand,double qty) {
           
           setProduct(product);
           setBrand(brand);

           this.ID.setText(String.valueOf(product.getId()));
           Brand.setText(String.valueOf(brand.getBrand()));
           Product.setText(String.valueOf(product.getProduct()));
           Qty.setText(String.valueOf(qty));
           CostPrice.setText(String.format("Rs. %,.2f", product.getCostPrice()));
           SalePrice.setText(String.format("Rs. %,.2f", product.getSalePrice()));
           double profit = product.getSalePrice()-product.getCostPrice();
           Profit.setText(String.format("Rs. %,.2f", profit));
          
     }
       
//             private  double divideWithPharmacyRounding(double qty, float measure) {
//        int whole = (int) (qty / measure);
//        int remainder = (int) (qty % measure);
//
//        double result = whole + (remainder / 100.0);
//        return result;
//    }
    
}
