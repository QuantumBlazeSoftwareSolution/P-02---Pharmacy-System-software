package com.qb.app.controllers.report;

import com.qb.app.controllers.report.beans.InvoiceBean;
import com.qb.app.controllers.report.beans.InvoiceItemsBean;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Invoice;
import com.qb.app.model.entity.InvoiceItem;
import com.qb.app.model.entity.Product;
import com.qb.app.model.entity.ProductHasProductType;
import com.qb.app.model.entity.ProductType;
import com.qb.app.model.getLogger;
import com.qb.app.session.CompanyInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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
    private Button LoadReportBtn;
    @FXML
    private ScrollPane tableScrollContainer;
    @FXML
    private VBox tableBody;
    @FXML
    private ScrollBar tableScroller;
    @FXML
    private TextField TFTotalSale;
    @FXML
    private TextField TFTotalProfit;
    @FXML
    private Button RefreshBtn;
    @FXML
    private Button ViewRepoetBtn;
    @FXML
    private TextField TFTotalCost;
    @FXML
    private TextField TFTotalDiscount;

  List<ReportSaleDetail_TableRowController> invoiceItemList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
          iconPage.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        DefaultAPI.bindTableScroll(tableScroller, tableScrollContainer, tableBody);
        DateSelector.setValue(LocalDate.now());
        setEventListner();
        loadComboBox();
        loadTextField();
    }
   
    private void  loadTextField(){
        TFTotalCost.setText(String.format("Rs. %,.2f", 0.00));
        TFTotalDiscount.setText(String.format("Rs. %,.2f", 0.00));
        TFTotalProfit.setText(String.format("Rs. %,.2f", 0.00));
        TFTotalSale.setText(String.format("Rs. %,.2f", 0.00));
    
    }
    
