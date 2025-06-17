package com.qb.app.controllers.report;

import com.qb.app.model.CustomAlert;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
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

        // Filter by selected date (assumes time is stored)
        Predicate datePredicate = cb.between(
            invoiceJoin.get("dateTime"),
            selectedDate.atStartOfDay(),
            selectedDate.plusDays(1).atStartOfDay()
        );

        cq.select(itemRoot).where(datePredicate).orderBy(cb.asc(invoiceJoin.get("id")));

        String selectedSort = cbFilter.getValue(); // ComboBox<String> for sorting

        if ("ID".equals(selectedSort)) {
            cq.orderBy(cb.asc(productJoin.get("id")));
        } else if ("Invoice Number".equals(selectedSort)) {
            cq.orderBy(cb.asc(invoiceJoin.get("id"))); // or "invoiceNumber" if exists
        } else if ("Product Name".equals(selectedSort)) {
            cq.orderBy(cb.asc(productJoin.get("product"))); // use the actual field name
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

            // Or load FXML row like:
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
    }

    @FXML
    private void Refresh(ActionEvent event) {
           if (event.getSource() == RefreshBtn) {
              refreshInterface();
         }
    }
}
