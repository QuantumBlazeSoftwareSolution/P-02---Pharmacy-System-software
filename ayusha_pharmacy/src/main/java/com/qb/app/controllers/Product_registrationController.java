package com.qb.app.controllers;

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
import com.qb.app.model.entity.Stock;
import com.qb.app.model.entity.Store;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Product_registrationController implements Initializable {

    // <editor-fold desc="FXML init component" defaultstate="collapsed">
    @FXML
    private Group iconPage;
    @FXML
    private TextField tfItemName;
    @FXML
    private TextField tfBarCode;
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
    private ImageView productImage;
    @FXML
    private Button btnPicture;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnRegister;
    @FXML
    private Label registrationMessage;
//    </editor-fold>

    private File selectedImageFile; // Stores the selected image file temporarily
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setInitialState();
    }

    private void setInitialState() {
        tfCostPrice.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tfSalePrice.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tfDiscount.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tfMeasure.setTextFormatter(DefaultAPI.createNumericTextFormatter());

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
            CustomAlert.showStyledAlert(root, e.getMessage(), "Error loading image", Alert.AlertType.ERROR);
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }

    @FXML
    private void handleActionEvent(ActionEvent event) {
        if (event.getSource() == btnRegister) {
            productRegister();
        } else if (event.getSource() == btnClear) {
            clearRegistrationField();
        }
    }

    private void productRegister() {
        if (IsProductValid()) {
            if (!isProductExist()) {
                if (cbType.getValue().getType().equals("Child")) {
                    if (tfParentID.getText().isEmpty()) {
                        displayRegistrationMessage("Parent ID is required for Child Products. Please enter the parent product ID.", false);
                    } else {
                        // Register child product
                        saveProduct("Child");
                    }
                } else if (cbType.getValue().getType().equals("Parent")) {
                    // Register parent product
                    saveProduct("parent");
                }
            } else {
                displayRegistrationMessage("Product with this name or barcode already exists!", false);
            }
        }
    }

    private boolean IsProductValid() {
        if (tfItemName.getText().isEmpty()) {
            displayRegistrationMessage("Product name is required.", false);
            tfItemName.requestFocus();
            return false;
        }

        if (tfBarCode.getText().isEmpty()) {
            displayRegistrationMessage("Barcode is required.", false);
            tfBarCode.requestFocus();
            return false;
        }

        if (tfSalePrice.getText().isEmpty()) {
            displayRegistrationMessage("Sale price is required.", false);
            tfSalePrice.requestFocus();
            return false;
        }

        if (!DefaultAPI.isDouble(tfSalePrice.getText())) {
            displayRegistrationMessage("Invalid sale price format.", false);
            tfSalePrice.requestFocus();
            return false;
        }

        if (cbType.getValue() == null || cbType.getValue().getType().equals("Parent")) {
            if (tfCostPrice.getText().isEmpty()) {
                displayRegistrationMessage("Cost price is required.", false);
                tfCostPrice.requestFocus();
                return false;
            }
        }

        if (!DefaultAPI.isDouble(tfCostPrice.getText())) {
            displayRegistrationMessage("Invalid cost price format.", false);
            tfCostPrice.requestFocus();
            return false;
        }

        if (!tfDiscount.getText().isEmpty() && !DefaultAPI.isDouble(tfDiscount.getText())) {
            displayRegistrationMessage("Invalid discount format.", false);
            tfDiscount.requestFocus();
            return false;
        }

        if (tfMeasure.getText().isEmpty()) {
            displayRegistrationMessage("Measurement is required.", false);
            tfMeasure.requestFocus();
            return false;
        }

        if (cbBrand.getValue() == null) {
            displayRegistrationMessage("Please select a brand.", false);
            cbBrand.requestFocus();
            return false;
        }

        if (cbUnit.getValue() == null) {
            displayRegistrationMessage("Please select a unit.", false);
            cbUnit.requestFocus();
            return false;
        }

        if (cbType.getValue() == null) {
            displayRegistrationMessage("Please select a product type.", false);
            cbType.requestFocus();
            return false;
        }

        // Additional validations
        double salePrice = Double.parseDouble(tfSalePrice.getText());
        double costPrice = Double.parseDouble(tfCostPrice.getText());

        if (salePrice <= 0) {
            displayRegistrationMessage("Sale price must be greater than 0.", false);
            tfSalePrice.requestFocus();
            return false;
        }

        if (costPrice <= 0) {
            displayRegistrationMessage("Cost price must be greater than 0.", false);
            tfCostPrice.requestFocus();
            return false;
        }

        if (salePrice < costPrice) {
            displayRegistrationMessage("Sale price cannot be less than cost price.", false);
            tfSalePrice.requestFocus();
            return false;
        }

        if (!tfDiscount.getText().isEmpty()) {
            double discount = Double.parseDouble(tfDiscount.getText());
            if (discount < 0 || discount >= 100) {
                displayRegistrationMessage("Discount must be between 0 and 100%.", false);
                tfDiscount.requestFocus();
                return false;
            }
        }

        return true;
    }

    private void loadComboBoxData() {
        ComboBoxUtils.loadComboBoxValues(cbBrand, Brand.class, "brand", Brand::getBrand);
        ComboBoxUtils.loadComboBoxValues(cbUnit, ProductUnit.class, "unit", ProductUnit::getUnit);
        ComboBoxUtils.loadComboBoxValues(cbType, ProductType.class, "type", ProductType::getType);

        // Set default selection to "Parent" after loading
        Platform.runLater(() -> {
            cbType.getItems().stream()
                    .filter(type -> "Parent".equals(type.getType()))
                    .findFirst()
                    .ifPresent(parentType -> cbType.getSelectionModel().select(parentType));
        });
    }

    private boolean isProductExist() {
        return JPATransaction.runInTransaction((em) -> {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Product> cQuery = cBuilder.createQuery(Product.class);
            Root<Product> productTable = cQuery.from(Product.class);

            Predicate productPredicate1 = cBuilder.equal(
                    cBuilder.lower(productTable.get("product")),
                    tfItemName.getText().toLowerCase()
            );

            Predicate productPredicate2 = cBuilder.equal(
                    cBuilder.lower(productTable.get("barCode")),
                    tfBarCode.getText().toLowerCase()
            );

            // Use OR instead of AND
            cQuery.where(cBuilder.or(productPredicate1, productPredicate2));

            return !em.createQuery(cQuery).getResultList().isEmpty();
        });
    }

    private ProductStatus getProductStatus() {
        return JPATransaction.runInTransaction((em) -> {
            try {
                // Using JPA Criteria API
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<ProductStatus> cq = cb.createQuery(ProductStatus.class);
                Root<ProductStatus> root = cq.from(ProductStatus.class);

                cq.where(cb.equal(root.get("status"), "Enable"));

                return em.createQuery(cq).getSingleResult();

            } catch (NoResultException e) {
                // Handle case where no "Enable" status exists
                System.err.println("No ProductStatus with status='Enable' found");
                return null;
            }
        });
    }

    private void clearRegistrationField() {
        cbBrand.setValue(null);
        cbUnit.setValue(null);
        cbType.setValue(null);
        cbBrand.setPromptText("Ex: Munche");
        cbUnit.setPromptText("Select Unit");
        cbType.setPromptText("Select Type");
        tfBarCode.setText("");
        tfCostPrice.setText("");
        tfDiscount.setText("");
        tfItemName.setText("");
        tfMeasure.setText("");
        tfParentID.setText("");
        tfSalePrice.setText("");
        loadDefaultImage();
        tfCostPrice.setDisable(false);
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
            productImage.setImage(null);
        }
    }

    private void saveProduct(String type) {
        JPATransaction.runInTransaction((em) -> {
            try {
                Product parentProduct = null;
                if (type.equals("Child")) {
                    parentProduct = em.find(Product.class, tfParentID.getText());
                    if (parentProduct == null) {
//                        CustomAlert.showStyledAlert("No parent product found with ID: " + tfParentID.getText(), Alert.AlertType.WARNING);
                        displayRegistrationMessage("No parent product found with ID: " + tfParentID.getText(), false);
                        tfParentID.requestFocus();
                        return;
                    }
                }

                // save new product
                Product product = new Product();
                product.setProduct(tfItemName.getText());
                product.setSalePrice(Double.parseDouble(tfSalePrice.getText()));
                double costPrice;
                if ("Child".equals(cbType.getValue().getType())) {
                    costPrice = 0.0; // Set cost price to 0 for child products
                } else {
                    costPrice = Double.parseDouble(tfCostPrice.getText());
                }
                product.setCostPrice(costPrice);
                product.setDiscount(tfDiscount.getText().isEmpty() ? 0.0
                        : Double.parseDouble(tfDiscount.getText()));
                product.setMeasure(Float.parseFloat(tfMeasure.getText()));
                product.setBarCode(tfBarCode.getText());
                product.setProductUnitId(cbUnit.getValue());
                product.setBrandId(cbBrand.getValue());
                product.setProductStatusId(getProductStatus());
                em.persist(product);

                // save new product's product_has_product_type
                ProductHasProductType productHasProductType = new ProductHasProductType();
                productHasProductType.setProductId(product);
                productHasProductType.setProductTypeId(cbType.getValue());
                // check if this product is a parent product or a child product
                if (type.equals("Child")) {
                    productHasProductType.setReferenceId(parentProduct);
                } else {
                    productHasProductType.setReferenceId(product);
                }
                em.persist(productHasProductType);

                if (selectedImageFile != null) {
                    String extension = getFileExtension(selectedImageFile.getName());
                    String imageName = "product_" + product.getId() + extension;
                    saveProductImage(selectedImageFile, imageName);
                }

                // save this product in the store
                Store store = new Store();
                store.setProductId(product);
                store.setQty(0);
                em.persist(store);

                // save this product in the stock
                Stock stock = new Stock();
                stock.setProductId(product);
                stock.setQty(0);
                em.persist(stock);

                displayRegistrationMessage("Product successfully added to inventory.", true);

                clearRegistrationField();
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
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

    private void displayRegistrationMessage(String message, boolean action) {
        if (action) {
            registrationMessage.setStyle("-fx-text-fill: #0D9F00;"); // Green
        } else {
            registrationMessage.setStyle("-fx-text-fill: #FF3333;"); // Red
        }
        // Set professional message
        registrationMessage.setText(message);

        // Schedule message clearance
        PauseTransition delay = new PauseTransition(Duration.seconds(10));
        delay.setOnFinished(event -> registrationMessage.setText(""));
        delay.play();
    }

    public void setParentID(String id) {
        tfParentID.setText(id);
    }

    @FXML
    private void handlePopUpProductView(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (tfParentID.getText().isEmpty()) {
                try {
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("popUpProductList.fxml"));
                    Parent root = loader.load();

                    // Create a new stage for the popup
                    Stage popupStage = new Stage();
                    popupStage.initOwner(tfParentID.getScene().getWindow());
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
