/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers;

import com.qb.app.model.ControllerClose;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.entity.CashWithdrawal;
import com.qb.app.model.entity.Invoice;
import com.qb.app.session.ApplicationSession;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Vihanga
 */
public class CashierWithdrawalController implements Initializable, ControllerClose {

    @FXML
    private AnchorPane root;
    @FXML
    private TextField tfAvailableCash;
    @FXML
    private TextField tfCashAmount;
    @FXML
    private TextField tfDateTime;
    @FXML
    private TextField tfReason;
    @FXML
    private Button btnWithrawal;

    private double availableCash;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getAvailableCash();
        tfCashAmount.setTextFormatter(DefaultAPI.createNumericTextFormatter());
    }

    @Override
    public void close() {
    }

    private void getAvailableCash() {
        double cashAmount = getCashAmount();
        double withdrawalAmount = getWithdrawalAmount();
        tfAvailableCash.setText(String.format("Rs. %, .2f", (cashAmount - withdrawalAmount)));
        this.availableCash = (cashAmount - withdrawalAmount);
    }

    private double getCashAmount() {
        return JPATransaction.runInTransaction((em) -> {
            double cashAmount = 0;
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Invoice> cQuery = cBuilder.createQuery(Invoice.class);
            Root<Invoice> invoiceTable = cQuery.from(Invoice.class);
            Predicate sessionPredicate = cBuilder.equal(invoiceTable.get("sessionId"), ApplicationSession.getSession());
            try {
                cQuery.where(sessionPredicate);
                List<Invoice> invoiceList = em.createQuery(cQuery).getResultList();
                for (Invoice invoice : invoiceList) {
                    cashAmount += invoice.getBillAmount() - invoice.getCreditAmount();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return cashAmount + ApplicationSession.getSession().getPettyCash();
        });
    }

    private double getWithdrawalAmount() {
        return JPATransaction.runInTransaction((em) -> {
            double withdrawalAmount = 0;
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<CashWithdrawal> cQuery = cBuilder.createQuery(CashWithdrawal.class);
            Root<CashWithdrawal> withdawalTable = cQuery.from(CashWithdrawal.class);
            Predicate sessionPredicate = cBuilder.equal(withdawalTable.get("sessionId"), ApplicationSession.getSession());
            try {
                cQuery.where(sessionPredicate);
                List<CashWithdrawal> withdrawalList = em.createQuery(cQuery).getResultList();
                for (CashWithdrawal withdrawal : withdrawalList) {
                    withdrawalAmount += withdrawal.getAmount();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return withdrawalAmount;
        });
    }

    @FXML
    private void handleActionEvent(ActionEvent event) {
        if (isDetailsValid()) {
            makeWithdrawal();
        }
    }

    private boolean isDetailsValid() {
        boolean status = false;
        if (tfCashAmount.getText().isEmpty()) {
            CustomAlert.showStyledAlert(root, "Withdrawal amount cannot be empty, Please enter withdrawal amount to get a withdrawal.", Alert.AlertType.WARNING);
        } else if (tfReason.getText().isEmpty()) {
            CustomAlert.showStyledAlert(root, "Withdrawal must have a reason.", Alert.AlertType.WARNING);
        } else {
            status = true;
        }

        return status;
    }

    private void makeWithdrawal() {
        JPATransaction.runInTransaction((em) -> {
            CashWithdrawal cashWithdrawal = new CashWithdrawal();
            cashWithdrawal.setAmount(Double.parseDouble(tfCashAmount.getText()));
            cashWithdrawal.setDateTime(new Date());
            cashWithdrawal.setReason(tfReason.getText());
            cashWithdrawal.setSessionId(ApplicationSession.getSession());
            em.persist(cashWithdrawal);
            CustomAlert.showStyledAlert(
                    root,
                    "Cash withdrawal completed.",
                    Alert.AlertType.INFORMATION
            );
        });
        clearWithdrawal();
    }

    private void clearWithdrawal() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        getAvailableCash();
        tfCashAmount.setText("");
        tfReason.setText("");
        tfDateTime.setText(dateFormat.format(new Date()));
    }
}
