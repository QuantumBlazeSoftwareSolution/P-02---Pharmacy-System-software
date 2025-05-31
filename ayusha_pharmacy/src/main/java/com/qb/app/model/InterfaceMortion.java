package com.qb.app.model;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class InterfaceMortion {

    private double xOffset = 0;
    private double yOffset = 0;

    // Generic method to enforce type safety
    public <T extends Parent> void enableDrag(T root) {
        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX(); // Scene-based coordinates
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset); // Use screen coordinates
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
