
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


public class Inventory_damage_itemController implements Initializable {

    @FXML
    private Group damageItemIcom;
    @FXML
    private ScrollPane damageItemTableScrollContainer;
    @FXML
    private VBox damageItemTableBody;
    @FXML
    private ScrollBar damageItemTableScroller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
          DefaultAPI.bindTableScroll(damageItemTableScroller, damageItemTableScrollContainer, damageItemTableBody);
          damageItemIcom.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
    }    
    
}
