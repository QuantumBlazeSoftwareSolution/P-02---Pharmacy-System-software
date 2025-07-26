package com.qb.app.controllers;

import com.jfoenix.controls.JFXToggleButton;
import com.qb.app.model.InterfaceAction;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.TableModels.ProductPopUpModel;
import com.qb.app.model.entity.Product;
import com.qb.app.model.getLogger;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class PopUpProductListController implements Initializable {

    @FXML
    private Group pageIcon;
    @FXML
    private Group closeIcon;
    @FXML
    private AnchorPane root;

    @FXML
    private TextField tfSearch;
    @FXML
    private JFXToggleButton toggleParent;
    @FXML
    private ComboBox<String> FilterBy;
    @FXML
    private TableView<ProductPopUpModel> tableProduct;
    @FXML
    private TableColumn<ProductPopUpModel, Integer> colId;
    @FXML
    private TableColumn<ProductPopUpModel, String> colProduct;
    @FXML
    private TableColumn<ProductPopUpModel, String> colDepartment;
    @FXML
    private TableColumn<ProductPopUpModel, Double> colSalePrice;
    @FXML
    private TableColumn<ProductPopUpModel, Double> colCostPrice;
    @FXML
    private TableColumn<ProductPopUpModel, String> colUnit;
    @FXML
    private TableColumn<ProductPopUpModel, String> colMeasure;
    @FXML
    private TableColumn<ProductPopUpModel, Double> colDiscount;
    @FXML
    private TableColumn<ProductPopUpModel, String> colStatus;

    public static Object callingController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //        DefaultAPI.bindTableScroll(TableScroller, TableScrollContainer, TableBody);
        pageIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        closeIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/close-icon.svg"));

        tableConfiguration();

        loadProducts(null, false);
        LoadComboBox();
    }

    private void tableConfiguration() {
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getProduct().getId()).asObject());
        colProduct.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProduct().getProduct()));
        colDepartment.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProduct().getBrandId().getBrand()));
        colSalePrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getProduct().getSalePrice()).asObject());
        colCostPrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getProduct().getCostPrice()).asObject());
        colUnit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProduct().getProductUnitId().getUnit()));
        colMeasure.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getProduct().getMeasure())));
        colDiscount.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getProduct().getDiscount()).asObject());
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProduct().getProductStatusId().getStatus()));

        tableProduct.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tableProduct.getSelectionModel().isEmpty()) {
                ProductPopUpModel selectedModel = tableProduct.getSelectionModel().getSelectedItem();
                int productId = selectedModel.getProduct().getId();

                // Pass to calling controller
                if (callingController != null) {
                    try {
                        callingController
                                .getClass()
                                .getMethod("setParentID", String.class)
                                .invoke(callingController, String.valueOf(productId));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        getLogger.logger().warning("Failed to pass product ID: " + ex.getMessage());
                    }
                }

                closeWindow();
            }
        });

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
        tableProduct.setPlaceholder(progress);

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

                    // Sorting
                    String selectedSort = FilterBy.getValue();
                    if ("Product Name".equals(selectedSort)) {
                        cQuery.orderBy(cBuilder.asc(product.get("product")));
                    } else if ("Brand Name".equals(selectedSort)) {
                        cQuery.orderBy(cBuilder.asc(product.get("brandId").get("brand")));
                    } else if ("ID".equals(selectedSort)) {
                        cQuery.orderBy(cBuilder.asc(product.get("id")));
                    }

                    return em.createQuery(cQuery).getResultList();
                });
            }
        };

        loadTask.setOnSucceeded(e -> {
            List<Product> products = loadTask.getValue();
            List<ProductPopUpModel> models = products.stream()
                    .map(ProductPopUpModel::new)
                    .toList();

            Platform.runLater(() -> {
                tableProduct.getItems().setAll(models);

                if (models.isEmpty()) {
                    tableProduct.setPlaceholder(new Label("No products found."));
                }
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
