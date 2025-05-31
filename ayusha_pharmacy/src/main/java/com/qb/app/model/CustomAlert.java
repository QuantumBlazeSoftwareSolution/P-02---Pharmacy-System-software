package com.qb.app.model;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CustomAlert {

    public static void showStyledAlert(Node node, String message, Alert.AlertType type) {
        Window owner = node.getScene().getWindow();
        Alert alert = new Alert(type);
        alert.setTitle("System Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Set owner if provided
        if (owner != null) {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.initOwner(owner);
        }

        alert.show();
    }

    public static void showStyledAlert(Node node, String message, String title, Alert.AlertType type) {
        Window owner = node.getScene().getWindow();
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Set owner if provided
        if (owner != null) {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.initOwner(owner);
        }

        alert.show();
    }
}
