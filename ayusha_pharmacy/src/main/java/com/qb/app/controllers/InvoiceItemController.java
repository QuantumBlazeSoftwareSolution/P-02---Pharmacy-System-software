package com.qb.app.controllers;

import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Product;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InvoiceItemController implements Initializable {

    @FXML
    private Button btnDelete;
    private CashierInvoiceController invoiceController;
    private Node rootNode; // Add this field

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @FXML
    private Label itemCode;
    @FXML
    private ImageView itemImage;
    @FXML
    private Label itemName;
    @FXML
    private Label itemPrice;
    @FXML
    private Label qty;
    @FXML
    private Label itemAmount;
    @FXML
    private Group invoiceItemMinusIcon;
    @FXML
    private Group invoiceItemAddIcon;
    @FXML
    private Group invoiceItemDeleteIcon;

    private int productID;
    private double productQty;
    private double productPrice;
    @FXML
    private Button btnDecrease;
    @FXML
    private Button btnIncrease;
    private Product product;

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public double getProductQty() {
        return productQty;
    }

    public void setProductQty(double productQty) {
        this.productQty = productQty;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public void refreshDisplay() {
        this.qty.setText(String.valueOf(getProductQty()));
        this.itemAmount.setText(String.format("Rs. %, .2f", (product.getSalePrice() * getProductQty()) - (getProductQty() * product.getDiscount())));
        invoiceController.calculateInvoiceSummary();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIcons();
    }

    public void InvoiceItemData(String itemImage, double quantity, Product product, Node rootNode) {
        this.rootNode = rootNode;
        this.setProduct(product);
        setProductID(product.getId());
        setProductQty(quantity);
        setProductPrice(product.getSalePrice());
        this.itemCode.setText(String.valueOf(product.getId()));
        this.itemName.setText(product.getProduct());
        this.itemPrice.setText(String.format("Rs. %, .2f", product.getSalePrice()));
        this.qty.setText(String.valueOf(quantity));
        this.itemAmount.setText(String.format("Rs. %, .2f", (product.getSalePrice() * quantity) - (product.getSalePrice() * product.getDiscount())));
        calculateItemAmount();
        setItemImage(itemImage);
    }

    public void setItemImage(String imageUrl) {
        try {
            Image image = new Image(imageUrl);
            itemImage.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load image: " + imageUrl);
        }
    }

    private void calculateItemAmount() {
        try {
            // Parse the item price and quantity from the labels
            double price = product.getSalePrice();
            double quantity = Double.parseDouble(qty.getText());

            // Calculate the total amount
            double totalAmount = price * quantity;

            // Update the itemAmount label
            itemAmount.setText(String.format("Rs. %, .2f", totalAmount));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.err.println("Failed to calculate item amount: Invalid price or quantity format.");
        }
    }

    private void setIcons() {
        invoiceItemAddIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/invoiceItemPlus.svg"));
        invoiceItemMinusIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/invoiceItemMinus.svg"));
        invoiceItemDeleteIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/invoiceItemDelete.svg"));
    }

    @FXML
    private void invoiceItemAction(ActionEvent event) {
        if (event.getSource() == btnDecrease) {
            if (getProductQty() > 1) {
                setProductQty(getProductQty() - 1);
                refreshDisplay();
            }
        } else if (event.getSource() == btnIncrease) {
            setProductQty(getProductQty() + 1);
            refreshDisplay();
        } else if (event.getSource() == btnDelete) {
            if (invoiceController != null) {
                invoiceController.removeInvoiceItem(this);
            }
        }
    }

    public void saveInvoiceController(CashierInvoiceController invoiceController) {
        this.invoiceController = invoiceController;
    }

    // Add this method to get the root node
    public Node getRootNode() {
        return rootNode;
    }

}
