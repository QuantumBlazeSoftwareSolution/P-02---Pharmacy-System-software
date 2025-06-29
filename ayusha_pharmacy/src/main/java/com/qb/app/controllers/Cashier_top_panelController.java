package com.qb.app.controllers;

import com.qb.app.App;
import com.qb.app.model.InterfaceAction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.getLogger;
import com.qb.app.session.ApplicationSession;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Cashier_top_panelController implements Initializable {

    @FXML
    private StackPane iconMenu;
    @FXML
    private Label panelTitle;
    @FXML
    private StackPane iconMinimize;
    @FXML
    private StackPane iconClose;

    private PanelCashierController panelCashierController; // Reference to PanelCashierController

    public void setPanelCashierController(PanelCashierController panelCashierController) {
        this.panelCashierController = panelCashierController;
    }

    @FXML
    private void toggleMenu() {
        if (panelCashierController != null) {
            panelCashierController.toggleMenu(); // Call the toggleMenu method in PanelCashierController
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIcons();
    }

    private void setIcons() {
        iconMenu.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/menu-icon.svg"));
        iconClose.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/close-icon.svg"));
        iconMinimize.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/minimize-icon.svg"));
    }

    public void setTitle(String title) {
        panelTitle.setText(title);
    }

    @FXML
    private void titleBarActionHandler(MouseEvent event) {
        if (event.getSource() == iconClose) {
            if (ApplicationSession.getSession() != null) {
                if (ApplicationSession.getSession().getStatus().equals("OFF")) {
                    try {
                        App.setRoot("sytemLogin");
                    } catch (IOException e) {
                        e.printStackTrace();
                        getLogger.logger().warning(e.toString());
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Session Active - Confirmation Required");
                    alert.setHeaderText("Active Session Detected");
                    alert.setContentText("You have an active sales session.\n\nPlease complete or cancel the current sale before exiting the system.");

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(getClass().getResource("/com/qb/app/assets/images/logo.png").toExternalForm()));

                    ButtonType exitButton = new ButtonType("Exit Anyway", ButtonBar.ButtonData.CANCEL_CLOSE);
                    ButtonType stayButton = new ButtonType("Stay in System", ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(stayButton, exitButton);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == exitButton) {
                        try {
                            App.setRoot("sytemLogin");
                        } catch (IOException e) {
                            e.printStackTrace();
                            getLogger.logger().warning(e.toString());
                        }
                    }
                }
            } else {
                try {
                    App.setRoot("sytemLogin");
                } catch (IOException e) {
                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            }
        } else if (event.getSource() == iconMinimize) {
            InterfaceAction.minimizeWindow(iconMinimize);
        }
    }

}
