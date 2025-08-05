package com.qb.app.controllers;

import com.qb.app.model.InterfaceAction;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.TableConfig;
import com.qb.app.model.TableModels.ProductTableModel;
import com.qb.app.model.entity.Product;
import com.qb.app.model.getLogger;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class PopUpCashierProductListController implements Initializable {

    @FXML
    private Group pageIcon;
    @FXML
    private Group closeIcon;
//    private ListView<Product> tableBody;
    @FXML
    private AnchorPane root;
    @FXML
    private TextField tfSearch;
    @FXML
    private ComboBox<String> FilterBy;

    public static CashierInvoiceController callingController;
    @FXML
    private TableView<ProductTableModel> table;
    @FXML
    private TableColumn<ProductTableModel, Integer> colId;
    @FXML
    private TableColumn<ProductTableModel, String> colProduct;
    @FXML
    private TableColumn<ProductTableModel, String> colDepartment;
    @FXML
    private TableColumn<ProductTableModel, Double> colSalePrice;
    @FXML
    private TableColumn<ProductTableModel, Double> colCostPrice;
    @FXML
    private TableColumn<ProductTableModel, String> colUnit;
    @FXML
    private TableColumn<ProductTableModel, Double> colMeasure;
    @FXML
    private TableColumn<ProductTableModel, Double> colDiscount;
    @FXML
    private TableColumn<ProductTableModel, String> colStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        DefaultAPI.bindTableScroll(TableScroller, TableScrollContainer, tableBody);
        pageIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        closeIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/close-icon.svg"));

//        tableBody.setCellFactory(lv -> new ProductListCell());
        configureTableColumns();
        setupDoubleClickHandler();
        loadProducts(null);
        loadComboBox();
    }

    private void setupDoubleClickHandler() {
        table.setRowFactory(tv -> {
            TableRow<ProductTableModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    ProductTableModel selectedProduct = row.getItem();
                    if (callingController != null) {
                        callingController.setParentID(String.valueOf(selectedProduct.getId()));
                        closeWindow();  // Close the popup after selection
                    }
                }
            });
            return row;
        });
    }

    private void configureTableColumns() {
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colProduct.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductName()));
        colDepartment.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBrandName()));
        colSalePrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getSalePrice()).asObject());
        colCostPrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCostPrice()).asObject());
        colUnit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUnit()));
        colMeasure.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getMeasure()).asObject());
        colDiscount.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getDiscount()).asObject());
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        // Add similar formatters for other numeric columns...

        TableConfig.formatDecimalColumn(colSalePrice);
        TableConfig.formatDecimalColumn(colCostPrice);
        TableConfig.formatDecimalColumn(colMeasure);
        TableConfig.formatDecimalColumn(colDiscount);
    }

//    private class ProductListCell extends ListCell<Product> {
//
//        @Override
//        protected void updateItem(Product item, boolean empty) {
//            super.updateItem(item, empty);
//            if (empty || item == null) {
//                setGraphic(null);
//            } else {
//                try {
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
//                            "/com/qb/app/fxmlComponent/popUpCashierProductList_TableRow.fxml"));
//                    Node tableRow = loader.load();
//                    PopUpCashierProductList_TableRowController controller = loader.getController();
//
//                    controller.setPopUpController(PopUpCashierProductListController.this);
//                    controller.setItems(
//                            item.getId().toString(),
//                            item.getProduct(),
//                            item.getBrandId().getBrand(),
//                            item.getSalePrice(),
//                            item.getCostPrice(),
//                            item.getProductUnitId().getUnit(),
//                            String.valueOf(item.getMeasure()),
//                            item.getDiscount(),
//                            item.getProductStatusId().getStatus()
//                    );
//
//                    setGraphic(tableRow);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    getLogger.logger().warning(e.toString());
//                }
//            }
//        }
//    }
    private void loadComboBox() {
        FilterBy.getItems().addAll("ID", "Brand Name", "Product Name");
        FilterBy.setValue("Select Filter");
    }

