/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers;

import com.qb.app.controllers.PanelDeveloperController;
import com.qb.app.model.SVGIconGroup;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author Vihanga
 */
public class Developer_top_panelController implements Initializable {

    @FXML
    private StackPane iconMenu;
    @FXML
    private Group iconDeveloper;
    @FXML
    private Label title;

    private PanelDeveloperController panelDeveloperController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIcons();
    }

    @FXML
    private void toggleMenu(MouseEvent event) {
        panelDeveloperController.toggleMenu();
    }

    public void setPanelDeveloperController(PanelDeveloperController panelDeveloperController) {
        this.panelDeveloperController = panelDeveloperController;
    }

    private void setIcons() {
        iconMenu.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/menu-icon.svg"));
        iconDeveloper.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/developer-console.svg"));
    }

    private void setTitle(String title) {
        this.title.setText(title);
    }

}
