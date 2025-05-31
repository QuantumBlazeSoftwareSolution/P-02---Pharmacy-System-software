package com.qb.app.controllers;

import com.qb.app.App;
import com.qb.app.model.ControllerClose;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.entity.Product;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CashierInvoiceController implements Initializable, ControllerClose {

    // <editor-fold desc="FXML init component" defaultstate="collapsed">
    @FXML
    private VBox invoiceItemContainer;
    @FXML
    private ScrollPane invoiceScrollContainer;
    @FXML
    private ImageView itemImage;
    @FXML
    private ScrollBar invoiceScroller;
    @FXML
    private TextField tfBarCode;
    @FXML
    private TextField tfItemCode;
    @FXML
    private Button btnProductView;
    @FXML
    private Label invoiceNumber;
    @FXML
    private Label labelItemName;
    @FXML
    private Label labelItemPrice;
    @FXML
    private Button btnDecreaseQty;
    @FXML
    private Button btnViewQty;
    @FXML
    private Button btnIncreaseQty;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnAdd;
    @FXML
    private Label invoiceItemCount;
    @FXML
    private Label invoiceSubTotal;
    @FXML
    private Label invoiceDiscount;
    @FXML
    private Label invoiceTotal;
    @FXML
    private Button btnPayment;
    @FXML
    private Button itemPrice;
    @FXML
    private AnchorPane root;
    // </editor-fold>

    private double unitPrice = 0;
    private double itemQty = 1;
    private boolean isProductLoaded;
    private Product product;

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getItemQty() {
        return itemQty;
    }

    public void setItemQty(double itemQty) {
        this.itemQty = itemQty;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DefaultAPI.bindTableScroll(invoiceScroller, invoiceScrollContainer, invoiceItemContainer);
        tfItemCode.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        setEventListener();
    }

    @Override
    public void close() {
    }

    @FXML
    private void handleActionEvent(ActionEvent event) {
        if (event.getSource() == btnClear) {
            clearLoadProduct();
        } else if (event.getSource() == btnPayment) {
            if (!invoiceItemList.isEmpty()) {
                openPaymentPanel();
            }
        }
    }

    @FXML
    private void itemCodePressed(KeyEvent event) {
        if (event.getCode() == KeyCode.MULTIPLY) {
            loadPreviewProduct();
        }
    }

    private void loadPreviewProduct() {
        String itemCode = tfItemCode.getText().trim();

        // Validate input before database query
        if (itemCode.isEmpty()) {
            System.out.println("CANNOT FIND THE PRODUCT - Empty code");
            return;
        }

        try {
            JPATransaction.runInTransaction((em) -> {
                try {
                    // Convert to proper ID type (assuming Integer)
                    Integer productId = Integer.valueOf(itemCode);
                    Product product = em.find(Product.class, productId);

                    if (product != null) {
                        double productPrice = product.getSalePrice() - product.getDiscount();
                        this.product = product;
                        setItemQty(1);
                        btnViewQty.setText("1");

                        Platform.runLater(() -> {
                            String imagePath = findProductImage(this.product.getId());
                            itemImage.setImage(new Image(imagePath));
                            labelItemName.setText(product.getProduct());
                            labelItemPrice.setText(String.format("%, .2f", productPrice));
                            setUnitPrice(productPrice);
                            setItemPrice();
                        });
                        isProductLoaded = true;
                    } else {
                        System.out.println("CANNOT FIND THE PRODUCT");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("CANNOT FIND THE PRODUCT - Invalid code format");
                }
            });
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            // Optionally show user-friendly error message
        }
    }

    private void setItemPrice() {
        double itemPrice = getUnitPrice() * getItemQty();
        this.itemPrice.setText(String.format("Rs. %, .2f", itemPrice));
    }

    private void setEventListener() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (null != event.getCode()) {
                switch (event.getCode()) {
                    case PLUS, ADD -> {
                        event.consume();
                        increaseQty();
                    }
                    case MINUS, SUBTRACT -> {
                        decreaseQty();
                        event.consume();
                    }
                    case ENTER -> {
                        if (isProductLoaded) {
                            addInvoiceItem();
                            isProductLoaded = false;
                        }
                    }
                    case F5 -> {
                        clearLoadProduct();
                    }
                    default -> {
                    }
                }
            }
            System.out.println("Ky Code: " + event.getCode());
        });
    }

    private void increaseQty() {
        if (isProductLoaded) {
            setItemQty(getItemQty() + 1);
            setItemPrice();
            btnViewQty.setText(String.valueOf(getItemQty()));
        }
    }

    private void decreaseQty() {
        if (isProductLoaded) {
            if (getItemQty() > 1) {
                setItemQty(getItemQty() - 1);
                setItemPrice();
            } else {
                System.out.println("Quantity is less than 1");
            }
            btnViewQty.setText(String.valueOf(getItemQty()));
        }
    }

    List<InvoiceItemController> invoiceItemList = new ArrayList<>();

    private void addInvoiceItem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/qb/app/fxmlComponent/invoiceItem.fxml"));
            Node invoiceItem = loader.load();
            InvoiceItemController itemController = loader.getController();
            itemController.saveInvoiceController(this);

            // Get the product image path (check for multiple possible extensions)
            String imagePath = findProductImage(this.product.getId());

            itemController.InvoiceItemData(
                    imagePath,
                    getItemQty(),
                    this.product,
                    invoiceItem
            );

            boolean productExists = false;

            // Check if product already exists in the list
            for (InvoiceItemController invoiceItemController : invoiceItemList) {
                if (invoiceItemController.getProductID() == this.product.getId()) {
                    invoiceItemController.setProductQty(invoiceItemController.getProductQty() + getItemQty());
                    invoiceItemController.refreshDisplay();
                    productExists = true;
                    tfItemCode.setText("");
                    break;
                }
            }

            // If product doesn't exist, add new item
            if (!productExists) {
                invoiceItemList.add(itemController);
                invoiceItemContainer.getChildren().add(invoiceItem);
                tfItemCode.setText("");
            }
            calculateInvoiceSummary();
            clearLoadProduct();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds the product image by checking common extensions
     *
     * @param productId The product ID to search for
     * @return The image URL if found, empty string otherwise
     */
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

    private void clearLoadProduct() {
        tfItemCode.setText("");
        tfBarCode.setText("");
        labelItemName.setText("Product name");
        labelItemPrice.setText("Rs. 0.00");
        itemPrice.setText("Rs. 0.00");
        btnViewQty.setText("0");
        itemImage.setImage(new Image(getClass().getResource("/com/qb/app/assets/images/new_product_image.png").toExternalForm()));
        isProductLoaded = false;
    }

    public void calculateInvoiceSummary() {
        double itemCount = 0;
        double subTotal = 0;
        double discount = 0;
        for (InvoiceItemController invoiceItemController : invoiceItemList) {
            itemCount += invoiceItemController.getProductQty();
            subTotal += invoiceItemController.getProduct().getSalePrice() * invoiceItemController.getProductQty();
            discount += invoiceItemController.getProduct().getDiscount() * invoiceItemController.getProductQty();
        }
        invoiceItemCount.setText(String.valueOf(itemCount));
        invoiceSubTotal.setText(String.format("Rs. %, .2f", subTotal));
        invoiceDiscount.setText(String.format("Rs. %, .2f", discount));
        invoiceTotal.setText(String.format("Rs. %, .2f", (subTotal - discount)));
    }

    public void removeInvoiceItem(InvoiceItemController itemToRemove) {
        Node nodeToRemove = itemToRemove.getRootNode();

        invoiceItemList.remove(itemToRemove);
        invoiceItemContainer.getChildren().remove(nodeToRemove);

        calculateInvoiceSummary();
    }

    public void removeAll() {
        invoiceItemList.clear();
        invoiceItemContainer.getChildren().clear();
        calculateInvoiceSummary();
    }

    @FXML
    private void handleQuantityAmount(ActionEvent event) {
        if (event.getSource() == btnIncreaseQty) {
            increaseQty();
        } else if (event.getSource() == btnDecreaseQty) {
            decreaseQty();
        }
    }

    private void openPaymentPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxmlPanel/InvoicePayment.fxml"));
            Parent root = loader.load();

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.initOwner(btnPayment.getScene().getWindow());
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
//            popupStage.setHeight(600);
            // Center the popup vertically
            popupStage.setY((bounds.getHeight() - popupStage.getHeight()) / 2);

            popupStage.initStyle(StageStyle.TRANSPARENT);

            // Get controller reference
            InvoicePaymentController controller = loader.getController();
            controller.saveProductRegistrationController(this);
            controller.setItems(invoiceItemList);

            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
