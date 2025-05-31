package com.qb.app.controllers;

import com.qb.app.model.InterfaceMortion;
import com.qb.app.model.SVGIconGroup;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        String correctPin = "1234";

        if (enteredPin.equals(correctPin)) {
            // PIN is correct, enable training mode
            panelCashierController.enableTrainingMode();
            stage.close();
        } else {
            System.out.println("Incorrect PIN. Please try again.");
        }
    }

    private void setIcons() {
        closeIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/close-icon.svg"));
    }

    @FXML
    private void closeWindow() {
        stage.close();
    }
}
