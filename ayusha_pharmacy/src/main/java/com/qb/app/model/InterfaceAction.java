package com.qb.app.model;

import javafx.scene.Node;
import javafx.stage.Stage;

public class InterfaceAction {

    public static void closeWindow(Node node) {
        if (node != null && node.getScene() != null) {
            Stage stage = (Stage) node.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        }
    }

    public static void minimizeWindow(Node node) {
        if (node != null && node.getScene() != null) {
            Stage stage = (Stage) node.getScene().getWindow();
            if (stage != null) {
                stage.setIconified(true); // Minimizes the window
            }
        }
    }
}
