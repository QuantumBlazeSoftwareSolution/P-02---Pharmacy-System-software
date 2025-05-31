/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Vihanga
 */
public class PopUp_ProductList_TableRowController implements Initializable {

    @FXML
    private Label itemID;
    @FXML
    private Label itemName;
    @FXML
    private Label itemBrand;
    @FXML
    private Label itemPrice;
    @FXML
    private Label itemCost;
    @FXML
    private Label itemUnit;
    @FXML
    private Label itemMeasure;
    @FXML
    private Label itemDiscount;
    @FXML
    private Label itemBarCode;

    private PopUpProductListController popUpProductListController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setItems(
            String id,
            String name,
            String brand,
            double price,
            double cost,
            String unit,
            String measure,
            double discount,
            String barcode
    ) {
        // Input validation
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");

        // Set values with formatting
        setLabelText(itemID, id);
        setLabelText(itemName, name);
        setLabelText(itemBrand, brand);
        setFormattedDouble(itemPrice, price, "%.2f");
        setFormattedDouble(itemCost, cost, "%.2f");
        setLabelText(itemUnit, unit);
        setLabelText(itemMeasure, measure);
//        setFormattedDouble(itemMeasure, measure, "%.2f");
        setFormattedDouble(itemDiscount, discount, "%.2f%%");
        setLabelText(itemBarCode, barcode);
    }

    private void setLabelText(Label label, String value) {
        label.setText(value != null ? value : "");
    }

    private void setFormattedDouble(Label label, double value, String format) {
        label.setText(String.format(format, value));
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            popUpProductListController.closeWindow();
            popUpProductListController.productRegistrationController.setParentID(itemID.getText());
        }
    }

    public void setPopUpController(PopUpProductListController popUpProductListController) {
        this.popUpProductListController = popUpProductListController;
    }
}
