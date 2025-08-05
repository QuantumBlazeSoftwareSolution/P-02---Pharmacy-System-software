package com.qb.app.controllers.report;

import com.qb.app.controllers.report.beans.GrnItemBean;
import com.qb.app.model.ComboBoxUtils;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import static com.qb.app.model.TableConfig.formatDecimalColumn;
import com.qb.app.model.TableModels.ReportGRNDetailModel;
import com.qb.app.model.entity.Grn;
import com.qb.app.model.entity.GrnItem;
import com.qb.app.model.entity.Product;
import com.qb.app.model.entity.Supplier;
import com.qb.app.model.getLogger;
import com.qb.app.session.ApplicationSession;
import com.qb.app.session.CompanyInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ReportGRNController implements Initializable {

    @FXML
    private Group iconPage;
    @FXML
    private ComboBox<Supplier> cbSupplier;
    @FXML
    private TextField TFGrnId;
    @FXML
    private ComboBox<String> cbFilterBy;
    @FXML
    private Button btnLoadReport;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnVieweReport;
    @FXML
    private AnchorPane root;

    @FXML
    private TextField tfTotalAmount;
    @FXML
    private TextField tfDiscount;

    private Grn savedGrn;
    List<ReportGrn_TableRowController> grnItemList = new ArrayList<>();
    String grnDateTimeString;
    double discount;
    @FXML
    private TableView<ReportGRNDetailModel> table;
    @FXML
    private TableColumn<ReportGRNDetailModel, Integer> olId;
    @FXML
    private TableColumn<ReportGRNDetailModel, String> colItemName;
    @FXML
    private TableColumn<ReportGRNDetailModel, Double> colCostPrice;
    @FXML
    private TableColumn<ReportGRNDetailModel, Double> colQty;
    @FXML
    private TableColumn<ReportGRNDetailModel, Double> colAmount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTableColumns();
//        DefaultAPI.bindTableScroll(tableScroller, tableScrollContainer, tableBody);
        iconPage.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        LoadComboBox();
        LoadFilterComboBox();
        loadTextField();
        setEventListner();
    }

    private void configureTableColumns() {
        olId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colItemName.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getProduct()));
        colCostPrice.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getCostPrice()).asObject());
        colQty.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getQty()).asObject());
        colAmount.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getAmount()).asObject());

        // Optional: Format double columns to 2 decimal places
        formatDecimalColumn(colCostPrice);
        formatDecimalColumn(colQty);
        formatDecimalColumn(colAmount);
    }

    private void loadTextField() {
        tfTotalAmount.setText(String.format("Rs. %,.2f", 0.00));
        tfDiscount.setText(String.format("Rs. %,.2f", 0.00));
    }

    private void LoadComboBox() {
        ComboBoxUtils.loadComboBoxValues(cbSupplier, Supplier.class, "name", Supplier::getName);

    }

    private void LoadFilterComboBox() {
        cbFilterBy.getItems().addAll("ID", "Product Name", "Quantity", "Cost Price");
        cbFilterBy.setValue("Select Filter");
    }

    @FXML
    private void loadReportAcion(ActionEvent event) {
        if (event.getSource() == btnLoadReport) {
            loadGRN();
        }
    }

    private void loadGRN() {
        if (isEntriesValid()) {
            if (isGrnIdAvailable()) {
                loadDataToTable();
            } else {
                CustomAlert.showStyledAlert(root, "GRN ID is not valid , Please use valid GRN ID", Alert.AlertType.WARNING);
            }
        }
    }

    private void loadDataToTable() {
        grnItemList.clear();
        table.getItems().clear();
        tfTotalAmount.setText("Loading...");
        tfDiscount.setText("Loading...");

        ProgressIndicator progress = new ProgressIndicator();
        progress.setMaxSize(50, 50);
        table.setPlaceholder(progress);

        Task<List<ReportGRNDetailModel>> task = new Task<>() {
            @Override
            protected List<ReportGRNDetailModel> call() {
                return JPATransaction.runInTransaction(em -> {
                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    CriteriaQuery<GrnItem> cq = cb.createQuery(GrnItem.class);
                    Root<GrnItem> root = cq.from(GrnItem.class);

                    Join<GrnItem, Grn> grnJoin = root.join("grnId");
                    Join<GrnItem, Product> productJoin = root.join("productId");

                    Predicate supplierCondition = cb.equal(grnJoin.get("supplierId"), cbSupplier.getValue());
                    Predicate grnIdCondition = cb.equal(grnJoin.get("grnCode"), TFGrnId.getText());
                    cq.select(root).where(cb.and(supplierCondition, grnIdCondition));

                    String selectedSort = cbFilterBy.getValue();
                    if ("Product Name".equals(selectedSort)) {
                        cq.orderBy(cb.asc(productJoin.get("product")));
                    } else if ("Quantity".equals(selectedSort)) {
                        cq.orderBy(cb.asc(root.get("qty")));
                    } else if ("Cost Price".equals(selectedSort)) {
                        cq.orderBy(cb.asc(root.get("costPrice")));
                    } else {
                        cq.orderBy(cb.desc(root.get("id")));
                    }

                    List<GrnItem> grnItems = em.createQuery(cq).getResultList();
                    List<ReportGRNDetailModel> models = new ArrayList<>();
                    int index = 1;
                    for (GrnItem item : grnItems) {
                        Product product = item.getProductId();
                        if (product == null) {
                            continue;
                        }

                        String genericName = (product.getGenericName() == null || product.getGenericName().trim().isEmpty())
                                ? "N/A"
                                : product.getGenericName();

                        double amount = item.getQty() * item.getCostPrice();
                        ReportGRNDetailModel model = new ReportGRNDetailModel(
                                index++,
                                product.getProduct(),
                                item.getCostPrice(),
                                item.getQty(),
                                amount,
                                genericName
                        );
                        models.add(model);
                    }

                    return models;
                });
            }
        };

        task.setOnSucceeded(e -> {
            List<ReportGRNDetailModel> models = task.getValue();
            table.getItems().addAll(models);

            double totalAmount = models.stream()
                    .mapToDouble(ReportGRNDetailModel::getAmount)
                    .sum();
            tfTotalAmount.setText(String.format("Rs. %,.2f", totalAmount));

            // Fetch discount and date from one of the items
            if (!models.isEmpty()) {
                Grn grn = JPATransaction.runInTransaction(em -> {
                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    CriteriaQuery<Grn> cq = cb.createQuery(Grn.class);
                    Root<Grn> root = cq.from(Grn.class);
                    cq.select(root).where(
                            cb.equal(root.get("grnCode"), TFGrnId.getText()),
                            cb.equal(root.get("supplierId"), cbSupplier.getValue())
                    );
                    return em.createQuery(cq).getSingleResult();
                });

                if (grn != null) {
                    if (grn.getDateTime() != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
                        grnDateTimeString = grn.getDateTime().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .format(formatter);
                    }
                    discount = grn.getDiscount();
                    tfDiscount.setText(String.format("Rs. %,.2f", discount));
                }
            } else {
                tfTotalAmount.setText("No data found");
                tfDiscount.setText("N/A");
                table.setPlaceholder(new Label("No GRN records found."));
            }
        });

        task.setOnFailed(e -> {
            tfTotalAmount.setText("Failed to load");
            tfDiscount.setText("Failed to load");
            Throwable ex = task.getException();
            ex.printStackTrace();
            getLogger.logger().warning("GRN Load Failed: " + ex);
        });

        new Thread(task).start();
    }

    private boolean isEntriesValid() {
        if (cbSupplier.getValue() == null) {
            CustomAlert.showStyledAlert(root, "Please select the supplier", Alert.AlertType.WARNING);
            cbSupplier.requestFocus();
            return false;
        }
        if (TFGrnId.getText().isEmpty()) {
            CustomAlert.showStyledAlert(root, "Please select the GRN ID", Alert.AlertType.WARNING);
            TFGrnId.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isGrnIdAvailable() {
        return JPATransaction.runInTransaction((em) -> {
            String grnID = TFGrnId.getText();
            Supplier selectedSupplier = cbSupplier.getValue();

            if (grnID == null || grnID.isEmpty() || selectedSupplier == null) {
                return true;
            }

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Grn> grnTable = cq.from(Grn.class);

            Predicate grnCodePredicate = cb.equal(grnTable.get("grnCode"), grnID);
            Predicate supplierPredicate = cb.equal(grnTable.get("supplierId"), selectedSupplier);

            cq.select(cb.count(grnTable))
                    .where(cb.and(grnCodePredicate, supplierPredicate));

            return em.createQuery(cq).getSingleResult() != 0;
        });
    }

    @FXML
    private void refresh(ActionEvent event) {
        if (event.getSource() == btnRefresh) {
            refreshInterface();
        }
    }

    private void refreshInterface() {
        cbSupplier.setValue(null);
        cbSupplier.setPromptText("Select Supplier");
        cbFilterBy.setValue(null);
        cbFilterBy.setPromptText("Select Filter");
        TFGrnId.setText("");
        loadTextField();
        table.getItems().clear();

    }

//    private void printGrnReport() {
//
//        if (!grnItemList.isEmpty()) {
//            Map<String, Object> params = getJRParams();
//            Vector<GrnItemBean> collection = getBeanCollection();
//            try {
//                JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
//                JRPropertiesUtil.getInstance(jasperReportsContext).setProperty(
//                        "net.sf.jasperreports.awt.ignore.missing.font", "true"
//                );
//                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
//                        getClass().getResourceAsStream("/com/qb/app/reports/PharmacyGRN.jasper"));
//                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(collection);
//                JasperPrint report = JasperFillManager.fillReport(jasperReport, params, dataSource);
////                    JasperViewer.viewReport(report, false);
//                JasperViewer viewer = new JasperViewer(report, false);
//                viewer.setAlwaysOnTop(true);
//                viewer.setVisible(true);
//            } catch (JRException e) {
//                e.printStackTrace();
//                getLogger.logger().warning(e.toString());
//            }
//        } else {
//            CustomAlert.showStyledAlert(root, "Report generation failed. Please Load Report First !", Alert.AlertType.WARNING);
//        }
//
//    }
    private void printGrnReport() {
        List<ReportGRNDetailModel> tableData = table.getItems();

        if (tableData.isEmpty()) {
            CustomAlert.showStyledAlert(root, "No data to generate report. Please load data first.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Prepare report parameters
            Map<String, Object> params = new HashMap<>();
            params.put("GrnTime", grnDateTimeString);
            params.put("GrnID", TFGrnId.getText());
            params.put("Supplier", cbSupplier.getValue().getName());
            params.put("CompanyName", CompanyInfo.companyName);
            params.put("Contact", CompanyInfo.mobile);
            params.put("Address", CompanyInfo.address);
            params.put("Employee", ApplicationSession.getEmployee().getName());

            // Calculate totals
            double subtotal = tableData.stream()
                    .mapToDouble(ReportGRNDetailModel::getAmount)
                    .sum();
            double finalTotal = subtotal - discount;

            params.put("SubTotal", String.format("Rs. %,.2f", subtotal));
            params.put("Discount", String.format("Rs. %,.2f", discount));
            params.put("Total", String.format("Rs. %,.2f", finalTotal));
            params.put("TotalQty", String.valueOf(tableData.size()));

            // Create data source for the report
            Vector<GrnItemBean> collection = new Vector<>();
            for (ReportGRNDetailModel item : tableData) {
                // Create GrnItemBean for each table row
                // Note: Generic name is not available in the table model, using "N/A"
                collection.add(new GrnItemBean(
                        String.valueOf(item.getId()),
                        item.getProduct(),
                        item.getGenericName(),
                        String.format("%,.2f", item.getCostPrice()),
                        String.format("%,.2f", item.getQty()),
                        String.format("%,.2f", item.getAmount())
                ));
            }

            // Load and display report
            JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
            JRPropertiesUtil.getInstance(jasperReportsContext).setProperty(
                    "net.sf.jasperreports.awt.ignore.missing.font", "true"
            );

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
                    getClass().getResourceAsStream("/com/qb/app/reports/PharmacyGRN.jasper"));

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(collection);
            JasperPrint report = JasperFillManager.fillReport(jasperReport, params, dataSource);

            JasperViewer viewer = new JasperViewer(report, false);
            viewer.setAlwaysOnTop(true);
            viewer.setVisible(true);

        } catch (JRException e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
            CustomAlert.showStyledAlert(root, "Error generating report: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
            CustomAlert.showStyledAlert(root, "Unexpected error generating report", Alert.AlertType.ERROR);
        }
    }

    private Map<String, Object> getJRParams() {

        Map<String, Object> params = new HashMap<>();
        params.put("GrnTime", grnDateTimeString);
        params.put("GrnID", TFGrnId.getText());
        params.put("Supplier", cbSupplier.getValue().getName());
        params.put("CompanyName", CompanyInfo.companyName);
        params.put("Contact", CompanyInfo.mobile);
        params.put("Address", CompanyInfo.address);
        params.put("Address", CompanyInfo.address);
        getGrnTotal(params);
        params.put("Employee", ApplicationSession.getEmployee().getName());

        return params;
    }

    private Vector<GrnItemBean> getBeanCollection() {

        Vector<GrnItemBean> collection = new Vector<>();
        for (ReportGrn_TableRowController item : grnItemList) {
            GrnItemBean bean = new GrnItemBean(
                    String.valueOf(item.getProductId()),
                    item.getProductName(),
                    item.getProduct().getGenericName() != null ? item.getProduct().getGenericName() : "N/A",
                    String.format("%,.2f", item.getProductCost()),
                    String.valueOf(item.getProductQty()),
                    String.format("%,.2f", item.getProductAmount())
            );
            collection.add(bean);
        }
        return collection;
    }

    private void getGrnTotal(Map<String, Object> params) {

        double total = 0;
        for (ReportGrn_TableRowController item : grnItemList) {
            total += item.getProductAmount();
        }
        double finalTotal = total - discount;
        params.put("SubTotal", String.format("Rs. %,.2f", total));
        params.put("Discount", String.format("Rs. %,.2f", discount));
        params.put("Total", String.format("Rs. %,.2f", finalTotal));
        params.put("TotalQty", String.valueOf(grnItemList.size()));
    }

    @FXML
    private void viewReport(ActionEvent event) {
        if (event.getSource() == btnVieweReport) {
            printGrnReport();
        }
    }

    private void setEventListner() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (null != event.getCode()) {
                if (event.getCode() == KeyCode.F5) {
                    refreshInterface();
                }
            }
        });

    }

    @FXML
    private void GRNIDTextChange(KeyEvent event) {
        table.getItems().clear();
        grnItemList.clear();
    }

}
