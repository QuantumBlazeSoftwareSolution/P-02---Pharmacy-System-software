package com.qb.app.controllers;

import com.qb.app.model.entity.Brand;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class PopUp_BrandList_TableRowController implements Initializable {

    @FXML
    private Label labelID;
    @FXML
    private Label labelBrand;
    @FXML
    private Label labelStatus;
    private PopUpBrandListController controller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setPopUpController(PopUpBrandListController controller) {
        this.controller = controller;
    }

    public void setItems(Brand item) {
        labelID.setText(String.valueOf(item.getId()));
        labelBrand.setText(item.getBrand());
        labelStatus.setText(item.getProductStatusId().getStatus());
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            controller.callingController.setBrandID(labelID.getText());
            controller.closeWindow();
        }
    }

}
