package com.qb.app.controllers;

import com.qb.app.App;
import com.qb.app.model.InterfaceAction;
import com.qb.app.model.InterfaceMortion;
import com.qb.app.model.PasswordEncryption;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Employee;
import com.qb.app.session.ApplicationSession;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.IOException;
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
import static com.qb.app.model.JPATransaction.runInTransaction;
import com.qb.app.model.entity.Session;
import java.time.LocalDate;
import java.util.Date;
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
    //    </editor-fold>
    @FXML
    private Label loginMessage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnLogin.setDisable(true);
        setMouseEvent();
        setInitialState();
        setQBImage();
        Thread thread = new Thread(() -> {
            loadORM();
        });
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void handleSystemLogin(ActionEvent event) {
        if (event.getSource() == btnLogin) {
            systemLogin();
        } else if (event.getSource() == btnExit) {
            InterfaceAction.closeWindow(btnExit);
        }
    }

    private void systemLogin() {
        runInTransaction((EntityManager em) -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
            Root<Employee> employeeRoot = cq.from(Employee.class);

            // Filter by username
            Predicate usernamePredicate = cb.equal(employeeRoot.get("username"), tfUsername.getText());
            cq.where(usernamePredicate);

            // Execute query
            TypedQuery<Employee> query = em.createQuery(cq);
            Employee emp = null;

            try {
                emp = query.getSingleResult();
                ApplicationSession.setEmployee(emp); // save in session
                checkSession();
            } catch (NoResultException e) {
                // No user found
                emp = null;
            }

            if (emp == null) {
                displayLoginMessage("No user found with this username", false);
                return;
            }

            String enteredPassword = tfPassword.getText();

            if (PasswordEncryption.verifyPassword(emp.getPassword(), enteredPassword)) {
                String role = emp.getEmployeeRoleId().getRole().toLowerCase(); // Assuming employeeRoleId is the FK
                displayLoginMessage("Login successful. Welcome " + role + ": " + emp.getName(), false);
                String status = emp.getEmployeeStatusId().getStatus();

                if (status.equals("Active")) {
                    try {
                        switch (role) {
                            case "admin" ->
                                App.setRoot("adminVerification");
                            case "cashier" ->
                                App.setRoot("panelCashier");
                            case "developer" ->
                                App.setRoot("panelDeveloper");
                            default ->
                                System.out.println("Unknown role: " + role);
                        }
                    } catch (IOException e) {
                        System.out.println("Navigation error: " + e.getMessage());
                    }
                } else {
                    displayLoginMessage("Access Denied", false);
                }
            } else {
                displayLoginMessage("Incorrect Password", false);
            }
        });
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

    private void loadORM() {
        runInTransaction((em) -> {
            System.out.println("ORM is Loaded");
            btnLogin.setDisable(false);
        });
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

    private void checkSession() {
        runInTransaction((em) -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            // Check for existing session for today
            CriteriaQuery<Session> sessionCq = cb.createQuery(Session.class);
            Root<Session> sessionRoot = sessionCq.from(Session.class);

            // Create predicates for employee match and today's date
            LocalDate today = LocalDate.now();
            Predicate employeePredicate = cb.equal(sessionRoot.get("employeeId"), ApplicationSession.getEmployee());
            Predicate datePredicate = cb.equal(
                    cb.function("DATE", Date.class, sessionRoot.get("dayInTime")),
                    java.sql.Date.valueOf(today)
            );

            sessionCq.where(cb.and(employeePredicate, datePredicate));

            try {
                Session activeSession = em.createQuery(sessionCq).getSingleResult();
                ApplicationSession.setSession(activeSession);
            } catch (NoResultException e) {
            }
        });
    }
}
