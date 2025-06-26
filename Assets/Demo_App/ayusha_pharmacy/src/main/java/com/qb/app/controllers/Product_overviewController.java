/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author Vihanga
 */
public class Product_overviewController implements Initializable {

    @FXML
    private BarChart<String, Number> productSalrChart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        loadChartData();
        // TODO
    } 
    
     private void loadChartData() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Product Sales");

        // Add data to the series (Category, Value)
        series.getData().add(new XYChart.Data<>("Product A", 50));
        series.getData().add(new XYChart.Data<>("Product B", 80));
        series.getData().add(new XYChart.Data<>("Product C", 30));
        series.getData().add(new XYChart.Data<>("Product D", 100));
        series.getData().add(new XYChart.Data<>("Product E", 70));
        series.getData().add(new XYChart.Data<>("Product F", 80));
        series.getData().add(new XYChart.Data<>("Product G", 40));
        series.getData().add(new XYChart.Data<>("Product H", 90));
        series.getData().add(new XYChart.Data<>("Product I", 50));
        series.getData().add(new XYChart.Data<>("Product J", 80));
        series.getData().add(new XYChart.Data<>("Product K", 60));
        series.getData().add(new XYChart.Data<>("Product L", 100));

        // Clear previous data and add the new series
        productSalrChart.getData().clear();
        productSalrChart.getData().add(series);
    }
    
}
