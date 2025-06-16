/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers.report;

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
public class ReportGrn_TableRowController implements Initializable {

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setData(Product product, Integer id, String productName, double costPrice, double itemQty) {
     
        this.id.setText(String.valueOf(id));
        ProductName.setText(productName);
        CostPrice.setText(String.format("Rs. %,.2f", costPrice));
        Qty.setText(String.valueOf(itemQty));
        Amount.setText(String.format("Rs. %,.2f", costPrice * itemQty));
    }
    
}
