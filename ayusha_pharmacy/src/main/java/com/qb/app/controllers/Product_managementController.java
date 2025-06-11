package com.qb.app.controllers;

import com.jfoenix.controls.JFXToggleButton;
import com.qb.app.App;
import com.qb.app.model.ComboBoxUtils;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Brand;
import com.qb.app.model.entity.Product;
import com.qb.app.model.entity.ProductHasProductType;
import com.qb.app.model.entity.ProductStatus;
import com.qb.app.model.entity.ProductType;
import com.qb.app.model.entity.ProductUnit;
import com.qb.app.model.getLogger;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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
    @FXML
    private AnchorPane root;
    @FXML
    private JFXToggleButton toggleStatus;
    // </editor-fold>

    private Product loadedProduct;
    private boolean productID;
    private boolean isProductLoaded;
    private File selectedImageFile;
    private ProductHasProductType productHasProductType;

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
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif")
        );

        selectedImageFile = fileChooser.showOpenDialog(null);
        if (selectedImageFile != null) {
            try {
                // Display preview
                productImage.setImage(new Image(selectedImageFile.toURI().toString()));
            } catch (Exception e) {
                CustomAlert.showStyledAlert(root, "Error loading image: " + e.getMessage(),
                        "Image Error", Alert.AlertType.ERROR);
            }
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }

    @FXML
    private void handleActionEvent(ActionEvent event) {
        if (event.getSource() == btnRegister) {
            productUpdate();
        } else if (event.getSource() == btnClear) {
            clearRegistrationField();
        }
    }

    private void loadProductDetails() {
        JPATransaction.runInTransaction((em) -> {
            try {
                int productID = Integer.parseInt(tfItemID.getText());
                Product product = em.find(Product.class, productID);
                if (product != null) {
                    this.loadedProduct = product;
                    isProductLoaded = true;
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
            getLogger.logger().warning(e.toString());
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
                this.productHasProductType = productHasProductType;
                if (productHasProductType.getProductTypeId().getType().equals("Child")) {
                    tfParentID.setDisable(true);
                    tfParentID.setText(String.valueOf(productHasProductType.getReferenceId().getId()));
                } else {
                    tfParentID.setDisable(true);
                }

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
            getLogger.logger().warning(e.toString());
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

        if (cbType.getValue() == null) {
            System.out.println("Type is empty");
            return; // or handle the null case appropriately
        }

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

    private void clearRegistrationField() {
        cbBrand.setValue(null);
        cbUnit.setValue(null);
        cbType.setValue(null);
        cbBrand.setPromptText("Ex: Munche");
        cbUnit.setPromptText("Select Unit");
        cbType.setPromptText("Select Type");
        tfBarcode.setText("");
        tfCostPrice.setText("");
        tfDiscount.setText("");
        tfItemID.setText("");
        tfItemName.setText("");
        tfGenericName.setText("");
        tfMeasure.setText("");
        tfParentID.setText("");
        tfSalePrice.setText("");
        loadDefaultImage();
        tfCostPrice.setDisable(false);
        isProductLoaded = false;
        loadedProduct = null;
    }

    private void loadDefaultImage() {
        try {
            // Absolute path from classpath root
            String imagePath = "/com/qb/app/assets/images/new_product_image.png";
            InputStream stream = getClass().getResourceAsStream(imagePath);

            if (stream != null) {
                productImage.setImage(new Image(stream));
            } else {
                System.err.println("Image not found: " + imagePath);
                productImage.setImage(null); // Clear or set placeholder
            }
        } catch (Exception e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
            productImage.setImage(null);
        }
    }

    private void productUpdate() {
        if (IsProductValid()) {
            if (isProductLoaded) {
                if (cbType.getValue().getType().equals("Child")) {
                    if (tfParentID.getText().isEmpty()) {
                        displayWarningMessage("Parent ID is required for Child Products. Please enter the parent product ID.", false);
                    } else {
                        // Register child product
                        mergeProduct("Child");
                    }
                } else if (cbType.getValue().getType().equals("Parent")) {
                    // Register parent product
                    mergeProduct("parent");
                }
            } else {
                CustomAlert.showStyledAlert(root, "Please select a product to update.", Alert.AlertType.WARNING);
            }
        }
    }

    private boolean IsProductValid() {
        if (tfItemName.getText() == null || tfItemName.getText().isEmpty()) {
            displayWarningMessage("Product name is required.", false);
            tfItemName.requestFocus();
            return false;
        }

//        if (tfBarCode.getText().isEmpty()) {
//            displayRegistrationMessage("Barcode is required.", false);
//            tfBarCode.requestFocus();
//            return false;
//        }
        if (tfSalePrice.getText() == null || tfSalePrice.getText().isEmpty()) {
            displayWarningMessage("Sale price is required.", false);
            tfSalePrice.requestFocus();
            return false;
        }

        if (!DefaultAPI.isDouble(tfSalePrice.getText())) {
            displayWarningMessage("Invalid sale price format.", false);
            tfSalePrice.requestFocus();
            return false;
        }

        if (cbType.getValue() == null || cbType.getValue().getType().equals("Parent")) {
            if (tfCostPrice.getText() == null || tfCostPrice.getText().isEmpty()) {
                displayWarningMessage("Cost price is required.", false);
                tfCostPrice.requestFocus();
                return false;
            }
        }

        if (!DefaultAPI.isDouble(tfCostPrice.getText())) {
            displayWarningMessage("Invalid cost price format.", false);
            tfCostPrice.requestFocus();
            return false;
        }

        if (!tfDiscount.getText().isEmpty() && !DefaultAPI.isDouble(tfDiscount.getText())) {
            displayWarningMessage("Invalid discount format.", false);
            tfDiscount.requestFocus();
            return false;
        }

        if (tfMeasure.getText() == null || tfMeasure.getText().isEmpty()) {
            displayWarningMessage("Measurement is required.", false);
            tfMeasure.requestFocus();
            return false;
        }

        if (cbBrand.getValue() == null) {
            displayWarningMessage("Please select a brand.", false);
            cbBrand.requestFocus();
            return false;
        }

        if (cbUnit.getValue() == null) {
            displayWarningMessage("Please select a unit.", false);
            cbUnit.requestFocus();
            return false;
        }

        if (cbType.getValue() == null) {
            displayWarningMessage("Please select a product type.", false);
            cbType.requestFocus();
            return false;
        }

        // Additional validations
        double salePrice = Double.parseDouble(tfSalePrice.getText());
        double costPrice = Double.parseDouble(tfCostPrice.getText());

        if (salePrice <= 0) {
            displayWarningMessage("Sale price must be greater than 0.", false);
            tfSalePrice.requestFocus();
            return false;
        }

        if (costPrice <= 0) {
            displayWarningMessage("Cost price must be greater than 0.", false);
            tfCostPrice.requestFocus();
            return false;
        }

        if (salePrice < costPrice) {
            displayWarningMessage("Sale price cannot be less than cost price.", false);
            tfSalePrice.requestFocus();
            return false;
        }

        if (!tfDiscount.getText().isEmpty()) {
            double discount = Double.parseDouble(tfDiscount.getText());
            if (discount < 0) {
                displayWarningMessage("Discount must be greater than LKR. 0.00", false);
                tfDiscount.requestFocus();
                return false;
            }
        }

        return true;
    }

    private void mergeProduct(String type) {
        JPATransaction.runInTransaction((em) -> {
            try {
                Product parentProduct = null;
                if (type.equals("Child")) {
                    parentProduct = em.find(Product.class, tfParentID.getText());
                    if (parentProduct == null) {
//                        CustomAlert.showStyledAlert("No parent product found with ID: " + tfParentID.getText(), Alert.AlertType.WARNING);
                        displayWarningMessage("No parent product found with ID: " + tfParentID.getText(), false);
                        tfParentID.requestFocus();
                        return;
                    }
                }

                // save new product
                Product product = new Product();
                loadedProduct.setProduct(tfItemName.getText());
                loadedProduct.setSalePrice(Double.parseDouble(tfSalePrice.getText()));
                double costPrice;
                if ("Child".equals(cbType.getValue().getType())) {
                    costPrice = 0.0; // Set cost price to 0 for child products
                } else {
                    costPrice = Double.parseDouble(tfCostPrice.getText());
                }
                loadedProduct.setCostPrice(costPrice);
                loadedProduct.setDiscount(tfDiscount.getText().isEmpty()
                        ? 0.0
                        : Double.parseDouble(tfDiscount.getText())
                );
                loadedProduct.setMeasure(Float.parseFloat(tfMeasure.getText()));
                if (!tfBarcode.getText().isEmpty()) {
                    product.setBarCode(tfBarcode.getText());
                }
                loadedProduct.setProductUnitId(cbUnit.getValue());
                loadedProduct.setBrandId(cbBrand.getValue());
                loadedProduct.setProductStatusId(getProductStatus(toggleStatus.isSelected()));
                if (!tfGenericName.getText().isEmpty()) {
                    loadedProduct.setGenericName(tfGenericName.getText());
                }
                em.merge(loadedProduct);

                // save new product's product_has_product_type
                productHasProductType.setProductId(loadedProduct);
                productHasProductType.setProductTypeId(cbType.getValue());
                // check if this product is a parent product or a child product
                if (type.equals("Child")) {
                    productHasProductType.setReferenceId(parentProduct);
                } else {
                    productHasProductType.setReferenceId(loadedProduct);
                }
                em.merge(productHasProductType);

                if (selectedImageFile != null) {
                    String extension = getFileExtension(selectedImageFile.getName());
                    String imageName = "product_" + loadedProduct.getId() + extension;
                    saveProductImage(selectedImageFile, imageName);
                }

                displayWarningMessage("Product successfully added to inventory.", true);

                clearRegistrationField();
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
                getLogger.logger().warning(e.toString());
            }
        });
    }

    private String saveProductImage(File sourceFile, String fileName) throws IOException {
        String targetDir = "src/main/resources/com/qb/app/assets/images/product/";
        Path dirPath = Paths.get(targetDir);

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Path destination = Paths.get(targetDir + fileName);
        Files.copy(sourceFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

        return destination.toString();
    }

    private ProductStatus getProductStatus(boolean selected) {
        return JPATransaction.runInTransaction((em) -> {
            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<ProductStatus> cq = cb.createQuery(ProductStatus.class);
                Root<ProductStatus> root = cq.from(ProductStatus.class);
                cq.where(cb.equal(root.get("status"), selected ? "Enable" : "Disable"));
                return em.createQuery(cq).getSingleResult();
            } catch (NoResultException e) {
                // Handle case where no "Enable" status exists
                System.err.println("No ProductStatus with status='Enable' found");
                return null;
            }
        });
    }
}
