package com.qb.app.controllers.inventory;

import com.qb.app.model.CRUD.StockAdjustmentCRUD;
import com.qb.app.model.CRUD.StockCRUD;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.TableModels.StockAdjustmentItemTable;
import com.qb.app.model.TableModels.StockAdjustmentTable;
import com.qb.app.model.entity.Stock;
import com.qb.app.model.entity.StockAdjustment;
import com.qb.app.model.entity.StockAdjustmentItem;
import com.qb.app.session.ApplicationSession;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Vihanga
 */
public class Inventory_stock_adjustmentController implements Initializable {

    @FXML
    private TableView<StockAdjustmentTable> table1;
    @FXML
    private TableColumn<StockAdjustmentTable, String> table1StockId;
    @FXML
    private TableColumn<StockAdjustmentTable, String> table1ItemName;
    @FXML
    private TableColumn<StockAdjustmentTable, String> table1Qty;
    @FXML
    private TableColumn<StockAdjustmentTable, String> table1CostPrice;
    @FXML
    private TableColumn<StockAdjustmentTable, String> table1SalePrice;
    @FXML
    private TextField tfItemName;
    @FXML
    private TextField tfPreviousQty;
    @FXML
    private TextField tfNewQty;
    @FXML
    private Button btnAddToList;
    @FXML
    private TableView<StockAdjustmentItemTable> table2;
    @FXML
    private TableColumn<StockAdjustmentItemTable, String> table2ItemName;
    @FXML
    private TableColumn<StockAdjustmentItemTable, Double> table2Qty;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnQuantityChange;
    private StockAdjustmentTable selectedRow;
    @FXML
    private TextField tfReason;
    @FXML
    private TextField tfDateTime;

