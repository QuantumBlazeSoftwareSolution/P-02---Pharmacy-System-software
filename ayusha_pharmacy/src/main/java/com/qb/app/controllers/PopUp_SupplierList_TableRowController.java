package com.qb.app.controllers;

import com.qb.app.model.entity.Supplier;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class PopUp_SupplierList_TableRowController implements Initializable {

    @FXML
    private Label labelID;
    @FXML
    private Label labelSupplierName;
    @FXML
    private Label labelCompanyName;
    @FXML
    private Label labelStatus;
    private PopUpSupplierListController controller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setPopUpController(PopUpSupplierListController controller) {
        this.controller = controller;
    }

    public void setItems(Supplier item) {
        labelID.setText(String.valueOf(item.getId()));
        labelSupplierName.setText(item.getName());
        labelCompanyName.setText(item.getCompanyId().getName());
        labelStatus.setText(item.getSupplierStatusId().getStatus());
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            controller.callingController.setSupplierID(labelID.getText());
            controller.closeWindow();
        }
    }

}
