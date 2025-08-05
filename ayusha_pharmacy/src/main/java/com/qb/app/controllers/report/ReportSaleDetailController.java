package com.qb.app.controllers.report;

import com.qb.app.controllers.report.beans.InvoiceBean;
import com.qb.app.controllers.report.beans.InvoiceItemsBean;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import static com.qb.app.model.TableConfig.formatDecimalColumn;
import com.qb.app.model.TableModels.ReportSaleDetailModel;
import com.qb.app.model.entity.Invoice;
import com.qb.app.model.entity.InvoiceItem;
import com.qb.app.model.entity.Product;
import com.qb.app.model.getLogger;
import com.qb.app.session.CompanyInfo;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ReportSaleDetailController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Group iconPage;
    @FXML
    private DatePicker DateSelector;
    @FXML
    private ComboBox<String> cbFilter;
    @FXML
    private Button LoadReportBtn, RefreshBtn, ViewRepoetBtn;
    @FXML
    private TextField TFTotalSale, TFTotalProfit, TFTotalCost, TFTotalDiscount;
    @FXML
    private TableView<ReportSaleDetailModel> tableInvoiceItems;
    @FXML
    private TableColumn<ReportSaleDetailModel, Integer> colId;
    @FXML
    private TableColumn<ReportSaleDetailModel, String> colInvoiceNo, colProduct;
    @FXML
    private TableColumn<ReportSaleDetailModel, Double> colQty, colUnitPrice, colAmount, colDiscount, colProfit;

    private final List<ReportSaleDetailModel> invoiceItemList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iconPage.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        DateSelector.setValue(LocalDate.now());
        setEventListner();
        loadComboBox();
        loadTextField();
        configureTableColumns();
    }

    private void configureTableColumns() {
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colInvoiceNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInvoiceNo()));
        colProduct.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProduct()));
        colQty.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getQty()).asObject());
        colUnitPrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getUnitPrice()).asObject());
        colAmount.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getSale()).asObject());
        colDiscount.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getDiscount()).asObject());
        colProfit.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getProfit()).asObject());

        formatDecimalColumn(colQty);
        formatDecimalColumn(colUnitPrice);
        formatDecimalColumn(colAmount);
        formatDecimalColumn(colDiscount);
        formatDecimalColumn(colProfit);
    }

    private void loadTextField() {
        TFTotalCost.setText("Rs. 0.00");
        TFTotalSale.setText("Rs. 0.00");
        TFTotalDiscount.setText("Rs. 0.00");
        TFTotalProfit.setText("Rs. 0.00");
    }

    private void loadInvoiceReport() {
        LocalDate selectedDate = DateSelector.getValue();
        if (selectedDate == null) {
            CustomAlert.showStyledAlert(root, "Please set a date", Alert.AlertType.WARNING);
            return;
        }

        ProgressIndicator progress = new ProgressIndicator();
        progress.setMaxSize(50, 50);
        tableInvoiceItems.setPlaceholder(progress);

        Task<List<ReportSaleDetailModel>> loadTask = new Task<>() {
            @Override
            protected List<ReportSaleDetailModel> call() {
                return JPATransaction.runInTransaction(em -> {
                    List<ReportSaleDetailModel> models = new ArrayList<>();

                    CriteriaBuilder cb = em.getCriteriaBuilder();
                    CriteriaQuery<InvoiceItem> cq = cb.createQuery(InvoiceItem.class);
                    Root<InvoiceItem> itemRoot = cq.from(InvoiceItem.class);
                    Join<InvoiceItem, Invoice> invoiceJoin = itemRoot.join("invoiceId");
                    Join<InvoiceItem, Product> productJoin = itemRoot.join("productId");

                    Predicate datePredicate = cb.between(
                            invoiceJoin.get("dateTime"),
                            selectedDate.atStartOfDay(),
                            selectedDate.plusDays(1).atStartOfDay()
                    );

                    cq.select(itemRoot).where(datePredicate);

                    String selectedSort = cbFilter.getValue();
                    if ("ID".equals(selectedSort)) {
                        cq.orderBy(cb.asc(productJoin.get("id")));
                    } else if ("Invoice Number".equals(selectedSort)) {
                        cq.orderBy(cb.asc(invoiceJoin.get("id")));
                    } else if ("Product Name".equals(selectedSort)) {
                        cq.orderBy(cb.asc(productJoin.get("product")));
                    } else if ("Quantity".equals(selectedSort)) {
                        cq.orderBy(cb.asc(itemRoot.get("qty")));
                    }

                    List<InvoiceItem> invoiceItems = em.createQuery(cq).getResultList();
                    for (InvoiceItem item : invoiceItems) {
                        Invoice invoice = item.getInvoiceId();
                        Product product = item.getProductId();
                        ReportSaleDetailModel model = new ReportSaleDetailModel(invoice, product, item);
                        models.add(model);
                    }

                    return models;
                });
            }
        };

        loadTask.setOnSucceeded(e -> {
            List<ReportSaleDetailModel> models = loadTask.getValue();
            System.out.println("List size: " + models.size());
            invoiceItemList.clear();
            invoiceItemList.addAll(models);
            tableInvoiceItems.getItems().setAll(models);

            if (models.isEmpty()) {
                tableInvoiceItems.setPlaceholder(new Label("No sales records found."));
            }

            double totalCost = models.stream().mapToDouble(ReportSaleDetailModel::getCost).sum();
            double totalSale = models.stream().mapToDouble(ReportSaleDetailModel::getSale).sum();
            double totalDiscount = models.stream().mapToDouble(ReportSaleDetailModel::getDiscount).sum();
            totalDiscount += models.stream().mapToDouble(ReportSaleDetailModel::getInvoiceDiscount).sum();
            double totalProfit = models.stream().mapToDouble(ReportSaleDetailModel::getProfit).sum();

            TFTotalCost.setText(String.format("Rs. %,.2f", totalCost));
            TFTotalSale.setText(String.format("Rs. %,.2f", totalSale - totalDiscount));
            TFTotalDiscount.setText(String.format("Rs. %,.2f", totalDiscount));
            TFTotalProfit.setText(String.format("Rs. %,.2f", totalProfit));
        });

        new Thread(loadTask).start();
    }

    private void LoadReportDetails() {
        if (invoiceItemList.isEmpty()) {
            CustomAlert.showStyledAlert(root, "Report generation failed. Please Load Report First!", Alert.AlertType.WARNING);
            return;
        }

        LocalDate selectedDate = DateSelector.getValue();
        if (selectedDate == null) {
            CustomAlert.showStyledAlert(root, "Please set a date", Alert.AlertType.WARNING);
            return;
        }

        JPATransaction.runInTransaction(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Invoice> InvoiceQuery = cb.createQuery(Invoice.class);
            Root<Invoice> invoiceRoot = InvoiceQuery.from(Invoice.class);
            Predicate datePredicate = cb.between(
                    invoiceRoot.get("dateTime"),
                    selectedDate.atStartOfDay(),
                    selectedDate.plusDays(1).atStartOfDay()
            );
            InvoiceQuery.select(invoiceRoot).where(datePredicate);
            List<Invoice> invoiceList = em.createQuery(InvoiceQuery).getResultList();

            if (invoiceList.isEmpty()) {
                CustomAlert.showStyledAlert(root, "No sales records found for the selected date.", Alert.AlertType.WARNING);
                return;
            }

            List<InvoiceBean> invoiceListBean = new ArrayList<>();
            double totalCost = 0, totalSale = 0, totalDiscount = 0, totalProfit = 0;

            for (Invoice invoice : invoiceList) {
                CriteriaQuery<InvoiceItem> itemQuery = cb.createQuery(InvoiceItem.class);
                Root<InvoiceItem> itemRoot = itemQuery.from(InvoiceItem.class);
                itemQuery.select(itemRoot).where(cb.equal(itemRoot.get("invoiceId"), invoice));
                List<InvoiceItem> items = em.createQuery(itemQuery).getResultList();

                List<InvoiceItemsBean> beans = new ArrayList<>();
                double tQty = 0, tAmount = 0, tDisc = 0, tCost = 0, tProfit = 0;

                tDisc += invoice.getBillDiscount();

                for (InvoiceItem item : items) {
                    double qty = item.getQty();
                    double cost = item.getCostPrice();
                    double sale = item.getSalePrice();
                    double discount = qty * item.getDiscount();
                    double netSale = (qty * item.getSalePrice()) - discount;
                    double profit = netSale - qty * item.getCostPrice();

                    tQty += qty;
                    tCost += qty * item.getCostPrice();
                    tAmount += netSale;
                    tDisc += discount;
                    tProfit += profit;

                    beans.add(new InvoiceItemsBean(
                            String.valueOf(item.getProductId().getId()),
                            item.getProductId().getProduct(),
                            String.format("%,.2f", cost),
                            String.format("%,.2f", sale),
                            String.valueOf((int) qty),
                            String.format("%,.2f", discount),
                            String.format("%,.2f", netSale),
                            String.format("%,.2f", profit)
                    ));
                }

                invoiceListBean.add(new InvoiceBean(
                        String.format("INV-%06d", invoice.getId()),
                        beans,
                        invoice.getDateTime().toInstant().atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")),
                        String.format("%,.2f", invoice.getBillAmount()),
                        String.format("%,.2f", invoice.getPaidAmount()),
                        String.format("%,.2f", invoice.getPaidAmount() - (invoice.getBillAmount() - invoice.getBillDiscount())),
                        String.format("%,.2f", tQty),
                        String.format("%,.2f", tDisc),
                        String.format("%,.2f", tAmount),
                        String.format("%,.2f", tProfit),
                        String.format("%,.2f", invoice.getBillDiscount())
                ));

                totalCost += tCost;
                totalSale += tAmount;
                totalDiscount += tDisc;
                totalProfit += tProfit;
            }

            Map<String, Object> params = new HashMap<>();
            params.put("ApplicationName", CompanyInfo.companyName);
            params.put("Address", CompanyInfo.address);
            params.put("Contact", CompanyInfo.mobile);
            params.put("TotalStockValue", String.format("Rs. %,.2f", totalCost));
            params.put("TotalSaleValue", String.format("Rs. %,.2f", totalSale - totalDiscount));
            params.put("TotalDiscount", String.format("Rs. %,.2f", totalDiscount));
            params.put("TotalProfit", String.format("Rs. %,.2f", totalProfit));

            try {
                params.put("Logo", getClass().getResource("/com/qb/app/assets/images/logo.png"));
                JasperReport subReport = (JasperReport) JRLoader.loadObject(
                        getClass().getResourceAsStream("/com/qb/app/reports/Pharmacy_Detail_Sale_Sub_Report.jasper"));
                params.put("SUB_REPORT_PATH", subReport);

                JasperReport mainReport = (JasperReport) JRLoader.loadObject(
                        getClass().getResourceAsStream("/com/qb/app/reports/Pharmacy_Detail_Sale.jasper"));

                JasperPrint print = JasperFillManager.fillReport(mainReport, params, new JRBeanCollectionDataSource(invoiceListBean));
                JasperViewer viewer = new JasperViewer(print, false);
                viewer.setAlwaysOnTop(true);
                viewer.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                getLogger.logger().warning(e.toString());
            }
        });
    }

    private void loadComboBox() {
        cbFilter.getItems().addAll("ID", "Invoice Number", "Product Name", "Quantity");
        cbFilter.setValue("ID");
    }

    private void setEventListner() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F5) {
                refreshInterface();
            }
        });
    }

    private void refreshInterface() {
        cbFilter.setValue("ID");
        DateSelector.setValue(LocalDate.now());
        loadTextField();
        tableInvoiceItems.getItems().clear();
    }

    @FXML
    private void LoadReport(ActionEvent event) {
        if (event.getSource() == LoadReportBtn) {
            loadInvoiceReport();
        }
    }

    @FXML
    private void ViewReport(ActionEvent event) {
        if (event.getSource() == ViewRepoetBtn) {
            LoadReportDetails();
        }
    }

    @FXML
    private void Refresh(ActionEvent event) {
        if (event.getSource() == RefreshBtn) {
            refreshInterface();
        }
    }
}