//    private void loadProducts(String searchTerm) {
//        ProgressIndicator progress = new ProgressIndicator();
//        progress.setMaxSize(50, 50);
//        tableBody.setPlaceholder(progress);
//
//        Task<List<Product>> loadTask = new Task<>() {
//            @Override
//            protected List<Product> call() throws Exception {
//                return JPATransaction.runInTransaction(em -> {
//                    CriteriaBuilder cBuilder = em.getCriteriaBuilder();
//                    CriteriaQuery<Product> cQuery = cBuilder.createQuery(Product.class);
//                    Root<Product> product = cQuery.from(Product.class);
//
//                    // Add fetch joins to prevent N+1 queries
//                    product.fetch("productStatusId", JoinType.INNER);
//                    product.fetch("brandId", JoinType.INNER);
//                    product.fetch("productUnitId", JoinType.INNER);
//
//                    // Base condition - only enabled products
//                    Predicate baseCondition = cBuilder.equal(
//                            product.get("productStatusId").get("status"), "Enable");
//
//                    // Add search condition if search term exists
//                    if (searchTerm != null && !searchTerm.isEmpty()) {
//                        String likePattern = "%" + searchTerm.toLowerCase() + "%";
//                        Predicate searchCondition = cBuilder.or(
//                                cBuilder.like(cBuilder.lower(product.get("product")), likePattern),
//                                cBuilder.like(cBuilder.lower(product.get("barCode")), likePattern)
//                        );
//                        baseCondition = cBuilder.and(baseCondition, searchCondition);
//                    }
//
//                    cQuery.where(baseCondition);
//
//                    // Apply sorting
//                    String selectedSort = FilterBy.getValue();
//                    if ("Product Name".equals(selectedSort)) {
//                        cQuery.orderBy(cBuilder.asc(product.get("product")));
//                    } else if ("Brand Name".equals(selectedSort)) {
//                        cQuery.orderBy(cBuilder.asc(product.get("brandId").get("brand")));
//                    } else if ("ID".equals(selectedSort)) {
//                        cQuery.orderBy(cBuilder.asc(product.get("id")));
//                    }
//
//                    return em.createQuery(cQuery).getResultList();
//                });
//            }
//        };
//
//        loadTask.setOnSucceeded(e -> {
//            List<Product> products = loadTask.getValue();
//            Platform.runLater(() -> {
//                tableBody.getItems().setAll(products);
//            });
//        });
//
//        loadTask.setOnFailed(e -> {
//            getLogger.logger().warning("Failed to load products: " + loadTask.getException());
//        });
//
//        new Thread(loadTask).start();
//    }
    private void loadProducts(String searchTerm) {
        ProgressIndicator progress = new ProgressIndicator();
        progress.setMaxSize(50, 50);
        table.setPlaceholder(progress);

        Task<List<ProductTableModel>> loadTask = new Task<>() {
            @Override
            protected List<ProductTableModel> call() throws Exception {
                return JPATransaction.runInTransaction(em -> {
                    CriteriaBuilder cBuilder = em.getCriteriaBuilder();
                    CriteriaQuery<Product> cQuery = cBuilder.createQuery(Product.class);
                    Root<Product> product = cQuery.from(Product.class);

                    // Add fetch joins to prevent N+1 queries
                    product.fetch("productStatusId", JoinType.INNER);
                    product.fetch("brandId", JoinType.INNER);
                    product.fetch("productUnitId", JoinType.INNER);

                    // Base condition - only enabled products
                    Predicate baseCondition = cBuilder.equal(
                            product.get("productStatusId").get("status"), "Enable");

                    // Add search condition if search term exists
                    if (searchTerm != null && !searchTerm.isEmpty()) {
                        String likePattern = "%" + searchTerm.toLowerCase() + "%";
                        Predicate searchCondition = cBuilder.or(
                                cBuilder.like(cBuilder.lower(product.get("product")), likePattern),
                                cBuilder.like(cBuilder.lower(product.get("barCode")), likePattern),
                                cBuilder.like(cBuilder.lower(product.get("genericName")), likePattern)
                        );
                        baseCondition = cBuilder.and(baseCondition, searchCondition);
                    }

                    cQuery.where(baseCondition);

                    // Apply sorting
                    String selectedSort = FilterBy.getValue();
                    if ("Product Name".equals(selectedSort)) {
                        cQuery.orderBy(cBuilder.asc(product.get("product")));
                    } else if ("Brand Name".equals(selectedSort)) {
                        cQuery.orderBy(cBuilder.asc(product.get("brandId").get("brand")));
                    } else if ("ID".equals(selectedSort)) {
                        cQuery.orderBy(cBuilder.asc(product.get("id")));
                    }

                    List<Product> products = em.createQuery(cQuery).getResultList();
                    List<ProductTableModel> models = new ArrayList<>();

                    for (Product item : products) {
                        models.add(new ProductTableModel(
                                item.getId(),
                                item.getProduct(),
                                item.getGenericName(),
                                item.getBrandId().getBrand(),
                                item.getSalePrice(),
                                item.getCostPrice(),
                                item.getProductUnitId().getUnit(),
                                Double.valueOf(String.valueOf(item.getMeasure())),
                                item.getDiscount(),
                                item.getProductStatusId().getStatus()
                        ));
                    }

                    return models;
                });
            }
        };

        loadTask.setOnSucceeded(e -> {
            List<ProductTableModel> products = loadTask.getValue();
            Platform.runLater(() -> {
                table.getItems().setAll(products);
            });
        });

        loadTask.setOnFailed(e -> {
            getLogger.logger().warning("Failed to load products: " + loadTask.getException());
        });

        new Thread(loadTask).start();
    }

    public void closeWindow() {
        InterfaceAction.closeWindow(root);
    }

    @FXML
    private void handleSearchKeyPressed(KeyEvent event) {
        String searchTerm = tfSearch.getText().trim();
        loadProducts(searchTerm);
    }

    @FXML
    private void closePopUp(MouseEvent event) {
        InterfaceAction.closeWindow(root);
    }

    public void saveCallingController(CashierInvoiceController controller) {
        this.callingController = controller;
    }

    @FXML
    private void FilterByAction(ActionEvent event) {
        String searchTerm = tfSearch.getText().trim();
        loadProducts(searchTerm);
    }
}
