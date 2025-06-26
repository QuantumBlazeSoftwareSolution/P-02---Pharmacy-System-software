package com.qb.app.model;

import com.qb.app.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.Node;
import javafx.stage.*;
import javafx.geometry.Rectangle2D;
import java.io.IOException;
import java.util.function.Consumer;

public class PopUp {

    public enum PopupType {
        CENTERED_80_WIDTH, // First example (80% width, centered)
        FULL_WIDTH_FIXED_HEIGHT, // Second and third examples (full width, fixed height)
        CUSTOM  // Fully customizable
    }

    /**
     * Creates and shows a popup stage with common configurations
     *
     * @param fxmlPath Path to the FXML file
     * @param ownerNode Node that owns the popup (for modality)
     * @param rootScene Root scene for blur effect (can be null)
     * @param type Type of popup layout
     * @param controllerSetup Consumer to configure the controller (can be null)
     * @param <T> Type of the controller
     * @return The created Stage
     * @throws IOException If FXML loading fails
     */
    public static <T> Stage createPopup(String fxmlPath,
            Node ownerNode,
            Scene rootScene,
            PopupType type,
            Consumer<T> controllerSetup) throws IOException {

        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlPath));
        Parent root = loader.load();

        Stage popupStage = new Stage();
        popupStage.initOwner(ownerNode.getScene().getWindow());
        popupStage.initModality(Modality.APPLICATION_MODAL);

        // Apply blur effect to root scene if provided
        if (rootScene != null) {
            GaussianBlur blur = new GaussianBlur(10);
            rootScene.getRoot().setEffect(blur);
            popupStage.setOnHidden(e -> rootScene.getRoot().setEffect(null));
        }

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        Scene scene = new Scene(root);
        popupStage.setScene(scene);

        // Configure stage based on type
        switch (type) {
            case CENTERED_80_WIDTH:
                double eightyPercentWidth = bounds.getWidth() * 0.8;
                popupStage.setWidth(eightyPercentWidth);
                popupStage.setX((bounds.getWidth() - eightyPercentWidth) / 2);
                popupStage.setY((bounds.getHeight() - popupStage.getHeight()) / 2);
                break;

            case FULL_WIDTH_FIXED_HEIGHT:
                popupStage.setWidth(bounds.getWidth());
                popupStage.setX(0);
                popupStage.setHeight(600);
                popupStage.setY((bounds.getHeight() - popupStage.getHeight()) / 2);
                break;

            case CUSTOM:
                // No default configuration - user must configure manually
                break;
        }

        popupStage.initStyle(StageStyle.TRANSPARENT);

        // Configure controller if setup consumer provided
        if (controllerSetup != null) {
            T controller = loader.getController();
            controllerSetup.accept(controller);
        }

        return popupStage;
    }

    /**
     * Simplified version without root scene blur effect
     * @param <T>
     * @param fxmlPath
     * @param ownerNode
     * @param type
     * @param controllerSetup
     * @return 
     * @throws java.io.IOException 
     */
    public static <T> Stage createPopup(String fxmlPath,
            Node ownerNode,
            PopupType type,
            Consumer<T> controllerSetup) throws IOException {
        return createPopup(fxmlPath, ownerNode, null, type, controllerSetup);
    }

    /**
     * Shows a popup and waits for it to close
     * @param <T>
     * @param fxmlPath
     * @param ownerNode
     * @param rootScene
     * @param controllerSetup
     * @param type
     * @throws java.io.IOException
     */
    public static <T> void showPopupAndWait(String fxmlPath,
            Node ownerNode,
            Scene rootScene,
            PopupType type,
            Consumer<T> controllerSetup) throws IOException {
        Stage stage = createPopup(fxmlPath, ownerNode, rootScene, type, controllerSetup);
        stage.showAndWait();
    }

    /**
     * Simplified version for showing popup without root scene blur
     * @param <T>
     * @param fxmlPath
     * @param ownerNode
     * @param type
     * @param controllerSetup
     * @throws java.io.IOException
     */
    public static <T> void showPopupAndWait(String fxmlPath,
            Node ownerNode,
            PopupType type,
            Consumer<T> controllerSetup) throws IOException {
        showPopupAndWait(fxmlPath, ownerNode, null, type, controllerSetup);
    }
}
