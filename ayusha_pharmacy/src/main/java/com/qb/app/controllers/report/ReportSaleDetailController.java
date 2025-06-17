package com.qb.app.controllers.report;

import com.qb.app.model.CustomAlert;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Invoice;
import com.qb.app.model.entity.InvoiceItem;
import com.qb.app.model.entity.Product;
import com.qb.app.model.getLogger;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

        });
    }

//    private void LoadReportDetails() {
//        
//           JPATransaction.runInTransaction((em) -> {
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Brand> brandQuery = cb.createQuery(Brand.class);
//            Root<Brand> brandRoot = brandQuery.from(Brand.class);
//            brandQuery.select(brandRoot);
//            List<Brand> brandList = em.createQuery(brandQuery).getResultList();
//
//            List<BrandBean> brandListBean = new ArrayList<>();
//            double grandTotalSaleAmount = 0;
//            double grandTotalStockAmount = 0;
//            int grandTotalQty = 0;
//            for (Brand brand : brandList) {
//                CriteriaQuery<Product> productQuery = cb.createQuery(Product.class);
//                Root<Product> productRoot = productQuery.from(Product.class);
//                productQuery.select(productRoot)
//                        .where(
//                                cb.and(
//                                        cb.equal(productRoot.get("brandId"), brand)
//                                )
//                        );
//
//                List<Product> productList = em.createQuery(productQuery).getResultList();
//                List<ProductBean> productBeanList = new ArrayList<>();
//
//                double brandTotalSaleAmount = 0;
//                double brandTotalStockAmount = 0;
//                int brandTotalQty = 0;
//
//                for (Product product : productList) {
//                    CriteriaQuery<Stock> stockQuery = cb.createQuery(Stock.class);
//                    Root<Stock> stockRoot = stockQuery.from(Stock.class);
//                    stockQuery.select(stockRoot)
//                            .where(cb.equal(stockRoot.get("productId"), product));
//                             Stock stockdetails = em.createQuery(stockQuery).getSingleResult();
//                    double qty = stockdetails.getQty();
//                    double tSaleAmount = qty * product.getSalePrice();
//                    double tCostAmount = qty * product.getCostPrice();
//                    brandTotalQty += qty;
//                    brandTotalSaleAmount += tSaleAmount;
//                    brandTotalStockAmount += tCostAmount;
//
//                    productBeanList.add(new ProductBean(
//                            String.valueOf(product.getId()),
//                            product.getProduct(),
//                            product.getGenericName(),
//                            String.format("Rs. %,.2f", product.getSalePrice()),
//                            String.valueOf(qty),
//                            String.format("Rs. %,.2f", tSaleAmount)
//                    ));
//                }
//                  grandTotalSaleAmount += brandTotalSaleAmount;
//                  grandTotalStockAmount += brandTotalStockAmount;
//
//                brandListBean.add(new BrandBean(
//                        brand.getBrand(),
//                        productBeanList,
//                        String.valueOf(productBeanList.size()),
//                        String.format("Rs. %,.2f", brandTotalSaleAmount),
//                        String.valueOf(brandTotalQty)
//                ));
//            }
//
//            Map<String, Object> params = new HashMap<>();
//            params.put("companyName", CompanyInfo.companyName);
//            params.put("Address", CompanyInfo.address);
//            params.put("Contact", CompanyInfo.mobile);
//            params.put("ExpectedProfit", String.format("Rs. %,.2f", grandTotalSaleAmount-grandTotalStockAmount));
//            params.put("TotalSaleValue",String.format("Rs. %,.2f", grandTotalSaleAmount ));
//            params.put("TotalStockValue",String.format("Rs. %,.2f", grandTotalStockAmount));
//
//            try {
//                URL imageUrl = getClass().getResource("/com/qb/app/assets/images/logo.png");
//                params.put("Logo", imageUrl);
//            } catch (Exception e) {
//                e.printStackTrace();
//                getLogger.logger().warning(e.toString());
//            }
//
//            try {
//                JasperReport subReport = (JasperReport) JRLoader.loadObject(
//                        getClass().getResourceAsStream("/com/qb/app/reports/Pharmacy_Stock_Balance_Sub_Report.jasper"));
//                params.put("SUB_REPORT_PATH", subReport);
//
//                JasperReport mainReport = (JasperReport) JRLoader.loadObject(
//                        getClass().getResourceAsStream("/com/qb/app/reports/Pharmacy_Stock_Balance.jasper"));
//
//                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(brandListBean);
//                JasperPrint report = JasperFillManager.fillReport(mainReport, params, dataSource);
//                JasperViewer.viewReport(report, false);
//            } catch (JRException e) {
//                e.printStackTrace();
//                 getLogger.logger().warning(e.toString());
//            }
//        });
//    }

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
//              LoadReportDetails();
         }
    }

    @FXML
    private void Refresh(ActionEvent event) {
           if (event.getSource() == RefreshBtn) {
              refreshInterface();
         }
    }
}
