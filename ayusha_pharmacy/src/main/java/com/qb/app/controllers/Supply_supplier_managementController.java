
package com.qb.app.controllers;

import com.qb.app.model.SVGIconGroup;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;


public class Supply_supplier_managementController implements Initializable {

    @FXML
    private Group suppierManagementIcon;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
   suppierManagementIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));    }    
    
}
