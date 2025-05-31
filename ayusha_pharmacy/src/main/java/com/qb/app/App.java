package com.qb.app;

import com.qb.app.model.ControllerClose;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;
    private static Object currentController; // Store current controller

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        scene = new Scene(loadFXML("sytemLogin"));
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(null);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        // Call close() on previous controller if applicable
        if (currentController instanceof ControllerClose controllerClose) {
            System.out.println("instanceof ControllerClose: Going to trigger close method.");
            controllerClose.close();
        } else {
            System.out.println("Not instanceof ControllerClose");
        }

        // Load new FXML
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();

        // Save the new controller
        currentController = fxmlLoader.getController();

        // Load additional stylesheets if needed
        if (fxml.equals("panelAdmin")) {
            scene.getStylesheets().add(App.class.getResource("/com/qb/app/css/annualSaleChartDesign.css").toExternalForm());
            scene.getStylesheets().add(App.class.getResource("/com/qb/app/css/adminStyle.css").toExternalForm());
        }

        scene.setRoot(root);
        primaryStage.sizeToScene();
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(null);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        currentController = fxmlLoader.getController(); // Store the initial controller
        return root;
    }

    public static void main(String[] args) {
        launch();
    }
}
