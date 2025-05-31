package com.qb.app.controllers;

import com.qb.app.model.DefaultAPI;
import com.qb.app.model.InterfaceAction;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Product;
import com.qb.app.model.entity.ProductStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class PopUpProductListController implements Initializable {

    @FXML
    private Group pageIcon;
    @FXML
    private Group closeIcon;
    @FXML
    private ScrollPane TableScrollContainer;
    @FXML
    private VBox TableBody;
    @FXML
    private ScrollBar TableScroller;
    @FXML
    private AnchorPane root;

    public static Product_registrationController productRegistrationController;
    @FXML
    private TextField tfSearch;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DefaultAPI.bindTableScroll(TableScroller, TableScrollContainer, TableBody);
        pageIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        closeIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/close-icon.svg"));
        loadProducts(null);
    }

    public void saveProductRegistrationController(Product_registrationController controller) {
        this.productRegistrationController = controller;
    }

    private void loadProducts(String searchTerm) {
        JPATransaction.runInTransaction((em) -> {
            // Clear existing items
            TableBody.getChildren().clear();

            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Product> cQuery = cBuilder.createQuery(Product.class);
            Root<Product> product = cQuery.from(Product.class);
            Join<Product, ProductStatus> statusJoin = product.join("productStatusId", JoinType.INNER);

            // Base condition - only enabled products
            Predicate baseCondition = cBuilder.equal(statusJoin.get("status"), "Enable");

            // Add search condition if search term exists
            if (searchTerm != null && !searchTerm.isEmpty()) {
                String likePattern = "%" + searchTerm.toLowerCase() + "%";
                Predicate searchCondition = cBuilder.or(
                        cBuilder.like(cBuilder.lower(product.get("product")), likePattern),
                        cBuilder.like(cBuilder.lower(product.get("barCode")), likePattern)
                );
                cQuery.where(cBuilder.and(baseCondition, searchCondition));
            } else {
                cQuery.where(baseCondition);
            }

            cQuery.select(product);
            List<Product> productList = em.createQuery(cQuery).getResultList();

            // Create table rows for each product
            for (Product item : productList) {
                createProductTableRow(item);
            }
        });
    }

    private void createProductTableRow(Product item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/qb/app/fxmlComponent/popUp_ProductList_TableRow.fxml"));
            Node tableRow = loader.load();
            PopUp_ProductList_TableRowController controller = loader.getController();

            // Set controllers
            controller.setPopUpController(this);

            // Set item data
            controller.setItems(
                    item.getId().toString(),
                    item.getProduct(),
                    item.getBrandId().getBrand(),
                    item.getSalePrice(),
                    item.getCostPrice(),
                    item.getProductUnitId().getUnit(),
                    String.valueOf(item.getMeasure()),
                    item.getDiscount(),
                    item.getBarCode()
            );

            TableBody.getChildren().add(tableRow);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void closePopUp(MouseEvent event) {
        InterfaceAction.closeWindow(root);
    }

    public void closeWindow() {
        InterfaceAction.closeWindow(root);
    }

    @FXML
    private void handleSearchKeyPressed(KeyEvent event) {
        String searchTerm = tfSearch.getText().trim();
        loadProducts(searchTerm);
    }

}
