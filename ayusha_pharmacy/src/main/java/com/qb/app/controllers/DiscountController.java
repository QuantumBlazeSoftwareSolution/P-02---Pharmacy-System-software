/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers;

import com.qb.app.App;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Product;
import com.qb.app.model.getLogger;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Vihanga
 */
public class DiscountController implements Initializable {

    @FXML
    private Group iconBillDiscountTopic;
    @FXML
    private Group iconProductDiscountTopic;
    @FXML
    private TextField tfProductID;
    @FXML
    private TextField tfProductName;
    @FXML
    private TextField tfProductDiscount;
    @FXML
    private Button btnProductClear;
    @FXML
    private Button btnProductDiscount;
    @FXML
    private TextField tfProductGeneric;
    private Product loadedProduct;
    @FXML
    private Label displayMessage;
    @FXML
    private TextField tfProductSalePrice;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIcon();
    }

    private void setIcon() {
        iconBillDiscountTopic.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        iconProductDiscountTopic.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
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
                    getLogger.logger().warning(e.toString());
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
                    tfProductGeneric.setText(product.getGenericName() != null ? product.getGenericName() : "");
                    tfProductDiscount.setText(String.valueOf(product.getDiscount()));
                    tfProductSalePrice.setText(String.format("Rs. %,.2f", product.getSalePrice()));
                    tfProductDiscount.requestFocus();
                } else {
                    CustomAlert.showStyledAlert(tfProductID, "Product not found", Alert.AlertType.WARNING);
                }
            } catch (Exception e) {
                CustomAlert.showStyledAlert(tfProductID, "Invalid Product ID", Alert.AlertType.WARNING);
            }
        });
    }

    @FXML
    private void actionEvent(ActionEvent event) {
        if (event.getSource() == btnProductDiscount) {
            saveDiscount();
        } else if (event.getSource() == btnProductClear) {
            resetProductDiscount();
        }
    }

    private void saveDiscount() {
        if (tfProductDiscount.getText().isEmpty()) {
            displayWarningMessage("Please add discount amount.", false);
            tfProductDiscount.requestFocus();
        } else {
            try {
                double discount = Double.parseDouble(tfProductDiscount.getText());
                if (discount > 0) {

                    JPATransaction.runInTransaction((em) -> {
                        loadedProduct.setDiscount(discount);
                        em.merge(loadedProduct);
                    });

                    displayWarningMessage("Discount applied successfully", true);
                    resetProductDiscount();
                } else {
                    displayWarningMessage("Discount amount should be greater than 0.", false);
                }
            } catch (Exception e) {
                displayWarningMessage("Invalid discount amount.", false);
                e.printStackTrace();
                getLogger.logger().warning(e.toString());
            }
        }
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

    private void resetProductDiscount() {
        loadedProduct = null;
        tfProductID.setText("");
        tfProductName.setText("");
        tfProductGeneric.setText("");
        tfProductDiscount.setText("");
    }

}
