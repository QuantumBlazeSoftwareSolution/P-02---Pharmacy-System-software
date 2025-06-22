package com.qb.app.controllers;

import com.qb.app.model.ControllerClose;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.PopUp;
import com.qb.app.model.entity.Product;
import com.qb.app.model.entity.Stock;
import com.qb.app.model.getLogger;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    private Label labelItemName;
    @FXML
    private Text labelItemPrice;
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
    @FXML
    private Label labelItemNewPrice;
    @FXML
    private Separator salePriceSeparator;
    @FXML
    private Separator previewSeparator;
    @FXML
    private Label previewMessage;
    // </editor-fold>

    private double unitPrice = 0;
    private double itemQty = 1;
    private boolean isProductLoaded;
    private Product product;
    public PanelCashierController panelCashierController;

    public void setPanelCashier(PanelCashierController controller) {
        this.panelCashierController = controller;
    }

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
        Platform.runLater(() -> {
            tfItemCode.requestFocus();
        });
        salePriceSeparator.setVisible(false);
        salePriceSeparator.setManaged(false);
        labelItemNewPrice.setText("");
        labelItemPrice.setFill(Color.web("#00796F"));
        previewMessage.setText("");
        previewSeparator.setVisible(false);
        previewSeparator.setManaged(false);
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
        } else if (event.getSource() == btnProductView) {
            openProductView();
        } else if (event.getSource() == btnAdd) {
            addItemToInvoice();
        }
    }

    @FXML
    private void itemCodePressed(KeyEvent event) {
        if (!tfItemCode.getText().isEmpty()) {
            if (event.getCode() == KeyCode.ENTER) {
                loadPreviewProduct();
            }
        } else {
            if (event.getCode() == KeyCode.ENTER) {
                openProductView();
            }
        }
    }

    private void loadPreviewProduct() {
        String itemCode = tfItemCode.getText().trim();

        // Validate input before database query
        if (itemCode.isEmpty()) {
            showPreviewMessage("(CANNOT FIND THE PRODUCT - Empty code)");
            return;
        }

        try {
            JPATransaction.runInTransaction((em) -> {
                try {
                    // Convert to proper ID type (assuming Integer)
                    Integer productId = Integer.valueOf(itemCode);
                    Product product = em.find(Product.class, productId);

                    if (product != null) {
                        hidePreviewMessage();
                        double productPrice = product.getSalePrice() - product.getDiscount();
                        this.product = product;
                        setItemQty(1);
                        btnViewQty.setText("1");

                        Platform.runLater(() -> {
                            String imagePath = findProductImage(this.product.getId());
                            itemImage.setImage(new Image(imagePath));
                            labelItemName.setText(product.getProduct());
                            if (product.getDiscount() > 0) {
                                labelItemPrice.setStyle("-fx-strikethrough: true;");
                                labelItemPrice.setFill(Color.RED);
                                labelItemPrice.setText(String.format("Rs. %, .2f", product.getSalePrice()));
                                labelItemNewPrice.setText(String.format("Rs. %, .2f", productPrice));
                                salePriceSeparator.setVisible(true);
                                salePriceSeparator.setManaged(true);
                            } else {
                                labelItemPrice.setStyle("-fx-strikethrough: false;");
                                labelItemPrice.setFill(Color.web("#00796F"));
                                labelItemPrice.setText(String.format("Rs. %, .2f", product.getSalePrice()));
                                labelItemNewPrice.setText("");
                                salePriceSeparator.setVisible(false);
                                salePriceSeparator.setManaged(false);
                            }
                            if (product.getProductStatusId().getStatus().equals("Enable")) {
                                if (this.product.getBrandId().getProductStatusId().getStatus().equals("Enable")) {
                                    Stock stock = getProductStock(product);
                                    if (stock.getQty() <= 0) {
                                        showPreviewMessage("(Out of stock)");
                                    } else if (stock.getQty() < 20) {
                                        showPreviewMessage("(Low stock amount)");
                                    } else {
                                        hidePreviewMessage();
                                    }
                                } else {
                                    showPreviewMessage("(Department Not Available)");
                                }
                            } else {
                                showPreviewMessage("(Product Not Available)");
                            }
                            setUnitPrice(productPrice);
                            setItemPrice();
                        });
                        isProductLoaded = true;
                    } else {
                        showPreviewMessage("(CANNOT FIND THE PRODUCT)");
                    }
                } catch (NumberFormatException e) {
                    showPreviewMessage("(CANNOT FIND THE PRODUCT - Invalid code format)");
                }
            });
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            // Optionally show user-friendly error message
            getLogger.logger().warning(e.toString());
        }
    }

    private void hidePreviewMessage() {
        previewMessage.setText("");
        previewSeparator.setVisible(false);
        previewSeparator.setManaged(false);
    }

    private void showPreviewMessage(String text) {
        previewMessage.setText(text);
        previewSeparator.setVisible(true);
        previewSeparator.setManaged(true);
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
                        event.consume();
                    }
                    case MINUS, SUBTRACT -> {
                        decreaseQty();
                        event.consume();
                        event.consume();
                    }
                    case ENTER -> {
                        if (!tfItemCode.getText().isEmpty()) {
                            if (isProductLoaded) {
                                addItemToInvoice();
                            } else {
                                loadPreviewProduct();
                            }
                        } else {
                            openProductView();
                        }
                        event.consume();
                    }
                    case DIVIDE -> {
                        if (!invoiceItemList.isEmpty()) {
                            openPaymentPanel();
                            event.consume();
                        }
                    }
                    case F5 -> {
                        clearLoadProduct();
                        event.consume();
                    }
                    case F1 -> {
                        openProductView();
                        event.consume();
                    }
                    default -> {

                    }
                }
            }
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
            isProductLoaded = false;
        } catch (IOException e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
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
        return getClass().getResource("/com/qb/app/assets/images/empty_product.png").toExternalForm();
    }

    private void clearLoadProduct() {
        tfItemCode.setText("");
        tfBarCode.setText("");
        labelItemName.setText("Product name");
        labelItemPrice.setText("Rs. 0.00");
        itemPrice.setText("Rs. 0.00");
        btnViewQty.setText("0");
        itemImage.setImage(new Image(getClass().getResource("/com/qb/app/assets/images/empty_product.png").toExternalForm()));
        isProductLoaded = false;
        labelItemNewPrice.setText("");
        labelItemPrice.setStyle("-fx-strikethrough: false;");
        labelItemPrice.setFill(Color.web("#00796F"));
        salePriceSeparator.setVisible(false);
        salePriceSeparator.setManaged(false);
        hidePreviewMessage();
        this.product = null;
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
            PopUp.showPopupAndWait(
                    "fxmlPanel/InvoicePayment.fxml",
                    btnPayment,
                    this.root.getScene(),
                    PopUp.PopupType.CENTERED_80_WIDTH,
                    (InvoicePaymentController controller) -> {
                        controller.saveProductRegistrationController(this);
                        controller.setItems(invoiceItemList);
                    }
            );
//            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxmlPanel/InvoicePayment.fxml"));
//            Parent root = loader.load();
//
//            Stage popupStage = new Stage();
//            popupStage.initOwner(btnPayment.getScene().getWindow());
//            popupStage.initModality(Modality.APPLICATION_MODAL);
//
//            Scene mainScene = this.root.getScene();
//            GaussianBlur blur = new GaussianBlur(10);
//            mainScene.getRoot().setEffect(blur);
//
//            popupStage.setOnHidden(e -> mainScene.getRoot().setEffect(null));
//
//            Screen screen = Screen.getPrimary();
//            Rectangle2D bounds = screen.getVisualBounds();
//
//            // Calculate 80% of screen width
//            double eightyPercentWidth = bounds.getWidth() * 0.8;
//
//            Scene scene = new Scene(root);
//            popupStage.setScene(scene);
//
//            // Set width to 80% of screen
//            popupStage.setWidth(eightyPercentWidth);
//
//            // Center the stage horizontally
//            popupStage.setX((bounds.getWidth() - eightyPercentWidth) / 2);
//
//            // Center vertically
//            popupStage.setY((bounds.getHeight() - popupStage.getHeight()) / 2);
//
//            popupStage.initStyle(StageStyle.TRANSPARENT);
//
//            InvoicePaymentController controller = loader.getController();
//            controller.saveProductRegistrationController(this);
//            controller.setItems(invoiceItemList);
//
//            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void openProductView() {
        try {
            PopUp.showPopupAndWait(
                    "popUpCashierProductList.fxml",
                    root,
                    this.root.getScene(),
                    PopUp.PopupType.CENTERED_80_WIDTH,
                    (PopUpCashierProductListController controller) -> {
                        controller.saveCallingController(this);
                    }
            );

//            FXMLLoader loader = new FXMLLoader(App.class.getResource("popUpCashierProductList.fxml"));
//            Parent root = loader.load();
//
//            // Create a new stage for the popup
//            Stage popupStage = new Stage();
//            popupStage.initOwner(this.root.getScene().getWindow());
//            popupStage.initModality(Modality.APPLICATION_MODAL);
//
//            // Get screen dimensions
//            Screen screen = Screen.getPrimary();
//            Rectangle2D bounds = screen.getVisualBounds();
//
//            // Create scene with full width but original height
//            Scene scene = new Scene(root);
//            popupStage.setScene(scene);
//
//            // Set width to screen width and position at x=0
//            popupStage.setWidth(bounds.getWidth());
//            popupStage.setX(0); // This ensures no left gap
//
//            // Set fixed height (adjust as needed)
//            popupStage.setHeight(600);
//
//            // Center the popup vertically
//            popupStage.setY((bounds.getHeight() - popupStage.getHeight()) / 2);
//
//            popupStage.initStyle(StageStyle.TRANSPARENT);
//
//            // Get controller reference
//            PopUpCashierProductListController controller = loader.getController();
//            controller.saveCallingController(this);
//
//            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    public void setParentID(String text) {
        tfItemCode.setText(text);
        tfItemCode.requestFocus();
    }

    private Stock getProductStock(Product product) {
        return JPATransaction.runInTransaction((em) -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Stock> cq = cb.createQuery(Stock.class);
            Root<Stock> stockTable = cq.from(Stock.class);

            Predicate prdct = cb.equal(stockTable.get("productId"), product);
            cq.where(prdct);

            Stock stock = em.createQuery(cq).getSingleResult();
            return stock;
        });
    }

    private void addItemToInvoice() {
        if (isProductLoaded) {
            if (this.product.getProductStatusId().getStatus().equals("Enable")) {
                if (this.product.getBrandId().getProductStatusId().getStatus().equals("Enable")) {
                    addInvoiceItem();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Department Disabled - Action Restricted");
                    alert.setHeaderText("Product Department Currently Inactive");
                    alert.setContentText("The department associated with this product has been deactivated.\n\n"
                            + "To proceed, please either:\n"
                            + "• Reactivate the department in system settings, or\n"
                            + "• Select an alternative product from an active department");

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(getClass().getResource("/com/qb/app/assets/images/logo.png").toExternalForm()));

                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Product Disabled - Action Restricted");
                alert.setHeaderText("This Product is Disabled");
                alert.setContentText("The selected product is currently disabled and cannot be added to the invoice.\n\nPlease enable the product or choose an alternative.");

                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResource("/com/qb/app/assets/images/logo.png").toExternalForm()));

                alert.show();
            }
            clearLoadProduct();
        }
    }
}
