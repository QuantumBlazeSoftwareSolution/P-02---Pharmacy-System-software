/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers;

import com.qb.app.model.SVGIconGroup;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author Vihanga
 */
public class Supply_company_overviewController implements Initializable {

    @FXML
    private PieChart conpanyOrverviewChart;
    @FXML
    private BarChart<String, Number> supplyHistoryBarchart;
    @FXML
    private Group iconCompanyOrverview;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setIcon();
        loadPieChartData();
        loadBarChartData();
    } 
    
    private void setIcon() {
        iconCompanyOrverview.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
    }
    
    private void loadPieChartData() {
        // Create data for the pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Product A", 50),
            new PieChart.Data("Product B", 80),
            new PieChart.Data("Product C", 30),
            new PieChart.Data("Product D", 100)
        );

        // Add data to PieChart
        conpanyOrverviewChart.setData(pieChartData);

        // Optional: Set a title
        conpanyOrverviewChart.setTitle("Company Orverview");
    }
    
    private void loadBarChartData() {
        // Create series for 2024
        XYChart.Series<String, Number> series2024 = new XYChart.Series<>();
        series2024.setName("2024");
        series2024.getData().add(new XYChart.Data<>("January", 150));
        series2024.getData().add(new XYChart.Data<>("February", 120));
        series2024.getData().add(new XYChart.Data<>("March", 200));
        series2024.getData().add(new XYChart.Data<>("April", 180));
        series2024.getData().add(new XYChart.Data<>("May", 170));
        series2024.getData().add(new XYChart.Data<>("June", 150));
        series2024.getData().add(new XYChart.Data<>("July", 120));
        series2024.getData().add(new XYChart.Data<>("Augest", 200));
        series2024.getData().add(new XYChart.Data<>("Septhember", 180));
        series2024.getData().add(new XYChart.Data<>("Octomber", 170));
        series2024.getData().add(new XYChart.Data<>("November", 180));
        series2024.getData().add(new XYChart.Data<>("December", 170));


        
        // Create series for 2025
        XYChart.Series<String, Number> series2025 = new XYChart.Series<>();
        series2025.setName("2025");
        series2025.getData().add(new XYChart.Data<>("January", 180));
        series2025.getData().add(new XYChart.Data<>("February", 140));
        series2025.getData().add(new XYChart.Data<>("March", 220));
        series2025.getData().add(new XYChart.Data<>("April", 190));
        series2025.getData().add(new XYChart.Data<>("May", 200));
        series2025.getData().add(new XYChart.Data<>("June", 150));
        series2025.getData().add(new XYChart.Data<>("July", 120));
        series2025.getData().add(new XYChart.Data<>("Augest", 180));
        series2025.getData().add(new XYChart.Data<>("Septhember", 120));
        series2025.getData().add(new XYChart.Data<>("Octomber", 170));
        series2025.getData().add(new XYChart.Data<>("November", 180));
        series2025.getData().add(new XYChart.Data<>("December", 130));


        // Add data to chart
        supplyHistoryBarchart.getData().addAll(series2024, series2025);
    }
    
    
    
    
}
