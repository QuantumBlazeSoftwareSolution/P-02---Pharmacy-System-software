package com.qb.app.controllers;

import com.qb.app.model.entity.Company;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class PopUp_CompanyList_TableRowController implements Initializable {

    @FXML
    private Label companyID;
    @FXML
    private Label companyName;
    @FXML
    private Label companyAddress;
    private PopUpCompanyListController popUpCompanyListController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleClickEvent(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // send company id to the company controller
            popUpCompanyListController.closeWindow();
        }
    }

    public void setPopUpController(PopUpCompanyListController controller) {
        this.popUpCompanyListController = controller;
    }

    public void setItems(Company item) {
        companyID.setText(item.getId().toString());
        companyName.setText(item.getName());
        companyAddress.setText(item.getAddress() != null ? item.getAddress() : "N/A");
    }

}
