/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers;

import com.qb.app.model.entity.Company;
import java.net.URL;
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
public class PopUp_CompanyList_TableRowController implements Initializable {

    @FXML
    private Label companyID;
    @FXML
    private Label companyName;
    @FXML
    private Label companyAddress;

    private PopUpCompanyListController popUpController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleClickEvent(MouseEvent event) {
        if (event.getClickCount() == 2) {
            popUpController.callingController.setCompanyID(companyID.getText());
            popUpController.closeWindow();
        }
    }

    public void setPopUpController(PopUpCompanyListController controller) {
        this.popUpController = controller;
    }

    public void setItems(Company item) {
        companyID.setText(String.valueOf(item.getId()));
        companyName.setText(item.getName());
        companyAddress.setText(item.getAddress());
    }

}
