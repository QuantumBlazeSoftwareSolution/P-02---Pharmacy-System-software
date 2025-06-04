package com.qb.app.controllers;

import com.qb.app.model.ControllerClose;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.EntityManagerCallBack;
import com.qb.app.model.PasswordEncryption;
import com.qb.app.model.entity.Session;
import com.qb.app.session.ApplicationSession;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import static com.qb.app.model.JPATransaction.runInTransaction;
import com.qb.app.session.ApplicationControllers;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class CashierSessionController implements Initializable, ControllerClose {

    //    <editor-fold desc="FXML init component" defaultstate="collapsed">
    @FXML
    private Button signInMessage;
    @FXML
    private Button signOffMessage;
    @FXML
    private TextField tfSignInUsername;
    @FXML
    private PasswordField tfSignInPassword;
    @FXML
    private TextField tfSignInPettyCash;
    @FXML
    private TextField tfSignOffUsername;
    @FXML
    private PasswordField tfSignOffPassword;
    @FXML
    private TextField tfSignOffCollection;
    @FXML
    private Button btnSignIn;
    @FXML
    private Button btnSignOff;
    @FXML
    private Label sessionHours;
    @FXML
    private Label sessionMinutes;
    @FXML
    private Label sessionAMPM;
    @FXML
    private AnchorPane root;
    //    </editor-fold>

    private static boolean isSignIn;
    private ScheduledExecutorService scheduler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getSessionDetails();
        sessionTimer();
    }

    private void getSessionDetails() {
        runInTransaction((EntityManager em) -> {
            CriteriaQuery<Session> criteriaQuery = getSessionQuery(em);

            try {
                Session sessionsToday = em.createQuery(criteriaQuery).getSingleResult();

                if (sessionsToday.getStatus().equals("OFF")) { // If record status was OFF
                    signInMessage.setText("Day Completed.");
                    signOffMessage.setText("Day Completed.");
                } else { // If record status was ON
                    signInMessage.setText("You're already signed in for today");
                    signOffMessage.setText("Waiting for Sign OFF.");
                    isSignIn = true;
                }
            } catch (Exception e) {
                signInMessage.setText("Waiting for Sign In.");
                signOffMessage.setText("Sign OFF is not activated.");
            }
        });
    }

    @FXML
    private void handleActionEvent(ActionEvent event) {
        if (event.getSource() == btnSignIn) {
            sessionSignIn();
        } else if (event.getSource() == btnSignOff) {
            sessionSignOff();
        }
    }

    private void sessionSignIn() {
        // check if there have a Sign IN for today
        if (!isSignIn) { // if not have a record for today
            if (checkSignInValidation()) {
                if (ApplicationSession.getEmployee().getUsername().equals(tfSignInUsername.getText()) && PasswordEncryption.verifyPassword(ApplicationSession.getEmployee().getPassword(), tfSignInPassword.getText())) { // check if the employee is matching to this username and password
                    if (isTodayAvailable()) { // check if today is available for sign in

                        Session signInSession = new Session(); // entity 'session'
                        signInSession.setDayInTime(new Date());
                        signInSession.setPettyCash(Double.parseDouble(tfSignInPettyCash.getText()));
                        signInSession.setEmployeeId(ApplicationSession.getEmployee());
                        signInSession.setStatus("ON");

                        saveNewSignInSession(signInSession);
                        ApplicationSession.setSession(signInSession);
                        isSignIn = true;
                        signInMessage.setText("Successfuly sign in for today.");
                        signOffMessage.setText("Waiting for sign off.");
                        MessageTransition("Sign in completed.", signInMessage);
                        cleanSignIn();
                    } else {
                        signInMessage.setText("Session active: You cannot sign in multiple times per day.");
                        MessageTransition("You're already signed in for today", signInMessage);
                    }

                    CustomAlert.showStyledAlert(root, "Successfuly Sign In for today.", Alert.AlertType.INFORMATION);
                } else {
                    CustomAlert.showStyledAlert(root, "Credentials Mismatch - Please check your username/password", Alert.AlertType.WARNING);
                }
            }
        } else {
            CustomAlert.showStyledAlert(root, "You're already signed in for today", Alert.AlertType.WARNING);
        }
    }

    private void sessionSignOff() {
        if (isSignIn) { // if have a record for today
            if (checkSignOffValidation()) {
                if (ApplicationSession.getEmployee().getUsername().equals(tfSignOffUsername.getText()) && PasswordEncryption.verifyPassword(ApplicationSession.getEmployee().getPassword(), tfSignOffPassword.getText())) { // check if the employee is matching to this username and password
                    if (!isTodayAvailable()) { // check if today is not available for sign in (it means it's available for sign off)
                        runInTransaction(em -> {
                            CriteriaQuery<Session> criteriaQuery = getSessionQuery(em);
                            try {
                                Session sessionToday = em.createQuery(criteriaQuery).getSingleResult();

                                if (sessionToday.getStatus().equals("ON")) { // If record status was OFF
                                    sessionToday.setDayOutTime(new Date());
                                    sessionToday.setCollection(Double.valueOf(tfSignOffCollection.getText()));
                                    sessionToday.setStatus("OFF");
                                    mergeSignOffSession(sessionToday);
                                    ApplicationSession.setSession(sessionToday);

                                    signInMessage.setText("Day completed.");
                                    signOffMessage.setText("Day completed.");
                                    CustomAlert.showStyledAlert(root, "Successfuly Sign Off for today.", Alert.AlertType.INFORMATION);
                                    cleanSignOff();

                                    PauseTransition delay = new PauseTransition(Duration.seconds(2));
                                    delay.setOnFinished(event -> {
                                        ApplicationControllers.getPanelCashierController().changePanel(
                                                "/com/qb/app/cashierDashboard.fxml",
                                                "Dashboard"
                                        );
                                    });
                                    delay.play();
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(e.getMessage());
                            }
                        });
                    } else {
                        signInMessage.setText("Cannot sign off: No active session detected. Sign in first.");
                        MessageTransition("Sign OFF is not activated.", signOffMessage);
                    }
                } else {
                    CustomAlert.showStyledAlert(root, "Credentials Mismatch - Please check your username/password", Alert.AlertType.WARNING);
                }
            }
        } else {
            CustomAlert.showStyledAlert(root, "You're not signed in for today, Please sign in to activate sign off option", Alert.AlertType.WARNING);
        }
    }

    private void MessageTransition(String text, Button btn) {
        // after 5 second i want to change that message to belowe message
        PauseTransition delay = new PauseTransition(Duration.seconds(6));
        delay.setOnFinished(event -> {
            btn.setText(text);
        });
        delay.play();
    }

    private void saveNewSignInSession(Session signInSession) {
        runInTransaction((EntityManagerCallBack) em -> em.persist(signInSession));
    }

    private CriteriaQuery<Session> getSessionQuery(EntityManager em) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Session> criteriaQuery = criteriaBuilder.createQuery(Session.class);
        Root<Session> sessionTable = criteriaQuery.from(Session.class);

        LocalDate today = LocalDate.now();

        // Build Predicate (where DATE(day_in_time) = today)
        Predicate predicate = criteriaBuilder.equal(
                criteriaBuilder.function("DATE", Date.class, sessionTable.get("dayInTime")),
                java.sql.Date.valueOf(today)
        );

        return criteriaQuery.select(sessionTable).where(predicate);
    }

    private boolean isTodayAvailable() {
        return runInTransaction(em -> {
            CriteriaQuery<Session> criteriaQuery = getSessionQuery(em);
            try {
                em.createQuery(criteriaQuery).getSingleResult();
                return false; // Already signed in
            } catch (Exception e) {
                return true; // Not signed in yet
            }
        });
    }

    private boolean checkSignInValidation() {
        if (tfSignInUsername.getText().isEmpty() || tfSignInUsername.getText().equals("")) {
            CustomAlert.showStyledAlert(root, "Username required - Please enter your credentials", Alert.AlertType.WARNING);
        } else if (tfSignInPassword.getText().isEmpty() || tfSignInPassword.getText().equals("")) {
            CustomAlert.showStyledAlert(root, "Password required for security verification", Alert.AlertType.WARNING);
        } else if (tfSignInPettyCash.getText().isEmpty() || tfSignInPettyCash.getText().equals("")) {
            CustomAlert.showStyledAlert(root, "Please specify the opening cash amount", Alert.AlertType.WARNING);
        } else if (!DefaultAPI.isDouble(tfSignInPettyCash.getText())) {
            CustomAlert.showStyledAlert(root, "Please enter a valid cash amount (e.g., 250.00)", Alert.AlertType.WARNING);
        } else {
            return true;
        }
        return false;
    }

    private boolean checkSignOffValidation() {
        if (tfSignOffUsername.getText().isEmpty() || tfSignOffUsername.getText().equals("")) {
            CustomAlert.showStyledAlert(root, "Username required - Please enter your credentials", Alert.AlertType.WARNING);
        } else if (tfSignOffPassword.getText().isEmpty() || tfSignOffPassword.getText().equals("")) {
            CustomAlert.showStyledAlert(root, "Password required for security verification", Alert.AlertType.WARNING);
        } else if (tfSignOffCollection.getText().isEmpty() || tfSignOffCollection.getText().equals("")) {
            CustomAlert.showStyledAlert(root, "Please specify the closing cash amount", Alert.AlertType.WARNING);
        } else if (!DefaultAPI.isDouble(tfSignOffCollection.getText())) {
            CustomAlert.showStyledAlert(root, "Please enter a valid cash amount (e.g., 250.00)", Alert.AlertType.WARNING);
        } else {
            return true;
        }
        return false;
    }

    private void mergeSignOffSession(Session sessionToday) {
        runInTransaction((EntityManagerCallBack) em -> em.merge(sessionToday));
    }

    private void cleanSignIn() {
        tfSignInUsername.setText("");
        tfSignInPassword.setText("");
        tfSignInPettyCash.setText("");
    }

    private void cleanSignOff() {
        tfSignOffUsername.setText("");
        tfSignOffPassword.setText("");
        tfSignOffCollection.setText("");
    }

    private void sessionTimer() {
        scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            System.out.println("Task executed at: " + new java.util.Date());
            LocalTime now = LocalTime.now();
            Platform.runLater(() -> {
                sessionHours.setText(String.format("%02d", now.getHour()));
                sessionMinutes.setText(String.format("%02d", now.getMinute()));
                sessionAMPM.setText(now.getHour() < 12 ? "AM" : "PM");
            });
        };

        // Start after 1 minute, repeat every 1 minute
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void close() {
        if (scheduler != null) {
            scheduler.shutdown(); // Disable new tasks from being submitted
            try {
                // Wait a while for existing tasks to terminate
                if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow(); // Cancel currently executing tasks
                }
            } catch (InterruptedException e) {
                // (Re-)Cancel if current thread also interrupted
                scheduler.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        }
    }

    @FXML
    private void handleKeyReleasedSignIn(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sessionSignIn();
        }
    }

    @FXML
    private void handleKeyReleasedSignOff(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sessionSignOff();
        }
    }

}
