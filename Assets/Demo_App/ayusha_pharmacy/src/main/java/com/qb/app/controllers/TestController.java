/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers;

import com.qb.app.model.DefaultAPI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
/**
 * FXML Controller class
 *
 * @author Vihanga
 */
public class TestController implements Initializable {


    @FXML
    private ScrollPane tableScrollContainer;
    @FXML
    private VBox tableContainer;
    @FXML
    private ScrollBar tableScroller;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DefaultAPI.bindTableScroll(tableScroller, tableScrollContainer, tableContainer);
    }    
    
}
