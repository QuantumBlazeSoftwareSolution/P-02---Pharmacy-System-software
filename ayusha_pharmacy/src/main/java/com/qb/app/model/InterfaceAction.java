package com.qb.app.model;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;

public class InterfaceAction {

    public static void closeWindow(Node node) {
        if (node != null && node.getScene() != null) {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close(); // Close this window

            // Check if this was the last open window
            if (Stage.getWindows().stream().filter(Window::isShowing).count() == 0) {
                Platform.exit(); // Exit if no windows left
                System.out.println("System Closed");
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

    public static void closeApplication() {
        // Close all open JavaFX stages
        Platform.exit();
        // Forcefully terminate the JVM (optional)
        System.exit(0);
    }
}
