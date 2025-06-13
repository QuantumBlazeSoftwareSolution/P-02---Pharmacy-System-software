/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers.report;

import com.qb.app.model.ComboBoxUtils;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Supplier;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Vihanga
 */
public class ReportGRNController implements Initializable {

    @FXML
    private Group iconPage;
    @FXML
    private ScrollPane tableScrollContainer;
    @FXML
    private VBox tableBody;
    @FXML
    private ScrollBar tableScroller;
    @FXML
    private TextField tfItemName;
    @FXML
    private ComboBox<Supplier> cbSupplier;
    @FXML
    private TextField TFGrnId;
    @FXML
    private ComboBox<?> cbFilterBy;
    @FXML
    private Button btnLoadReport;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnVieweReport;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DefaultAPI.bindTableScroll(tableScroller, tableScrollContainer, tableBody);
        iconPage.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        
        LoadComboBox();

    }    
    
    private void LoadComboBox(){
            ComboBoxUtils.loadComboBoxValues(cbSupplier, Supplier.class, "name", Supplier::getName);
    }

    @FXML
    private void loadReportAcion(ActionEvent event) {
         if (event.getSource() == btnLoadReport) {
          loadGRN();
        }
    }
    
    private void loadGRN(){
        if(isEntriesValid()){
            loadDataToTable();
        }
        
    }
    
    private  void loadDataToTable(){
        
    }
    
    private boolean isEntriesValid() {
        if (cbSupplier.getValue() == null) {
//            displayWarningMessage("Please select the supplier", false);
            System.out.println("Please select the supplier");
            cbSupplier.requestFocus();
            return false;
        }
                if (cbFilterBy.getValue() == null) {
//            displayWarningMessage("Please select the supplier", false);
            System.out.println("Please select the filter By");
            cbFilterBy.requestFocus();
            return false;
        }
        
        if (TFGrnId.getText().isEmpty()) {
//            displayWarningMessage("Please enter the grn id", false);
            System.out.println("Please select the grn id");
            TFGrnId.requestFocus();
            return false;
        }
        return true;
    }

    @FXML
    private void refresh(ActionEvent event) {
         if (event.getSource() == btnRefresh) 
             loadGRN();
    }
      

}
