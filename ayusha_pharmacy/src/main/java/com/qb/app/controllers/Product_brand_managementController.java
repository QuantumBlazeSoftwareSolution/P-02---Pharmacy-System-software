package com.qb.app.controllers;

import com.jfoenix.controls.JFXToggleButton;
import com.qb.app.App;
import com.qb.app.model.SVGIconGroup;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.JPATransaction;
import static com.qb.app.model.JPATransaction.runInTransaction;
import com.qb.app.model.entity.Brand;
import com.qb.app.model.entity.Product;
import com.qb.app.model.entity.ProductStatus;
import com.qb.app.model.getLogger;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Product_brand_managementController implements Initializable {

    //<editor-fold desc="FXML init component" defaultstate="collapsed">
    @FXML
    private Group iconPage;
    @FXML
    private TextField tfPrimaryBrandName;
    @FXML
    private Button btnPrimaryClear;
    @FXML
    private Button btnPrimaryRegister;
    @FXML
    private AnchorPane root;
    @FXML
    private TextField tfBrandID;
    @FXML
    private TextField tfSecondaryBrandName;
    @FXML
    private JFXToggleButton toggleBrandStatus;
    @FXML
    private Button btnSecondaryClear;
    @FXML
    private Button btnSecondaryUpdate;
    //</editor-fold>

    private Brand loadedBrand;
    @FXML
    private Label displayMessage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iconPage.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
    }

    @FXML
    private void handleActionEvent(ActionEvent event) {
        if (event.getSource() == btnPrimaryRegister) {
            BrandRegistration();
        } else if (event.getSource() == btnPrimaryClear) {
            clearPrimary();
        } else if (event.getSource() == btnSecondaryUpdate) {
            updateBrand();
        } else if (event.getSource() == btnSecondaryClear) {
            clearSecondary();
        }
    }

    private void BrandRegistration() {
        if (checkRegistrationValidity()) {
            if (!isBrandExist()) {
                registerNewBrand();
            } else {
                CustomAlert.showStyledAlert(root, "This brand name is already registered. Please choose a different name.", Alert.AlertType.WARNING);
            }
        }
    }

    private boolean checkRegistrationValidity() {
        if (tfPrimaryBrandName.getText().isEmpty() || tfPrimaryBrandName.getText().equals("")) {
            CustomAlert.showStyledAlert(root, "Brand name is required and cannot be blank.", Alert.AlertType.WARNING);
        } else {
            return true;
        }
        return false;
    }

    private boolean isBrandExist() {
        return runInTransaction(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Brand> cq = cb.createQuery(Brand.class);
            Root<Brand> brandTable = cq.from(Brand.class);

            Predicate predicate = cb.equal(brandTable.get("brand"), tfPrimaryBrandName.getText());
            cq.where(predicate);
            List<Brand> results = em.createQuery(cq).getResultList();
            return !results.isEmpty();
        });
    }

    private void registerNewBrand() {
        runInTransaction(em -> {
            Brand newBrand = new Brand();
            newBrand.setBrand(tfPrimaryBrandName.getText());
            newBrand.setProductStatusId(getProductStatus("Enable"));
            em.persist(newBrand);
        });
    }

    private ProductStatus getProductStatus(String status) {
        return JPATransaction.runInTransaction(em -> {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<ProductStatus> cQuery = cBuilder.createQuery(ProductStatus.class);
            Root<ProductStatus> productStatusTable = cQuery.from(ProductStatus.class);

            Predicate prediction = cBuilder.equal(productStatusTable.get("status"), status);
            cQuery.where(prediction);

            try {
                return em.createQuery(cQuery).getSingleResult();
            } catch (Exception e) {
                CustomAlert.showStyledAlert(root, "Cannot find the product status: " + status, Alert.AlertType.WARNING);
                return null;
            }
        });
    }

    private void clearPrimary() {
        tfPrimaryBrandName.setText("");
    }

    @FXML
    private void handleBrandIDKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (tfBrandID.getText().isEmpty()) {
                try {
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("popUpBrandList.fxml"));
                    Parent root = loader.load();

                    // Create a new stage for the popup
                    Stage popupStage = new Stage();
                    popupStage.initOwner(this.root.getScene().getWindow());
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
                    PopUpBrandListController controller = loader.getController();
                    controller.saveCallingController(this);

                    popupStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            } else {
                loadBrands();
            }
        }
    }

    public void setBrandID(String id) {
        tfBrandID.setText(id);
    }

    private void loadBrands() {
        JPATransaction.runInTransaction((em) -> {
            try {
                int brandID = Integer.parseInt(tfBrandID.getText());
                Brand brand = em.find(Brand.class, brandID);
                if (brand != null) {
                    this.loadedBrand = brand;
                    tfSecondaryBrandName.setText(brand.getBrand());
                    if (brand.getProductStatusId().getStatus().equals("Enable")) {
                        toggleBrandStatus.setSelected(true);
                    } else {
                        toggleBrandStatus.setSelected(false);
                    }
                } else {
                    displayWarningMessage("Brand not found.", false);
                }
            } catch (Exception e) {
                displayWarningMessage("Invalid Brand ID.", false);
            }
        });
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

    private void clearSecondary() {
        tfBrandID.setText("");
        tfSecondaryBrandName.setText("");
        tfBrandID.requestFocus();
    }

    private void updateBrand() {
        if (loadedBrand != null) {
            if (!tfSecondaryBrandName.getText().isEmpty()) {
                loadedBrand.setBrand(tfSecondaryBrandName.getText());
                if (!toggleBrandStatus.isSelected()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Disable Brand - Confirmation Required");
                    alert.setHeaderText("Warning: This Action Will Disable All Brand Products");
                    alert.setContentText("You are about to disable '" + loadedBrand.getBrand() + "' brand.\n\n"
                            + "This will automatically deactivate ALL the products associated with this brand.\n\n"
                            + "Are you sure you want to proceed?");

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(getClass().getResource("/com/qb/app/assets/images/logo.png").toExternalForm()));

                    ButtonType disableButton = new ButtonType("Disable Brand", ButtonBar.ButtonData.CANCEL_CLOSE);
                    ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(disableButton, cancelButton);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == disableButton) {
                        loadedBrand.setProductStatusId(getProductStatus(false));
                    }
                } else {
                    loadedBrand.setProductStatusId(getProductStatus(true));
                }

                JPATransaction.runInTransaction((em) -> {
                    em.merge(loadedBrand);
                    loadedBrand = null;
                    displayWarningMessage("Brand update completed", true);
                    clearSecondary();
                });
            } else {
                displayWarningMessage("Brand name cannot be empty.", false);
            }
        } else {
            displayWarningMessage("Please enter a brand by press enter to 'Brand ID' text field.", false);
        }
    }

    private ProductStatus getProductStatus(boolean status) {
        return JPATransaction.runInTransaction((em) -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProductStatus> cq = cb.createQuery(ProductStatus.class);
            Root<ProductStatus> statusTable = cq.from(ProductStatus.class);

            Predicate prdcts = cb.equal(statusTable.get("status"), status ? "Enable" : "Disable");

            cq.where(prdcts);
            ProductStatus productStatus = em.createQuery(cq).getSingleResult();
            return productStatus;
        });
    }
}
