package com.qb.app.controllers;

import com.qb.app.model.SVGIconGroup;
import com.qb.app.session.ApplicationSession;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Admin_top_panelController implements Initializable {

    @FXML
    private StackPane iconMenu;
    @FXML
    private Label employeeName;
    @FXML
    private Label employeeType;

    private PanelAdminController panelAdminController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setAdminProfile();
        setIcons();
    }

    @FXML
    private void toggleMenu(MouseEvent event) {
        if (panelAdminController != null) {
            panelAdminController.toggleMenu(); // Call the toggleMenu method in PanelCashierController
        }
    }

    public void setPanelAdminController(PanelAdminController panelAdminController) {
        this.panelAdminController = panelAdminController;
    }

    public void setAdminProfile() {
        employeeName.setText(ApplicationSession.getEmployee().getName());
        employeeType.setText(ApplicationSession.getEmployee().getEmployeeRoleId().getRole());
    }

    private void setIcons() {
        iconMenu.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/menu-icon.svg"));
    }
}
