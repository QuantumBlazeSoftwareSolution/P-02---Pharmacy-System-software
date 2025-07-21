package com.qb.app.controllers;

import com.jfoenix.controls.JFXToggleButton;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.InterfaceAction;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Product;
import com.qb.app.model.getLogger;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class PopUpProductListController implements Initializable {

    @FXML
    private Group pageIcon;
    @FXML
    private Group closeIcon;
    @FXML
    private ScrollPane TableScrollContainer;
    @FXML
    private ListView<Product> TableBody;
//    private ScrollBar TableScroller;
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
        TableBody.setCellFactory(lv -> new ProductListCell());
//        DefaultAPI.bindTableScroll(TableScroller, TableScrollContainer, TableBody);
        pageIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        closeIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/close-icon.svg"));

        loadProducts(null, false);
        LoadComboBox();
    }

    private class ProductListCell extends ListCell<Product> {

        private final Map<Integer, Node> rowCache = new HashMap<>();

        @Override
        protected void updateItem(Product item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                Node tableRow = rowCache.computeIfAbsent(item.getId(), k -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                                "/com/qb/app/fxmlComponent/popUp_ProductList_TableRow.fxml"));
                        Node row = loader.load();
                        PopUp_ProductList_TableRowController controller = loader.getController();
                        controller.setPopUpController(PopUpProductListController.this);
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
                        return row;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                });
                setGraphic(tableRow);
            }
        }
    }

    private void LoadComboBox() {
        FilterBy.getItems().addAll("ID", "Brand Name", "Product Name");
        FilterBy.setValue("Select Filter");
    }

    public void saveProductRegistrationController(Object controller) {
        this.callingController = controller;
    }

    private void loadProducts(String searchTerm, boolean isParent) {
        ProgressIndicator progress = new ProgressIndicator();
        progress.setMaxSize(50, 50);

        TableBody.setPlaceholder(progress);

        // Create a task for background processing
        Task<List<Product>> loadTask = new Task<>() {
            @Override
            protected List<Product> call() throws Exception {
                return JPATransaction.runInTransaction(em -> {
                    CriteriaBuilder cBuilder = em.getCriteriaBuilder();
                    CriteriaQuery<Product> cQuery = cBuilder.createQuery(Product.class);
                    Root<Product> product = cQuery.from(Product.class);

                    // Add fetch joins to prevent N+1 queries
                    product.fetch("productStatusId", JoinType.INNER);
                    product.fetch("brandId", JoinType.INNER);
                    product.fetch("productUnitId", JoinType.INNER);
                    product.fetch("productHasProductTypeCollection", JoinType.LEFT)
                            .fetch("productTypeId", JoinType.LEFT);

                    // Your existing predicate logic
                    Predicate finalPredicate = null;
                    if (isParent) {
                        finalPredicate = cBuilder.equal(product.join("productHasProductTypeCollection")
                                .join("productTypeId")
                                .get("type"), "Parent");
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

                    String selectedSort = FilterBy.getValue();
                    if (selectedSort != null) {
                        switch (selectedSort) {
                            case "Product Name" -> cQuery.orderBy(cBuilder.asc(product.get("product")));
                            case "Brand Name" -> cQuery.orderBy(cBuilder.asc(product.get("brandId").get("brand")));
                            case "ID" -> cQuery.orderBy(cBuilder.asc(product.get("id")));
                            default -> cQuery.orderBy(cBuilder.asc(product.get("id")));
                        }
                    } else {
                        cQuery.orderBy(cBuilder.asc(product.get("id")));
                    }

                    return em.createQuery(cQuery).getResultList();
                });
            }
        };

        // Update UI when task succeeds
        loadTask.setOnSucceeded(e -> {
            Platform.runLater(() -> {
                TableBody.getItems().setAll(loadTask.getValue());
            });
        });

        // Handle errors
        loadTask.setOnFailed(e -> {
            getLogger.logger().warning("Failed to load products: " + loadTask.getException());
        });

        // Start the task in a new thread
        new Thread(loadTask).start();
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