private void loadInvoiceReport() {
    tableBody.getChildren().clear();

    LocalDate selectedDate = DateSelector.getValue();
    if (selectedDate == null) {
         CustomAlert.showStyledAlert(root, "Please set a date", Alert.AlertType.WARNING);
        return;
    }

    JPATransaction.runInTransaction(em -> {
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
        cq.select(itemRoot).where(datePredicate).orderBy(cb.asc(invoiceJoin.get("id")));
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
        if (invoiceItems.isEmpty()) {
             CustomAlert.showStyledAlert(root, "No sales records found for the selected date.", Alert.AlertType.WARNING);
        }
        double Tdiscount =0;
        double TSale =0;
        double TCost =0;

        for (InvoiceItem item : invoiceItems) {
            Product product = item.getProductId();
            Invoice invoice = item.getInvoiceId();
            
            Tdiscount += item.getDiscount()* item.getQty();
            TSale +=   item.getSalePrice()* item.getQty();
            TCost += item.getCostPrice()* item.getQty();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/qb/app/fxmlComponent/reportSaleDetail_TableRow.fxml"));
                Node row = loader.load();
                ReportSaleDetail_TableRowController controller = loader.getController();
                controller.setData(invoice,product,item);
                invoiceItemList.add(controller);
                tableBody.getChildren().add(row);
            } catch (IOException e) {
                e.printStackTrace();
                 getLogger.logger().warning(e.toString());
            }
        }
        
        TFTotalSale.setText(String.format("Rs. %,.2f", TSale));
        TFTotalCost.setText(String.format("Rs. %,.2f", TCost));
        TFTotalDiscount.setText(String.format("Rs. %,.2f", Tdiscount));
        TFTotalProfit.setText(String.format("Rs. %,.2f", TSale-(TCost+Tdiscount)));

        }
    );
    }

    private void LoadReportDetails() {

        if (!invoiceItemList.isEmpty()) {
            LocalDate selectedDate = DateSelector.getValue();
            if (selectedDate == null) {
                CustomAlert.showStyledAlert(root, "Please set a date", Alert.AlertType.WARNING);
                return;
            }

            JPATransaction.runInTransaction((em) -> {

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
                List<InvoiceBean> invoiceListBean = new ArrayList<>();

                if (invoiceList.isEmpty()) {
                    CustomAlert.showStyledAlert(root, "No sales records found for the selected date.", Alert.AlertType.WARNING);
                    return;
                }

                double GrnadTotalStockValue = 0;
                double GrnadTotalSaleValue = 0;
                double GrnadTotalDiscountValue = 0;
                double GrnadTotalProfitValue = 0;

                for (Invoice invoice : invoiceList) {
                    CriteriaQuery<InvoiceItem> InvoiceItemQuery = cb.createQuery(InvoiceItem.class);
                    Root<InvoiceItem> InvoiceItemRoot = InvoiceItemQuery.from(InvoiceItem.class);
                    InvoiceItemQuery.select(InvoiceItemRoot)
                            .where(
                                    cb.and(
                                            cb.equal(InvoiceItemRoot.get("invoiceId"), invoice)
                                    )
                            );

                    List<InvoiceItem> InvoiceItemList = em.createQuery(InvoiceItemQuery).getResultList();
                    List<InvoiceItemsBean> invoiceItemBeanList = new ArrayList<>();

                    double tQty = 0;
                    double tDiscount = 0;
                    double tAmount = 0;
                    double tProfit = 0;
                    double tCost = 0;
                    for (InvoiceItem invoiceItem : InvoiceItemList) {
                        tQty += invoiceItem.getQty();
                        tDiscount += invoiceItem.getQty()* invoiceItem.getDiscount();
                        double Ramount = (invoiceItem.getQty() * invoiceItem.getSalePrice()) - ( invoiceItem.getQty() * invoiceItem.getDiscount()) ;
                        double Rcost = invoiceItem.getQty() * invoiceItem.getCostPrice();
                        double Rdiscount = invoiceItem.getQty() * invoiceItem.getDiscount();
                        double Rprofit = Ramount - (Rcost );
                        tCost += Rcost;
                        tAmount += Ramount;
                        tProfit += Rprofit;

                        invoiceItemBeanList.add(new InvoiceItemsBean(
                                String.valueOf(invoiceItem.getProductId().getId()),
                                String.valueOf(invoiceItem.getProductId().getProduct()),
                                String.format("%,.2f", invoiceItem.getQty() *invoiceItem.getCostPrice()),
                                String.format("%,.2f",invoiceItem.getQty() * invoiceItem.getSalePrice()),
                                String.valueOf(invoiceItem.getQty()),
                                String.valueOf(invoiceItem.getQty() * invoiceItem.getDiscount()),
                                String.format("%,.2f", Ramount),
                                String.format("%,.2f", Rprofit)
                        ));
                    }

                    GrnadTotalDiscountValue += tDiscount;
                    GrnadTotalProfitValue += tProfit;
                    GrnadTotalSaleValue += tAmount;
                    GrnadTotalStockValue += tCost;

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
                    String DateTimeString = invoice.getDateTime().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .format(formatter);

                    invoiceListBean.add(new InvoiceBean(
                            String.format("INV-%06d", invoice.getId()),
                            invoiceItemBeanList,
                            DateTimeString,
                            String.format("%,.2f", invoice.getBillAmount()),
                            String.format("%,.2f", invoice.getPaidAmount()),
                            String.format("%,.2f", invoice.getPaidAmount() - invoice.getBillAmount()),
                            String.format("%,.2f", tQty),
                            String.format("%,.2f", tDiscount),
                            String.format("%,.2f", tAmount),
                            String.format("%,.2f", tProfit)
                    ));
                }

                Map<String, Object> params = new HashMap<>();
                params.put("ApplicationName", CompanyInfo.companyName);
                params.put("Address", CompanyInfo.address);
                params.put("Contact", CompanyInfo.mobile);
                params.put("TotalStockValue", String.format("Rs. %,.2f", GrnadTotalStockValue));
                params.put("TotalSaleValue", String.format("Rs. %,.2f", GrnadTotalSaleValue));
                params.put("TotalDiscount", String.format("Rs. %,.2f", GrnadTotalDiscountValue));
                params.put("TotalProfit", String.format("Rs. %,.2f", GrnadTotalProfitValue));

                try {
                    URL imageUrl = getClass().getResource("/com/qb/app/assets/images/logo.png");
                    params.put("Logo", imageUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }

                try {
                    JasperReport subReport = (JasperReport) JRLoader.loadObject(
                            getClass().getResourceAsStream("/com/qb/app/reports/Pharmacy_Detail_Sale_Sub_Report.jasper"));
                    params.put("SUB_REPORT_PATH", subReport);

                    JasperReport mainReport = (JasperReport) JRLoader.loadObject(
                            getClass().getResourceAsStream("/com/qb/app/reports/Pharmacy_Detail_Sale.jasper"));

                    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(invoiceListBean);
                    JasperPrint report = JasperFillManager.fillReport(mainReport, params, dataSource);
                    JasperViewer.viewReport(report, false);
                } catch (JRException e) {
                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            });
        } else {
            CustomAlert.showStyledAlert(root, "Report generation failed. Please Load Report First !", Alert.AlertType.WARNING);
        }
    }

    private void loadComboBox() {
        cbFilter.getItems().addAll("ID", "Invoice Number", "Product Name", "Quantity");
        cbFilter.setValue("Select Filter");
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

    private void refreshInterface() {
        cbFilter.setValue(null);
        loadTextField();
        DateSelector.setValue(LocalDate.now());
        tableBody.getChildren().clear();
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
