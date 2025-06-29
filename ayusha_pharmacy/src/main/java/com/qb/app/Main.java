package com.qb.app;

import com.qb.app.model.AppLogger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger logger = AppLogger.getLogger();

    public static void main(String[] args) {
        App.main(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        
        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            logger.log(Level.SEVERE, "Uncaught Exception in Main Thread", throwable);
        });

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            logger.log(Level.SEVERE, "Uncaught Exception in Thread: " + thread.getName(), throwable);
        });

        // Example scene
        stage.setTitle("Demo App");
        stage.setScene(new Scene(new Label("Hello")));
        stage.show();
    }
}
