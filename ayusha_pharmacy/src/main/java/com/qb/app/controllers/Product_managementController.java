package com.qb.app.controllers;

import com.qb.app.App;
import com.qb.app.model.ComboBoxUtils;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Brand;
import com.qb.app.model.entity.Product;
import com.qb.app.model.entity.ProductHasProductType;
import com.qb.app.model.entity.ProductType;
import com.qb.app.model.entity.ProductUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Product_managementController implements Initializable {

    // <editor-fold desc="FXML init component" defaultstate="collapsed">
    @FXML
    private Group iconPage;
    @FXML
    private TextField tfItemName;
    @FXML
    private ComboBox<Brand> cbBrand;
    @FXML
    private TextField tfSalePrice;
    @FXML
    private TextField tfCostPrice;
    @FXML
    private TextField tfDiscount;
    @FXML
    private ComboBox<ProductUnit> cbUnit;
    @FXML
    private TextField tfMeasure;
    @FXML
    private ComboBox<ProductType> cbType;
    @FXML
    private TextField tfParentID;
    @FXML
    private Button btnPicture;
    @FXML
    private ImageView productImage;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnRegister;
    @FXML
    private TextField tfItemID;
    @FXML
    private TextField tfGenericName;
    @FXML
    private TextField tfBarcode;
    @FXML
    private Label displayMessage;
    // </editor-fold>

    private Product loadedProduct;
    private boolean productID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfItemID.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tfCostPrice.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tfSalePrice.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tfDiscount.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tfMeasure.setTextFormatter(DefaultAPI.createNumericTextFormatter());

        tfParentID.setDisable(true);

        iconPage.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        loadComboBoxData();
    }

    @FXML
    private void handleFileChooser(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        // Set extension filters
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
                "Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                // 1. Save to resources folder
                String destPath = saveToResources(selectedFile);

                // 2. Display in ImageView
                displayImage(destPath);

            } catch (IOException e) {
                showErrorAlert("Error saving image", e.getMessage());
            }
        }
    }

    private String saveToResources(File sourceFile) throws IOException {
        // Define target directory in resources
        String resourcesDir = "src/main/resources/com/qb/app/assets/images/product/";

        // Create directory if it doesn't exist
        Path dirPath = Paths.get(resourcesDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // Generate unique filename to avoid overwrites
        String fileName = "product_" + System.currentTimeMillis()
                + getFileExtension(sourceFile.getName());
        Path destination = Paths.get(resourcesDir + fileName);

        // Copy file to resources
        Files.copy(sourceFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
        return destination.toString();
    }

    private void displayImage(String imagePath) {
        try {
            // For development (using file system path)
            Image image = new Image(new File(imagePath).toURI().toString());

            // For production (when packaged in JAR)
            // Image image = new Image(getClass().getResourceAsStream(
            //     "/com/qb/app/assets/images/product/" + Paths.get(imagePath).getFileName()));
            productImage.setImage(image);
        } catch (Exception e) {
            showErrorAlert("Error loading image", e.getMessage());
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleActionEvent(ActionEvent event) {

    }

    private void loadProductDetails() {
        JPATransaction.runInTransaction((em) -> {
            try {
                int productID = Integer.parseInt(tfItemID.getText());
                Product product = em.find(Product.class, productID);
                if (product != null) {
                    this.loadedProduct = product;
                    tfItemName.setText(product.getProduct());
                    tfGenericName.setText(product.getGenericName());
                    tfSalePrice.setText(String.valueOf(product.getSalePrice()));
                    tfCostPrice.setText(String.valueOf(product.getCostPrice()));
                    tfDiscount.setText(String.valueOf(product.getDiscount()));
                    tfMeasure.setText(String.valueOf(product.getMeasure()));
                    String imagePath = findProductImage(product.getId());
                    setItemImage(imagePath);
                    setComboBoxData(product);
                } else {
                    displayWarningMessage("Product not found.", false);
                }
            } catch (Exception e) {
                displayWarningMessage("Invalid Product ID.", false);
            }
        });
    }

    private void setItemImage(String imageUrl) {
        try {
            Image image = new Image(imageUrl);
            productImage.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load image: " + imageUrl);
        }
    }

    private String findProductImage(Integer productId) {
        // Common image extensions to check
        String[] extensions = {".png", ".jpg", ".jpeg", ".gif"};
        String basePath = "/com/qb/app/assets/images/product/product_";

        for (String ext : extensions) {
            String imagePath = basePath + productId + ext;
            try {
                URL imageUrl = getClass().getResource(imagePath);
                if (imageUrl != null) {
                    return imageUrl.toExternalForm();
                }
            } catch (Exception e) {
                // Continue to next extension if this one fails
                continue;
            }
        }

        // Return default image if none found
        return getClass().getResource("/com/qb/app/assets/images/new_product_image.png").toExternalForm();
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
    private void handlePopUpWindow(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            productID = true;
            if (tfItemID.getText().isEmpty()) {
                openProductPopUpView();
            } else {
                loadProductDetails();
            }
        }
    }

    public void setParentID(String id) {
        if (productID) {
            tfItemID.setText(id);
        } else {
            tfParentID.setText(id);
        }
    }

    private void loadComboBoxData() {
        ComboBoxUtils.loadComboBoxValues(cbBrand, Brand.class, "brand", Brand::getBrand);
        ComboBoxUtils.loadComboBoxValues(cbUnit, ProductUnit.class, "unit", ProductUnit::getUnit);
        ComboBoxUtils.loadComboBoxValues(cbType, ProductType.class, "type", ProductType::getType);
    }

    private void setComboBoxData(Product product) {
        JPATransaction.runInTransaction((em) -> {

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProductHasProductType> cq = cb.createQuery(ProductHasProductType.class);
            Root<ProductHasProductType> productHasProductTypeTable = cq.from(ProductHasProductType.class);

            Predicate predicate1 = cb.equal(productHasProductTypeTable.get("productId"), product);
            cq.where(predicate1);

            try {
                ProductHasProductType productHasProductType = em.createQuery(cq).getSingleResult();

                if (productHasProductType.getProductTypeId().getType().equals("Child")) {
                    tfParentID.setDisable(true);
                    tfParentID.setText(String.valueOf(productHasProductType.getReferenceId().getId()));
                } else {
                    tfParentID.setDisable(true);
                }

                Platform.runLater(() -> {
                    cbType.getItems().stream()
                            .filter(type -> productHasProductType.getProductTypeId().getType().equals(type.getType()))
                            .findFirst()
                            .ifPresent(parentType -> cbType.getSelectionModel().select(parentType));
                    cbUnit.getItems().stream()
                            .filter(unit -> product.getProductUnitId().getUnit().equals(unit.getUnit()))
                            .findFirst()
                            .ifPresent(unitType -> cbUnit.getSelectionModel().select(unitType));
                    cbBrand.getItems().stream()
                            .filter(brand -> product.getBrandId().getBrand().equals(brand.getBrand()))
                            .findFirst()
                            .ifPresent(brand -> cbBrand.getSelectionModel().select(brand));
                });
            } catch (Exception e) {
                System.out.println("Cannot allocate the product" + e.getMessage());
            }
        });
    }

    private void openProductPopUpView() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("popUpProductList.fxml"));
            Parent root = loader.load();

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.initOwner(tfItemID.getScene().getWindow());
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
    }

    @FXML
    private void openParentProducts(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (tfParentID.getText().isEmpty()) {
                productID = false;
                openProductPopUpView();
            }
        }
    }

    @FXML
    private void handleProductType(ActionEvent event) {
        if (cbType.getValue().getType().equals("Child")) {
            tfParentID.setDisable(false);
            tfCostPrice.setDisable(true);
            tfCostPrice.setText("");
        } else {
            tfParentID.setDisable(true);
            tfParentID.setText("");
            tfCostPrice.setDisable(false);
        }
    }
}