    private static final int STOCK_ADJUSTMENT_COUNT = 3;
    private static int thisMonthsAdjustmentCount = 0;
    private FilteredList<StockAdjustmentTable> filteredData;
    @FXML
    private TextField tfSearch;
    @FXML
    private Label labelAvailableStockCount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        interfaceInitializer();
    }

    @FXML
    private void handleActionEvent(ActionEvent event) {
        if (event.getSource() == btnAddToList) {
            addToList();
        } else if (event.getSource() == btnQuantityChange) {
            btnQuantityChange.setText("Processing Adjustments...");
            btnQuantityChange.setDisable(true);

            applyStockAdjustments();

            btnQuantityChange.setText("Update Inventory");
            btnQuantityChange.setDisable(false);
        }
    }

    private void interfaceInitializer() {
        refreshPage();
        loadStocks();
        configureTable1();
        showCurrentTime();
        loadCurrentMonthAdjustments();
    }

    private void loadCurrentMonthAdjustments() {
        Task<List<StockAdjustment>> task = new Task() {
            @Override
            protected List<StockAdjustment> call() throws Exception {
                return StockAdjustmentCRUD.getThisMonthStockAdjustments();
            }
        };

        task.setOnSucceeded((t) -> {
            List<StockAdjustment> value = task.getValue();
            Inventory_stock_adjustmentController.thisMonthsAdjustmentCount = value.size();
            labelAvailableStockCount.setText(String.valueOf(value.size()));
        });

        task.setOnFailed((t) -> {
            Throwable ex = task.getException();
            ex.printStackTrace();

            CustomAlert.showStyledAlert(
                    table1,
                    "Unable to retrieve the monthly adjustment history. Please check your connection or try again later.",
                    "Data Sync Error",
                    Alert.AlertType.WARNING
            );
        });

        new Thread(task).start();
    }

    private void loadStocks() {
        table1.getItems().clear();

        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setMaxSize(50, 50);
        table1.setPlaceholder(spinner);

        Task<List<Stock>> stockLoadingTask = new Task() {
            @Override
            protected List<Stock> call() throws Exception {
                List<Stock> stockList = StockCRUD.getStock();

                return stockList;
            }
        };

        stockLoadingTask.setOnSucceeded((t) -> {
            List<Stock> stockList = stockLoadingTask.getValue();
            ObservableList<StockAdjustmentTable> masterData = FXCollections.observableArrayList();

            for (Stock stock : stockList) {
                masterData.add(new StockAdjustmentTable(stock));
            }

            filteredData = new FilteredList<>(masterData, p -> true);

            table1.setItems(filteredData);

            setupSearch();
        });

        stockLoadingTask.setOnFailed((t) -> {
            stockLoadingTask.getException().printStackTrace();

            table1.setPlaceholder(new Label("Failed to load data"));
        });

        new Thread(stockLoadingTask).start();
    }

    private void setupSearch() {
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(row -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (row.getItemName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (row.getStockId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });
    }

    private void configureTable1() {
        // table 1
        table1StockId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStockId()));
        table1ItemName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItemName()));
        table1Qty.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getQty()));
        table1CostPrice.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCostPrice()));
        table1SalePrice.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSalePrice()));

        // table 2
        table2ItemName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItemName()));
        table2Qty.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getQty()).asObject());
    }

    @FXML
    private void table1MouseClicker(MouseEvent event) {
        if (event.getClickCount() == 2 && table1.getSelectionModel().getSelectedItem() != null) {
            StockAdjustmentTable row = table1.getSelectionModel().getSelectedItem();

            this.selectedRow = row;
            tfItemName.setText(row.getItemName());
            tfPreviousQty.setText(row.getQty());
        }
    }

    private void refreshPage() {
        this.selectedRow = null;
        tfItemName.setText("");
        tfPreviousQty.setText("");
        tfReason.setText("");
        table2.getItems().clear();
        table2.refresh();
    }

    private void addToList() {
        if (this.selectedRow != null) {

            String newQty = tfNewQty.getText();

            if (newQty != null && !newQty.trim().isEmpty()) {
                try {
                    Double qty = Double.valueOf(newQty);
                    if (qty > 0) {
                        StockAdjustmentItemTable row = new StockAdjustmentItemTable(
                                qty,
                                this.selectedRow.getStock()
                        );

                        this.selectedRow = null;
                        tfItemName.setText("");
                        tfPreviousQty.setText("");
                        tfNewQty.setText("");

                        table2.getItems().add(row);
                    } else {
                        CustomAlert.showStyledAlert(
                                table1,
                                "Quantity must be greater than 0.",
                                "Invalid Quantity",
                                Alert.AlertType.WARNING
                        );
                    }

                } catch (NumberFormatException e) {
                    CustomAlert.showStyledAlert(
                            table1,
                            "Please enter a valid numeric quantity.",
                            "Invalid Input",
                            Alert.AlertType.WARNING
                    );
                }

            } else {
                CustomAlert.showStyledAlert(
                        table1,
                        "Please enter the new quantity before proceeding.",
                        "Missing Quantity",
                        Alert.AlertType.WARNING
                );
            }
        } else {
            CustomAlert.showStyledAlert(
                    table1,
                    "Please select a stock item before adjusting quantity.",
                    "No Item Selected",
                    Alert.AlertType.WARNING
            );
        }
    }

    private void applyStockAdjustments() {
        String reason = tfReason.getText();
        if (reason != null && !reason.trim().isEmpty()) {
            if (Inventory_stock_adjustmentController.thisMonthsAdjustmentCount < STOCK_ADJUSTMENT_COUNT) {
                if (!table2.getItems().isEmpty()) {
                    List<StockAdjustmentItemTable> adjustmentList = table2.getItems();
                    updateStockQuantities(adjustmentList, () -> createStockAdjustment(reason));
                } else {
                    CustomAlert.showStyledAlert(
                            table1,
                            "No items to process. Please add at least one stock adjustment.",
                            "Empty Adjustment List",
                            Alert.AlertType.WARNING
                    );
                }
            } else {
                CustomAlert.showStyledAlert(
                        table1,
                        "Stock adjustments for this month have already been completed.",
                        "Adjustment Limit Reached",
                        Alert.AlertType.WARNING
                );
            }
        } else {
            CustomAlert.showStyledAlert(
                    table1,
                    "Please enter a reason for this stock adjustment before proceeding.",
                    "Missing Reason",
                    Alert.AlertType.WARNING
            );
        }
    }

    private void updateStockQuantities(List<StockAdjustmentItemTable> adjustmentList, Runnable onSuccessCallback) {

        Task<Void> updateQtyTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (StockAdjustmentItemTable item : adjustmentList) {
                    Stock stock = item.getStock();
                    stock.setQty(item.getQty());

                    JPATransaction.runInTransaction((em) -> {
                        em.merge(stock);
                    });
                }
                return null;
            }
        };

        updateQtyTask.setOnSucceeded((t) -> {
            if (onSuccessCallback != null) {
                onSuccessCallback.run();
            }
        });

        updateQtyTask.setOnFailed((t) -> {
            CustomAlert.showStyledAlert(
                    table1,
                    "Failed to update stock quantities. No changes were saved.",
                    "Update Failed",
                    Alert.AlertType.ERROR
            );
            updateQtyTask.getException().printStackTrace();
        });

        new Thread(updateQtyTask).start();
    }

    private void createStockAdjustment(String reason) {
        List<StockAdjustmentItemTable> adjList = new ArrayList<>(table2.getItems());

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                StockAdjustment adjustment = new StockAdjustment();
                adjustment.setEmployeeId(ApplicationSession.getEmployee());
                adjustment.setReason(reason);
                adjustment.setDateTime(new Date());

                JPATransaction.runInTransaction((em) -> {
                    em.persist(adjustment);
                    em.flush();

                    for (StockAdjustmentItemTable item : adjList) {
                        StockAdjustmentItem itemRow = new StockAdjustmentItem();
                        itemRow.setProductId(item.getStock().getProductId());
                        itemRow.setQty(item.getQty());
                        itemRow.setStockAdjustmentId(adjustment);
                        em.persist(itemRow);
                    }
                    em.flush();
                });

                return null;
            }
        };

        task.setOnSucceeded(e -> {
            CustomAlert.showStyledAlert(
                    table1,
                    "Stock quantities and adjustment saved successfully.",
                    "Success",
                    Alert.AlertType.INFORMATION
            );
            refreshPage();
            loadStocks();
            loadCurrentMonthAdjustments();
        });

        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            CustomAlert.showStyledAlert(
                    table1,
                    "Failed to create stock adjustment.",
                    "Error",
                    Alert.AlertType.ERROR
            );
        });

        new Thread(task).start();
    }

    private void showCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = sdf.format(new Date());
        tfDateTime.setText(currentDateTime);
    }
}
