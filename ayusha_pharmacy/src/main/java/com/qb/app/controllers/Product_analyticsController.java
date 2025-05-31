package com.qb.app.controllers;

import com.qb.app.model.DefaultAPI;
import com.qb.app.model.SVGIconGroup;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class Product_analyticsController implements Initializable {

    @FXML
    private Group iconPage;
    @FXML
    private ComboBox<?> cbBrand1;
    @FXML
    private ComboBox<?> cbBrand;
    @FXML
    private ScrollPane tableScrollContainer;
    @FXML
    private VBox tableBody;
    @FXML
    private ScrollBar tableScroller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        DefaultAPI.bindTableScroll(tableScroller, tableScrollContainer, tableBody);
        iconPage.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        
        for (int i = 0; i < 20; i++) {
            try {
                Random random = new Random();

                // Load the invoiceItem.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/qb/app/fxmlComponent/productAnalyticsItem.fxml"));
                Node invoiceItem = loader.load(); // Load the node

                // Add the invoice item to the VBox
                tableBody.getChildren().add(invoiceItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }    
    
}
