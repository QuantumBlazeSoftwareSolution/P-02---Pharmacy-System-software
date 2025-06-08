package com.qb.app.controllers;

import com.qb.app.App;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.InterfaceAction;
import com.qb.app.model.InterfaceMortion;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.session.CompanyInfo;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class AdminVerificationController implements Initializable {

    @FXML
    private Rectangle quantumBlazeIcon;
    @FXML
    private Group iconAdmin;
    @FXML
    private Button btnExit;
    @FXML
    private Group iconExit;
    @FXML
    private AnchorPane root;
    @FXML
    private Button btnVerify;
    @FXML
    private PasswordField tfPinNumber;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setMouseEvent();
        setInitialState();
        setQBImage();
        // TODO
    }

    private void setInitialState() {
        tfPinNumber.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        setIcons();
        Rectangle clip = new Rectangle(root.getPrefWidth(), root.getPrefHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        root.setClip(clip);
    }

    private void setIcons() {
        iconAdmin.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/users-solid.svg"));
        iconExit.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/exit-solid.svg"));
    }

    private void setMouseEvent() {
        InterfaceMortion interfaceMortion = new InterfaceMortion();
        interfaceMortion.enableDrag(root);
    }

    private void setQBImage() {
        Image image = new Image(getClass().getResource("/com/qb/app/assets/images/QB_LOGO.png").toExternalForm());
        quantumBlazeIcon.setFill(new ImagePattern(image));
    }

    @FXML
    private void handleSystemVerification(ActionEvent event) {
        if (event.getSource() == btnExit) {
            InterfaceAction.closeWindow(btnExit);
        } else if (event.getSource() == btnVerify) {
            verifyAdmin();
        }
    }

    private void verifyAdmin() {
        if (!tfPinNumber.getText().isEmpty()) {
            if (tfPinNumber.getText().equals(CompanyInfo.authenticationNumber)) {
                try {
                    App.setRoot("panelAdmin");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                CustomAlert.showStyledAlert(
                        root,
                        "The PIN you entered is incorrect. Please verify and try again.",
                        "Invalid PIN Entered",
                        Alert.AlertType.WARNING
                );
            }
        } else {
            CustomAlert.showStyledAlert(
                    root,
                    "Please enter your administrator PIN to continue",
                    "Administrator Verification Required",
                    Alert.AlertType.WARNING
            );
        }
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            verifyAdmin();
        }
    }

}
