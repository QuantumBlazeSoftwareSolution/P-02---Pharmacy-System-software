package com.qb.app.controllers;

import com.qb.app.model.InterfaceAction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.getLogger;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PanelAdminController implements Initializable {

    // <editor-fold desc="FXML init component" defaultstate="collapsed">
    @FXML
    private AnchorPane root;
    @FXML
    private BorderPane mainBorderLayout;
    @FXML
    private BorderPane leftSideMenu;
    @FXML
    private Circle systemLogo;
    @FXML
    private Button btnExit;
    @FXML
    private Group iconExit;
    @FXML
    private Button btnDashboard;
    @FXML
    private Group iconDashboard;
    @FXML
    private Group iconProduct;
    @FXML
    private Group iconDiscount;
    @FXML
    private Group iconInventory;
    @FXML
    private Group iconSupplyManagement;
    @FXML
    private Group iconReport;
    @FXML
    private BorderPane contentBorder;
    @FXML
    private Button btnProduct;
    @FXML
    private Button btnDiscount;
    @FXML
    private Button btnInventory;
    @FXML
    private Button btnSupplyManagement;
    @FXML
    private Button btnReports;
    @FXML
    private VBox subMenuProduct;
    @FXML
    private VBox subMenuInventory;
    @FXML
    private VBox subMenuSupply;
    @FXML
    private VBox subMenuReport;
    @FXML
    private Group iconProductRegistration;
    @FXML
    private Group iconProductManagement;
    @FXML
    private Group iconBrandManagement;
    @FXML
    private Group iconInventoryGrn;
    @FXML
    private Group iconCompanyManagement;
    @FXML
    private Group iconSupplierManagement;
    @FXML
    private Group iconReportSale1;
    @FXML
    private Group iconReportGRN;
    @FXML
    private Group iconReportStockBalance;
    @FXML
    private HBox btnProductRegistration;
    @FXML
    private HBox btnProductManagement;
    @FXML
    private HBox btnProductBrandManagement;
    @FXML
    private HBox btnInventoryGRN;
    @FXML
    private HBox btnSupplyCompanyManagement;
    @FXML
    private HBox btnSupplySupplierManagement;
    @FXML
    private HBox btnReportDetailSale;
    @FXML
    private HBox btnReportGRN;
    @FXML
    private HBox btnReportStockBalance;
    // </editor-fold>

    // <editor-fold desc="Initial Variables" defaultstate="collapsed">
    private boolean isMenuCollapsed = false;
    private Admin_top_panelController controller;
    // </editor-fold>

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIcons();
        setInitialState();
        setSystemLogo();
        setSubMenuState();
        setEventFilter();
    }

    private void subMenuToggle(VBox subMenu) {
        subMenu.setVisible(!subMenu.isVisible());
        subMenu.setManaged(!subMenu.isManaged());
    }

    @FXML
    private void handleActionButtons(ActionEvent event) {
        if (event.getSource() == btnProduct) {
            subMenuToggle(subMenuProduct);
            setSubMenuState(subMenuProduct);
        } else if (event.getSource() == btnInventory) {
            subMenuToggle(subMenuInventory);
            setSubMenuState(subMenuInventory);
        } else if (event.getSource() == btnSupplyManagement) {
            subMenuToggle(subMenuSupply);
            setSubMenuState(subMenuSupply);
        } else if (event.getSource() == btnReports) {
            subMenuToggle(subMenuReport);
            setSubMenuState(subMenuReport);
        } else if (event.getSource() == btnExit) {
            InterfaceAction.closeWindow(root);
            // App.setRoot("sytemLogin");
        } else if (event.getSource() == btnDiscount) {
            loadCenterPanel("discount");
        } else if (event.getSource() == btnDashboard) {
            loadCenterPanel("adminDashboard");
        }
    }

    private void setIcons() {
        iconDashboard.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/dashboard-solid.svg"));
        iconDiscount.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/admin-discount.svg"));
        iconExit.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/cashier-exit-solid.svg"));
        iconInventory.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/admin-inventory.svg"));
        iconProduct.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/admin-product.svg"));
        iconReport.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/admin-report.svg"));
        iconSupplyManagement.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/admin-supply-management.svg"));

        iconProductRegistration.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/product-registration.svg"));
        iconProductManagement.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/product-management.svg"));
        iconBrandManagement.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/brand-management.svg"));

        iconInventoryGrn.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/inventory-grn.svg"));

        iconCompanyManagement.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/companyManagement.svg"));
        iconSupplierManagement.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/supplierManagement.svg"));

        iconReportGRN.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/reports.svg"));
        iconReportSale1.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/reports.svg"));
        iconReportStockBalance.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/reports.svg"));
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
    }

    private void setDefaultPanel() {
        try {
            FXMLLoader dashboard = new FXMLLoader(getClass().getResource("/com/qb/app/adminDashboard.fxml"));
            contentBorder.setCenter(dashboard.load());
            FXMLLoader admin_top_menu = new FXMLLoader(getClass().getResource("/com/qb/app/fxmlComponent/admin_top_panel.fxml"));
            contentBorder.setTop(admin_top_menu.load());
            controller = admin_top_menu.getController();
            controller.setPanelAdminController(this);
        } catch (IOException e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void changeCenterPanel(String fxml, String title) {
        try {
            FXMLLoader panel = new FXMLLoader(getClass().getResource(fxml));
            contentBorder.setCenter(panel.load());
        } catch (IOException e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void setSystemLogo() {
        Image image = new Image(getClass().getResource("/com/qb/app/assets/images/QB_LOGO.png").toExternalForm());
        systemLogo.setFill(new ImagePattern(image));
    }

    private void setSubMenuState() {
        for (VBox subMenu : getMenu()) {
            subMenu.setVisible(false);
            subMenu.setManaged(false);
        }
    }

    private VBox[] getMenu() {
        VBox[] subMenus = {subMenuProduct, subMenuInventory, subMenuSupply, subMenuReport};
        return subMenus;
    }

    private void setSubMenuState(VBox excludeSubMenu) {
        for (VBox subMenu : getMenu()) {
            if (subMenu != excludeSubMenu) { // Skip the excluded submenu
                subMenu.setVisible(false);
                subMenu.setManaged(false);
            }
        }
    }

    @FXML
    private void handleSubMenuItems(MouseEvent event) {
        if (event.getSource() == btnProductBrandManagement) {
            loadCenterPanel("product_brand_management");
        } else if (event.getSource() == btnProductManagement) {
            loadCenterPanel("product_management");
        } else if (event.getSource() == btnProductRegistration) {
            loadCenterPanel("product_registration");
        } else if (event.getSource() == btnInventoryGRN) {
            loadCenterPanel("inventory_grn");
        } else if (event.getSource() == btnSupplyCompanyManagement) {
            loadCenterPanel("supply_company_management");
        } else if (event.getSource() == btnSupplySupplierManagement) {
            loadCenterPanel("supply_supplier_management");
        } else if (event.getSource() == btnReportGRN) {
            loadCenterPanel("reportFXML/reportGRN");
        } else if (event.getSource() == btnReportDetailSale) {
            loadCenterPanel("reportFXML/reportSaleDetail");
        } else if (event.getSource() == btnReportStockBalance) {
            loadCenterPanel("reportFXML/reportStockBalance");
        }
    }

    private void loadCenterPanel(String fxml) {
        try {
            FXMLLoader panel = new FXMLLoader(getClass().getResource("/com/qb/app/" + fxml + ".fxml"));
            contentBorder.setCenter(panel.load());
        } catch (IOException e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void setEventFilter() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (event) -> {
            if (event.getCode() == KeyCode.F11) {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.setFullScreen(!stage.isFullScreen());
                event.consume();
            }
        });
    }
}
