
package com.qb.app.controllers;

import com.qb.app.model.DefaultAPI;
import com.qb.app.model.SVGIconGroup;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;


public class Inventory_stock_adjustmentController implements Initializable {

    @FXML
    private Group StockAdjustmentIcon;
    @FXML
    private ScrollPane stockAdjustmentTableScrollContainer;
    @FXML
    private VBox stockAdjustmentTableBody;
    @FXML
    private ScrollBar stockAdjustmentTableScroller;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DefaultAPI.bindTableScroll(stockAdjustmentTableScroller, stockAdjustmentTableScrollContainer, stockAdjustmentTableBody);
          StockAdjustmentIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
    }    
    
}
