package com.qb.app.controllers;

import com.qb.app.App;
import com.qb.app.model.InterfaceAction;
import com.qb.app.model.InterfaceMortion;
import com.qb.app.model.SVGIconGroup;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class SytemLoginController implements Initializable {

    //    <editor-fold desc="FXML init component" defaultstate="collapsed">
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnExit;
    @FXML
    private Group iconExit;
    @FXML
    private AnchorPane root;
    @FXML
    private Rectangle quantumBlazeIcon;
    @FXML
    private Group iconUser;
    @FXML
    private Label loginMessage;
    //    </editor-fold>

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnLogin.setDisable(true);
        setMouseEvent();
        setInitialState();
        setQBImage();
        tfUsername.addEventHandler(KeyEvent.KEY_RELEASED, (e) -> {
            if (!tfUsername.getText().isEmpty()) {
                btnLogin.setDisable(false);
            } else {
                btnLogin.setDisable(true);
            }
        });
    }

    private static class Employee {

        String username;
        String password; // In real app, this would be encrypted
        String name;
        String role;
        String status;

        public Employee(String username, String password, String name, String role, String status) {
            this.username = username;
            this.password = password;
            this.name = name;
            this.role = role;
            this.status = status;
        }

        // Getters
        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        public String getStatus() {
            return status;
        }
    }

    // Mock database
    private static final Employee[] DEMO_EMPLOYEES = {
        new Employee("admin", "admin123", "Admin User", "admin", "Active"),
        new Employee("cashier", "cashier123", "Cashier User", "cashier", "Active"),
        new Employee("developer", "dev123", "Developer User", "developer", "Active"),
        new Employee("inactive", "inactive123", "Inactive User", "cashier", "Inactive")
    };

    @FXML
    private void handleSystemLogin(ActionEvent event) {
        if (event.getSource() == btnLogin) {
            systemLogin();
        } else if (event.getSource() == btnExit) {
            InterfaceAction.closeWindow(btnExit);
        }
    }

    public static Employee currentUser;

    private void systemLogin() {
        String username = tfUsername.getText();
        String password = tfPassword.getText();

        // Find user in demo data
        Employee emp = null;
        for (Employee employee : DEMO_EMPLOYEES) {
            if (employee.getUsername().equals(username)) {
                emp = employee;
                break;
            }
        }

        if (emp == null) {
            displayLoginMessage("No user found with this username", false);
            return;
        }

        // Check password (in real app, use proper password encryption)
        if (!emp.getPassword().equals(password)) {
            displayLoginMessage("Incorrect Password", false);
            return;
        }

        // Check status
        if (!emp.getStatus().equals("Active")) {
            displayLoginMessage("Access Denied - Account Inactive", false);
            return;
        }

        // Login successful
        currentUser = emp;
        displayLoginMessage("Login successful. Welcome " + emp.getRole() + ": " + emp.getName(), true);

        try {
            if (emp.getUsername().equals("cashier")) {
                App.setRoot("panelCashier");
            } else if (emp.getUsername().equals("admin")) {
                App.setRoot("adminVerification");
            }
        } catch (IOException e) {
            displayLoginMessage("Error: " + e.getMessage(), false);
        }

    }

    private void setInitialState() {
        setIcons();
        Rectangle clip = new Rectangle(root.getPrefWidth(), root.getPrefHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        root.setClip(clip);
    }

    private void setIcons() {
        iconUser.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/users-solid.svg"));
        iconExit.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/exit-solid.svg"));
    }

    private void setMouseEvent() {
        InterfaceMortion interfaceMortion = new InterfaceMortion();
        interfaceMortion.enableDrag(root);
    }

    private void setQBImage() {
        Image image = new Image(getClass().getResource("/com/qb/app/assets/images/logo.png").toExternalForm());
        quantumBlazeIcon.setFill(new ImagePattern(image));
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            systemLogin();
        }
    }

    private void displayLoginMessage(String message, boolean action) {
        if (action) {
            loginMessage.setStyle("-fx-text-fill: #0D9F00;"); // Green
        } else {
            loginMessage.setStyle("-fx-text-fill: #FF3333;"); // Red
        }
        // Set professional message
        loginMessage.setText(message);

        // Schedule message clearance
        PauseTransition delay = new PauseTransition(Duration.seconds(10));
        delay.setOnFinished(event -> loginMessage.setText(""));
        delay.play();
    }
}
