package com.qb.app.controllers;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class DeveloperDashboardController implements Initializable {

    @FXML
    private PieChart pieChart;
    @FXML
    private BarChart<String, Number> barChart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        XYChart.Series<String, Number> barChartSeries = new XYChart.Series<>();
        barChartSeries.setName("2025");
        barChartSeries.getData().add(new XYChart.Data<>("Employees", 60));
        barChartSeries.getData().add(new XYChart.Data<>("Cashiers", 70));
        barChartSeries.getData().add(new XYChart.Data<>("Companies", 50));
        barChartSeries.getData().add(new XYChart.Data<>("Suppliers", 30));
        barChartSeries.getData().add(new XYChart.Data<>("GRNs", 60));
        barChartSeries.getData().add(new XYChart.Data<>("Brands", 40));
        barChartSeries.getData().add(new XYChart.Data<>("Products", 80));
        barChartSeries.getData().add(new XYChart.Data<>("Invoices", 50));

        barChart.getData().add(barChartSeries);

        ObservableList<PieChart.Data> systemChartData = FXCollections.observableArrayList(
                new PieChart.Data("Employees", getRandomValue()),
                new PieChart.Data("Cashiers", getRandomValue()),
                new PieChart.Data("Products", getRandomValue()),
                new PieChart.Data("Brands", getRandomValue()),
                new PieChart.Data("Pending Creditors", getRandomValue())
        );

        pieChart.getData().addAll(systemChartData);

    }

    private int getRandomValue() {
        Random random = new Random();
        return random.nextInt(100) + 1;
    }

}
