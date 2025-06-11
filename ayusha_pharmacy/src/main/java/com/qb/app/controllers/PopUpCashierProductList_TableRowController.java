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
public class PopUpCashierProductList_TableRowController implements Initializable {

    @FXML
    private Label itemID;
    @FXML
    private Label ItemName;
    @FXML
    private Label itemGeneric;
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
    private Label itemBarcode;
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
            String generic,
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
        setLabelText(ItemName, name);
        setLabelText(itemGeneric, generic);
        setFormattedDouble(itemPrice, price, "Rs. %.2f");
        setFormattedDouble(itemCost, cost, "Rs. %.2f");
        setLabelText(itemUnit, unit);
        setLabelText(itemMeasure, measure);
//        setFormattedDouble(itemMeasure, measure, "%.2f");
        setFormattedDouble(itemDiscount, discount, "Rs. %.2f");
        setLabelText(itemBarcode, barcode);
    }

    private void setLabelText(Label label, String value) {
        label.setText(value != null ? value : "");
    }

    private void setFormattedDouble(Label label, double value, String format) {
        label.setText(String.format(format, value));
    }

    private void handleMouseClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            popUpProductListController.closeWindow();
            switch (PopUpProductListController.callingController) {
                case PopUpCashierProductListController controller ->
                    controller.setParentID(itemID.getText());
                default -> {

                }
            }
        }
    }

    public void setPopUpController(PopUpProductListController popUpProductListController) {
        this.popUpProductListController = popUpProductListController;
    }

}
