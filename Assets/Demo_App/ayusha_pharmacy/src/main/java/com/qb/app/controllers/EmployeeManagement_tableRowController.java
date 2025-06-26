/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers;

import com.qb.app.model.SVGIconGroup;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;

/**
 * FXML Controller class
 *
 * @author user
 */
public class EmployeeManagement_tableRowController implements Initializable {

    @FXML
    private Group editIcon;
    @FXML
    private Group visibleIcon;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIcon();
    }
    
    private void setIcon() {
        editIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/edit_icon.svg"));
        visibleIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/visible_icon.svg"));
    }

    
    
}
