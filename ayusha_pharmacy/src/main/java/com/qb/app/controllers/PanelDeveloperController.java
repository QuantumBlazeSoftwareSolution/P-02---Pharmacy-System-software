package com.qb.app.controllers;

import com.qb.app.model.InterfaceAction;
import com.qb.app.model.SVGIconGroup;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class PanelDeveloperController implements Initializable {

    @FXML
    private BorderPane mainBorderLayout;
    @FXML
    private BorderPane leftSideMenu;
    @FXML
    private Circle systemLogo;
    @FXML
    private Button btnDashboard;
    @FXML
    private Group iconDashboard;
    @FXML
    private Button btnExit;
    @FXML
    private Group iconExit;
    @FXML
    private BorderPane contentBorder;
    @FXML
    private Button btnOwnership;
    @FXML
    private HBox btnOwnershipTransferring;
    @FXML
    private HBox btnOwnershipManagement;
    @FXML
    private Button btnControlPanel;
    @FXML
    private Button btnBasics;
    @FXML
    private Group iconOwnershipManagement;
    @FXML
    private Group iconOwnershipTransferringSubMenu;
    @FXML
    private Group iconOwnershipManagementSubMenu;
    @FXML
    private Group iconControlPanel;
    @FXML
    private Group iconBasics;

    private boolean isMenuCollapsed = false;
    private Developer_top_panelController controller;
    @FXML
    private AnchorPane root;
    @FXML
    private VBox subMenuOwnership;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIcons();
        setInitialState();
    }

    @FXML
    private void handleActionButtons(ActionEvent event) {
        if (event.getSource() == btnExit) {
            InterfaceAction.closeWindow(root);
        } else if (event.getSource() == btnDashboard) {
            loadCenterPanel("developerDashboard");
        } else if (event.getSource() == btnOwnership) {
            subMenuToggle(subMenuOwnership);
            setSubMenuState(subMenuOwnership);
        } else if (event.getSource() == btnControlPanel) {
            loadCenterPanel("developerControlPanel");
        } else if (event.getSource() == btnBasics) {
            loadCenterPanel("developerBasics");
        }
    }

    @FXML
    private void handleSubMenuItems(MouseEvent event) {
        if (event.getSource() == btnOwnershipTransferring) {
            loadCenterPanel("developerOwnerShipTransferring");
        } else if (event.getSource() == btnOwnershipManagement) {
            loadCenterPanel("developerOwnerShipManagement");
        }
    }

    private void setSubMenuState(VBox excludeSubMenu) {
        for (VBox subMenu : getMenu()) {
            if (subMenu != excludeSubMenu) { // Skip the excluded submenu
                subMenu.setVisible(false);
                subMenu.setManaged(false);
            }
        }
    }

    private void setSubMenuState() {
        for (VBox subMenu : getMenu()) {
            subMenu.setVisible(false);
            subMenu.setManaged(false);
        }
    }

    private VBox[] getMenu() {
        VBox[] subMenus = {subMenuOwnership};
        return subMenus;
    }

    private void subMenuToggle(VBox subMenu) {
        subMenu.setVisible(!subMenu.isVisible());
        subMenu.setManaged(!subMenu.isManaged());
    }

    private void loadCenterPanel(String fxml) {
        try {
            FXMLLoader panel = new FXMLLoader(getClass().getResource("/com/qb/app/" + fxml + ".fxml"));
            contentBorder.setCenter(panel.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setIcons() {
        iconDashboard.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/dashboard-solid.svg"));

        iconOwnershipManagement.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/developer-ownership.svg"));
        iconOwnershipTransferringSubMenu.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/developer-ownership-small.svg"));
        iconOwnershipManagementSubMenu.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/developer-ownership-small.svg"));
        iconControlPanel.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/developer-control-panel.svg"));
        iconBasics.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/developer-basics.svg"));
    }

    private void setDefaultPanel() {
        try {
            FXMLLoader dashboard = new FXMLLoader(getClass().getResource("/com/qb/app/developerDashboard.fxml"));
            contentBorder.setCenter(dashboard.load());
            FXMLLoader developer_top_menu = new FXMLLoader(getClass().getResource("/com/qb/app/fxmlComponent/developer_top_panel.fxml"));
            contentBorder.setTop(developer_top_menu.load());
            controller = developer_top_menu.getController();
            controller.setPanelDeveloperController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toggleMenu() {
        if (isMenuCollapsed) {
            expandMenu();
        } else {
            collapseMenu();
        }
        isMenuCollapsed = !isMenuCollapsed; // Toggle the state
        double menuWidth = leftSideMenu.getWidth();
    }

    private void collapseMenu() {
        double menuWidth = leftSideMenu.getWidth();

        leftSideMenu.setMinWidth(0); // Ensure it can shrink properly
        leftSideMenu.setMaxWidth(menuWidth);

        // Create a TranslateTransition for the side menu
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(300), leftSideMenu);
        translateTransition.setToX(-menuWidth); // Move the menu to the left by its width

        // Create a Timeline to animate the width of the side menu
        Timeline widthTransition = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new KeyValue(leftSideMenu.prefWidthProperty(), 0)
                )
        );

        // Combine both transitions into a ParallelTransition
        ParallelTransition parallelTransition = new ParallelTransition(widthTransition, translateTransition);
        parallelTransition.setOnFinished(event -> leftSideMenu.setMaxWidth(0)); // Ensure it stays collapsed
        parallelTransition.play();
    }

    private void expandMenu() {
        double menuWidth = 250;

        leftSideMenu.setMaxWidth(menuWidth);

        // Create a TranslateTransition for the side menu
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(300), leftSideMenu);
        translateTransition.setToX(0); // Move the menu back to its original position

        // Create a Timeline to animate the width of the side menu
        Timeline widthTransition = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new KeyValue(leftSideMenu.prefWidthProperty(), menuWidth)
                )
        );

        // Combine both transitions into a ParallelTransition
        ParallelTransition parallelTransition = new ParallelTransition(translateTransition, widthTransition);
        parallelTransition.setOnFinished(event -> leftSideMenu.setMinWidth(menuWidth)); // Prevent it from resizing back to 140px
        parallelTransition.play();
    }

    private void setInitialState() {
        setDefaultPanel();
        setSubMenuState();
    }

}
