package com.qb.app.controllers;

import com.jfoenix.controls.JFXToggleButton;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.InterfaceAction;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Product;
import com.qb.app.model.entity.ProductHasProductType;
import com.qb.app.model.entity.ProductStatus;
import com.qb.app.model.entity.ProductType;
import com.qb.app.model.getLogger;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
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

    public static Object callingController;
    @FXML
    private TextField tfSearch;
    @FXML
    private JFXToggleButton toggleParent;
    @FXML
    private ComboBox<String> FilterBy;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DefaultAPI.bindTableScroll(TableScroller, TableScrollContainer, TableBody);
        pageIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        closeIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/close-icon.svg"));
        loadProducts(null, false);
        LoadComboBox();
    }

    private void LoadComboBox() {
        FilterBy.getItems().addAll("ID", "Brand Name", "Product Name");
        FilterBy.setValue("Select Filter");

    }

    public void saveProductRegistrationController(Object controller) {
        this.callingController = controller;
    }

    private void loadProducts(String searchTerm, boolean isParent) {
        JPATransaction.runInTransaction((em) -> {
            TableBody.getChildren().clear();

            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Product> cQuery = cBuilder.createQuery(Product.class);
            Root<Product> product = cQuery.from(Product.class);
            Join<Product, ProductStatus> statusJoin = product.join("productStatusId", JoinType.INNER);
            Join<Product, ProductHasProductType> productTypeJoin = product.join("productHasProductTypeCollection", JoinType.LEFT);
            Join<ProductHasProductType, ProductType> typeJoin = productTypeJoin.join("productTypeId", JoinType.LEFT);

            Predicate finalPredicate = null;

            if (isParent) {
                finalPredicate = cBuilder.equal(typeJoin.get("type"), "Parent");
            }

            if (searchTerm != null && !searchTerm.isEmpty()) {
                String likePattern = "%" + searchTerm.toLowerCase() + "%";
                Predicate searchCondition = cBuilder.or(
                        cBuilder.like(cBuilder.lower(product.get("product")), likePattern),
                        cBuilder.like(cBuilder.lower(product.get("barCode")), likePattern)
                );
                finalPredicate = finalPredicate != null ? cBuilder.and(finalPredicate, searchCondition) : searchCondition;
            }

            if (finalPredicate != null) {
                cQuery.where(finalPredicate);
            }

            // 👉 Apply sorting based on ComboBox selection
            String selectedSort = FilterBy.getValue();
            if ("Product Name".equals(selectedSort)) {
                cQuery.orderBy(cBuilder.asc(product.get("product")));
            } else if ("Brand Name".equals(selectedSort)) {
                cQuery.orderBy(cBuilder.asc(product.get("brandId").get("brand")));
            } else if ("ID".equals(selectedSort)) {
                cQuery.orderBy(cBuilder.asc(product.get("id")));
            }

            cQuery.select(product);
            List<Product> productList = em.createQuery(cQuery).getResultList();
            
            System.out.println(cQuery.toString());

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
                    item.getProductStatusId().getStatus()
            );

            TableBody.getChildren().add(tableRow);
        } catch (IOException e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
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
        loadProducts(searchTerm, false);
    }

    @FXML
    private void handleProductTypeToggle(ActionEvent event) {
        String searchTerm = tfSearch.getText().trim();
        loadProducts(searchTerm, toggleParent.isSelected());
    }

    @FXML
    private void FilterChange(ActionEvent event) {
        String searchTerm = tfSearch.getText().trim();
        loadProducts(searchTerm, toggleParent.isSelected());
    }

}
