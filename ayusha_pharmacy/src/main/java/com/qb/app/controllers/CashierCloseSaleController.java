package com.qb.app.controllers;

import com.qb.app.model.ControllerClose;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.PasswordEncryption;
import com.qb.app.model.entity.CloseSale;
import com.qb.app.session.ApplicationControllers;
import com.qb.app.session.ApplicationSession;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class CashierCloseSaleController implements Initializable, ControllerClose {

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TextField tfCashier;
    @FXML
    private TextField tfDayIn;
    @FXML
    private TextField tfDayOff;
    @FXML
    private TextField tf5000Qty;
    @FXML
    private TextField tf1000Qty;
    @FXML
    private TextField tf1000Value;
    @FXML
    private TextField tf500Qty;
    @FXML
    private TextField tf500Value;
    @FXML
    private TextField tf100Qty;
    @FXML
    private TextField tf100Value;
    @FXML
    private TextField tf50Qty;
    @FXML
    private TextField tf50Value;
    @FXML
    private TextField tf20Qty;
    @FXML
    private TextField tf20Value;
    @FXML
    private TextField tf10Qty;
    @FXML
    private TextField tf10Value;
    @FXML
    private TextField tf5Qty;
    @FXML
    private TextField tf5Value;
    @FXML
    private TextField tfCollection;
    @FXML
    private Button btnAction;
    @FXML
    private TextField tf5000Value;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializingTextFields();
    }

    private void initializingTextFields() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        tf5000Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf1000Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf500Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf100Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf50Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf20Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf10Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf5Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        if (ApplicationSession.getSession() != null) {
            tfCashier.setText(ApplicationSession.getSession().getEmployeeId().getName());
            tfDayIn.setText(dateFormat.format(ApplicationSession.getSession().getDayInTime()));
            tfDayOff.setText(dateFormat.format(ApplicationSession.getSession().getDayOutTime()));
        }
    }

    @FXML
    private void handleKeyEvent(KeyEvent event) {
        if (event.getSource() == tf5000Qty) {
            System.out.println("5000 pressed");
            setCashValue(tf5000Qty, tf5000Value, 5000);
        }

        if (event.getSource() == tf1000Qty) {
            setCashValue(tf1000Qty, tf1000Value, 1000);
        }

        if (event.getSource() == tf500Qty) {
            setCashValue(tf500Qty, tf500Value, 500);
        }

        if (event.getSource() == tf100Qty) {
            setCashValue(tf100Qty, tf100Value, 100);
        }

        if (event.getSource() == tf50Qty) {
            setCashValue(tf50Qty, tf50Value, 50);
        }

        if (event.getSource() == tf20Qty) {
            setCashValue(tf20Qty, tf20Value, 20);
        }

        if (event.getSource() == tf10Qty) {
            setCashValue(tf10Qty, tf10Value, 10);
        }

        if (event.getSource() == tf5Qty) {
            setCashValue(tf5Qty, tf5Value, 5);
        }

        double totalCollection = calculateTotalCollection();
        tfCollection.setText(String.format("Rs. %, .2f", totalCollection));
    }

    private void setCashValue(TextField getter, TextField setter, int value) {
        if (!getter.getText().isEmpty()) {
            int cash = Integer.parseInt(getter.getText());
            setter.setText(String.format("Rs. %,d.00", (cash * value)));
        }
    }

    private double calculateTotalCollection() {
        System.out.println("calculateTotalCollection() method was triggered");
        double total = 0;

        // Add each denomination's total value
        total += getDenominationTotal(tf5000Qty, 5000);
        total += getDenominationTotal(tf1000Qty, 1000);
        total += getDenominationTotal(tf500Qty, 500);
        total += getDenominationTotal(tf100Qty, 100);
        total += getDenominationTotal(tf50Qty, 50);
        total += getDenominationTotal(tf20Qty, 20);
        total += getDenominationTotal(tf10Qty, 10);
        total += getDenominationTotal(tf5Qty, 5);
        return total;
    }

    private double getDenominationTotal(TextField quantityField, int denominationValue) {
        System.out.println("getDenominationTotal() method was triggered");
        try {
            if (!quantityField.getText().isEmpty()) {
                int quantity = Integer.parseInt(quantityField.getText());
                return quantity * denominationValue;
            } else {
                System.out.println("It's empty");
            }
        } catch (NumberFormatException e) {
            // Handle invalid number format (optional)
            e.printStackTrace();
        }
        return 0;
    }

    @FXML
    private void ActionEvent(ActionEvent event) {
        if (event.getSource() == btnAction) {
            if (tfUsername.getText().isEmpty()) {
                CustomAlert.showStyledAlert(root, "Username field cannot be empty. Please enter your username.", "Username Required", Alert.AlertType.WARNING);
            } else if (tfPassword.getText().isEmpty()) {
                CustomAlert.showStyledAlert(root, "Password field cannot be empty. Please enter your password.", "Password Required", Alert.AlertType.WARNING);
            } else {
                if (tfUsername.getText().equals(ApplicationSession.getEmployee().getUsername()) && PasswordEncryption.verifyPassword(ApplicationSession.getSession().getEmployeeId().getPassword(), tfPassword.getText())) {
                    JPATransaction.runInTransaction((em) -> {
                        CloseSale closeSale = new CloseSale();
                        closeSale.setC5(Integer.parseInt(tf5Qty.getText()));
                        closeSale.setC10(Integer.parseInt(tf10Qty.getText()));
                        closeSale.setC20(Integer.parseInt(tf20Qty.getText()));
                        closeSale.setC50(Integer.parseInt(tf50Qty.getText()));
                        closeSale.setC100(Integer.parseInt(tf100Qty.getText()));
                        closeSale.setC500(Integer.parseInt(tf500Qty.getText()));
                        closeSale.setC1000(Integer.parseInt(tf1000Qty.getText()));
                        closeSale.setC5000(Integer.parseInt(tf5000Qty.getText()));
                        closeSale.setDateTime(new Date());
                        closeSale.setSessionId(ApplicationSession.getSession());
                        em.persist(closeSale);

                        clearCloseSale();
                        CustomAlert.showStyledAlert(root, "The sale has been successfully closed.", "Sale Closed Successfully", Alert.AlertType.INFORMATION);
                        ApplicationControllers.getPanelCashierController().changePanel(
                                "/com/qb/app/cashierDashboard.fxml",
                                "Dashboard"
                        );
                    });
                } else {
                    CustomAlert.showStyledAlert(root, "The username or password you entered is incorrect. Please try again.", "Invalid Credentials", Alert.AlertType.WARNING);
                }
            }
        }
    }

    private void clearCloseSale() {
        tf5Qty.setText("");
        tf10Qty.setText("");
        tf20Qty.setText("");
        tf50Qty.setText("");
        tf100Qty.setText("");
        tf500Qty.setText("");
        tf1000Qty.setText("");
        tf5000Qty.setText("");
        tf5Value.setText("");
        tf10Value.setText("");
        tf20Value.setText("");
        tf50Value.setText("");
        tf100Value.setText("");
        tf500Value.setText("");
        tf1000Value.setText("");
        tf5000Value.setText("");
        tfUsername.setText("");
        tfPassword.setText("");
        tfCollection.setText("");
        tfDayIn.setText("");
        tfDayOff.setText("");
        tfCashier.setText("");
    }

    @Override
    public void close() {
        
    }

}
