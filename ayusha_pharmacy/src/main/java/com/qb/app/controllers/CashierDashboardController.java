/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers;

import com.qb.app.model.ControllerClose;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Vihanga
 */
public class CashierDashboardController implements Initializable, ControllerClose {

    // <editor-fold desc="FXML init component" defaultstate="collapsed">
    @FXML
    private Label labelYear;
    @FXML
    private Label labelMonth;
    @FXML
    private Label labelDate;
    // </editor-fold>

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDate();
    }

    @Override
    public void close() {

    }

    private void setDate() {
        LocalDate currentDate = LocalDate.now();

        // Set year (full 4-digit format)
        labelYear.setText(String.valueOf(currentDate.getYear()));

        // Set month as 3-letter abbreviation (Jan, Feb, etc.)
        Month month = currentDate.getMonth();
        labelMonth.setText(month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));

        // Set date (day of month)
        labelDate.setText(String.valueOf(currentDate.getDayOfMonth()));
    }

}
