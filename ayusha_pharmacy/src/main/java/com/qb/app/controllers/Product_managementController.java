package com.qb.app.controllers;

import com.qb.app.model.DefaultAPI;
import com.qb.app.model.SVGIconGroup;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class Product_managementController implements Initializable {

    // <editor-fold desc="FXML init component" defaultstate="collapsed">
    @FXML
    private Group iconPage;
    @FXML
    private TextField tfItemName;
    @FXML
    private TextField tfBarCode;
    @FXML
    private ComboBox<?> cbBrand;
    @FXML
    private TextField tfSalePrice;
    @FXML
    private TextField tfCostPrice;
    @FXML
    private TextField tfDiscount;
    @FXML
    private ComboBox<?> cbUnit;
    @FXML
    private TextField tfMeasure;
    @FXML
    private ComboBox<?> cbType;
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
    // </editor-fold>

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfCostPrice.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tfSalePrice.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tfDiscount.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tfMeasure.setTextFormatter(DefaultAPI.createNumericTextFormatter());

        iconPage.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
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

}
