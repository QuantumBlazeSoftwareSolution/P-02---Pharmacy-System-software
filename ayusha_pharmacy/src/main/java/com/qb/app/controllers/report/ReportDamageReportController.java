/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers.report;

import com.qb.app.model.SVGIconGroup;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author user
 */
public class ReportDamageReportController implements Initializable {

    @FXML
    private Group iconDamageReportTopic;
    @FXML
    private ComboBox<?> cbBrand;
    @FXML
    private Button btnRegister1;
    @FXML
    private Button btnClear11;
    @FXML
    private Button btnRegister11;
    @FXML
    private ScrollPane tableScrollContainer;
    @FXML
    private VBox tableBody;
    @FXML
    private ScrollBar tableScroller;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIcon();
    } 
    
     private void setIcon() {
        iconDamageReportTopic.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
    }
    
}
