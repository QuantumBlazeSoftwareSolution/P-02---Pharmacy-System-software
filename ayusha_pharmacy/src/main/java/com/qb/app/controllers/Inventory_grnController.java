/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers;

import com.qb.app.App;
import com.qb.app.model.ComboBoxUtils;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Company;
import com.qb.app.model.entity.Grn;
import com.qb.app.model.entity.Product;
import com.qb.app.model.entity.Supplier;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Inventory_grnController implements Initializable {

    // <editor-fold desc="FXML init component" defaultstate="collapsed">
    @FXML
    private ScrollPane grnTableScrollContainer;
    @FXML
    private VBox grnTableBody;
    @FXML
    private ScrollBar grnTableScroller;
    @FXML
    private Group GrnIcon;
    @FXML
    private ComboBox<Company> cbCompany;
    @FXML
    private ComboBox<Supplier> cbSupplier;
    @FXML
    private TextField tfGRNID;
    @FXML
    private TextField tfProductID;
    @FXML
    private TextField tfProductName;
    @FXML
    private TextField tfGenericName;
    @FXML
    private TextField tfCostPrice;
    @FXML
    private TextField tfQty;
    @FXML
    private TextField tfAmount;
    @FXML
    private TextField tfTotal;
    @FXML
    private Button btnAction;
    //</editor-fold>
    @FXML
    private AnchorPane root;
    @FXML
    private Label displayMessage;

    private Product loadedProduct;
    private boolean readyItemToAdd;
    private int itemQty;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfQty.setDisable(false);
        DefaultAPI.bindTableScroll(grnTableScroller, grnTableScrollContainer, grnTableBody);
        GrnIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        tfQty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        loadComboBox();

    }

    private void loadComboBox() {
        ComboBoxUtils.loadComboBoxValues(cbCompany, Company.class, "name", Company::getName);
        ComboBoxUtils.loadComboBoxValues(cbSupplier, Supplier.class, "name", Supplier::getName);
    }

    @FXML
    private void actionEvent(ActionEvent event) {
        if (event.getSource() == btnAction) {
            makeGrn();
        }
    }

    private void makeGrn() {
        if (isEntriesValid()) {
            if (isGrnIdAvailable()) {
                createGrn();
            } else {
                CustomAlert.showStyledAlert(root, "This GRN number is already registered in the system.", "GRN Duplication Warning", Alert.AlertType.WARNING);
            }
        }
    }

    private boolean isEntriesValid() {
        if (cbCompany.getValue() == null) {
            displayWarningMessage("Please select the supply company", false);
            cbCompany.requestFocus();
            return false;
        }

        if (cbSupplier.getValue() == null) {
            displayWarningMessage("Please select the supplier", false);
            cbSupplier.requestFocus();
            return false;
        }

        if (tfGRNID.getText().isEmpty()) {
            displayWarningMessage("Please enter the grn id", false);
            tfGRNID.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isGrnIdAvailable() {
        return JPATransaction.runInTransaction((em) -> {
            String grnID = tfGRNID.getText();
            Supplier selectedSupplier = cbSupplier.getValue();

            if (grnID == null || grnID.isEmpty() || selectedSupplier == null) {
                return true;
            }

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Grn> grnTable = cq.from(Grn.class);

            Predicate grnCodePredicate = cb.equal(grnTable.get("grnCode"), grnID);
            Predicate supplierPredicate = cb.equal(grnTable.get("supplierId"), selectedSupplier);

            cq.select(cb.count(grnTable))
                    .where(cb.and(grnCodePredicate, supplierPredicate));

            return em.createQuery(cq).getSingleResult() == 0;
        });
    }

    private void createGrn() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void displayWarningMessage(String message, boolean action) {
        if (action) {
            displayMessage.setStyle("-fx-text-fill: #0D9F00;"); // Green
        } else {
            displayMessage.setStyle("-fx-text-fill: #FF3333;"); // Red
        }
        displayMessage.setText(message);

        PauseTransition delay = new PauseTransition(Duration.seconds(10));
        delay.setOnFinished(event -> displayMessage.setText(""));
        delay.play();
    }

    @FXML
    private void handlePopUpProductView(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (tfProductID.getText().isEmpty()) {
                try {
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("popUpProductList.fxml"));
                    Parent root = loader.load();

                    // Create a new stage for the popup
                    Stage popupStage = new Stage();
                    popupStage.initOwner(tfProductID.getScene().getWindow());
                    popupStage.initModality(Modality.APPLICATION_MODAL);

                    // Get screen dimensions
                    Screen screen = Screen.getPrimary();
                    Rectangle2D bounds = screen.getVisualBounds();

                    // Create scene with full width but original height
                    Scene scene = new Scene(root);
                    popupStage.setScene(scene);

                    // Set width to screen width and position at x=0
                    popupStage.setWidth(bounds.getWidth());
                    popupStage.setX(0); // This ensures no left gap

                    // Set fixed height (adjust as needed)
                    popupStage.setHeight(600);

                    // Center the popup vertically
                    popupStage.setY((bounds.getHeight() - popupStage.getHeight()) / 2);

                    popupStage.initStyle(StageStyle.TRANSPARENT);

                    // Get controller reference
                    PopUpProductListController controller = loader.getController();
                    controller.saveProductRegistrationController(this);

                    popupStage.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                loadProductDetails();
            }
        }
    }

    public void setParentID(String id) {
        tfProductID.setText(id);
    }

    private void loadProductDetails() {
        JPATransaction.runInTransaction((em) -> {
            try {
                int productID = Integer.parseInt(tfProductID.getText());
                Product product = em.find(Product.class, productID);
                if (product != null) {
                    this.loadedProduct = product;
                    tfProductName.setText(product.getProduct());
                    tfGenericName.setText(product.getGenericName());
                    tfCostPrice.setText(String.valueOf(product.getCostPrice()));
                    tfQty.setDisable(false);
                    tfQty.requestFocus();
                } else {
                    displayWarningMessage("Product not found.", false);
                }
            } catch (Exception e) {
                displayWarningMessage("Invalid Product ID.", false);
            }
        });
    }

    private void calculateLoadedItemAmount() {
        int qty = Integer.parseInt(tfQty.getText());
        this.itemQty = qty;
        double productAmount = loadedProduct.getCostPrice() * qty;
        tfAmount.setText(String.format("Rs. %,.2f", productAmount));
        this.readyItemToAdd = true;
    }

    @FXML
    private void qtyListener(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (this.readyItemToAdd) {
                addItemToList();
            } else {
                String qty = tfQty.getText();
                if (qty.isEmpty() || Integer.parseInt(qty) <= 0) {
                    displayWarningMessage("To proceed, please enter the quantity for this product.", false);
                    this.readyItemToAdd = false;
                } else {
                    calculateLoadedItemAmount();
                }
            }
        } else {
            this.readyItemToAdd = true;
        }
    }

    List<InventoryGRN_TableRowController> grnItemList = new ArrayList<>();

    private void addItemToList() {
        try {
            boolean itemExists = false;

            for (InventoryGRN_TableRowController item : grnItemList) {
                if (item.getProductID() == loadedProduct.getId()) {
                    item.setProductQty(item.getQty() + itemQty);
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/qb/app/fxmlComponent/InventoryGRN_TableRow.fxml"));
                Node grnItem = loader.load();
                InventoryGRN_TableRowController controller = loader.getController();
                controller.setData(loadedProduct.getId(), loadedProduct.getProduct(), loadedProduct.getCostPrice(), itemQty);

                grnItemList.add(controller);
                grnTableBody.getChildren().add(grnItem);
            }

            clearLoadedTextFields();
            resetFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private Product loadedProduct;
//    private boolean readyItemToAdd;
//    private int itemQty;
    private void resetFields() {
        this.loadedProduct = null;
        this.readyItemToAdd = false;
        this.itemQty = 0;
    }

    private void clearLoadedTextFields() {
        tfProductID.setText("");
        tfProductName.setText("");
        tfGenericName.setText("");
        tfCostPrice.setText("");
        tfQty.setText("");
        tfAmount.setText("");

        tfProductID.requestFocus();
    }

}
