package com.qb.app.controllers;

import com.qb.app.model.CustomAlert;
import com.qb.app.model.InterfaceMortion;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.session.CompanyInfo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TrainingVerificationController implements Initializable {

    @FXML
    private StackPane closeIcon;
    @FXML
    private TextField tfPin;

    private Stage stage;
    private PanelCashierController panelCashierController;
    @FXML
    private AnchorPane root;
    @FXML
    private Button verifyBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIcons();
        InterfaceMortion interfaceMortion = new InterfaceMortion();
        interfaceMortion.enableDrag(root);
    }

    public void setMainController(PanelCashierController panelCashierController) {
        this.panelCashierController = panelCashierController;
    }

    // Set the stage for closing the verification window
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handlePinVerification() {
        String enteredPin = tfPin.getText();

        if (enteredPin.equals(CompanyInfo.authenticationNumber)) {
            // PIN is correct, enable training mode
            panelCashierController.isTrainingOpened = false;
            panelCashierController.enableTrainingMode();
            stage.close();
        } else {
            CustomAlert.showStyledAlert(root, "Incorrect PIN. Please try again.", Alert.AlertType.WARNING);
        }
    }

    private void setIcons() {
        closeIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/close-icon.svg"));
    }

    @FXML
    private void closeWindow() {
        panelCashierController.isTrainingOpened = false;
        stage.close();
    }
    
}
