
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


public class Inventory_location_returnController implements Initializable {

    @FXML
    private Group locationReturnIcon;
    @FXML
    private ScrollPane locationReturnTableScrollContainer;
    @FXML
    private VBox locationReturnTableBody;
    @FXML
    private ScrollBar locationReturnTableScroller;

 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DefaultAPI.bindTableScroll(locationReturnTableScroller, locationReturnTableScrollContainer, locationReturnTableBody);
          locationReturnIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
    }    
    
}
