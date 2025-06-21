/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers.report;

import com.qb.app.controllers.InventoryGRN_TableRowController;
import com.qb.app.controllers.report.beans.GrnItemBean;
import com.qb.app.model.ComboBoxUtils;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Employee;
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
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author Vihanga
 */
public class ReportGRNController implements Initializable {

    @FXML
    private Group iconPage;
    @FXML
    private ScrollPane tableScrollContainer;
    @FXML
    private VBox tableBody;
    @FXML
    private ScrollBar tableScroller;
  
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


    /**
     * Initializes the controller class.
     */
    
    private Grn savedGrn;
    List<ReportGrn_TableRowController> grnItemList = new ArrayList<>();
    String grnDateTimeString;
    double discount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DefaultAPI.bindTableScroll(tableScroller, tableScrollContainer, tableBody);
        iconPage.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        LoadComboBox();
        LoadFilterComboBox();
        setEventListner();
        loadTextField();

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

        JPATransaction.runInTransaction((em) -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<GrnItem> cq = cb.createQuery(GrnItem.class);
            Root<GrnItem> grnItemRoot = cq.from(GrnItem.class);

            Join<GrnItem, Grn> grnJoin = grnItemRoot.join("grnId");
            Join<GrnItem, Product> productJoin = grnItemRoot.join("productId"); // same here

            Predicate supplierCondition = cb.equal(grnJoin.get("supplierId"), cbSupplier.getValue());
            Predicate grnIdCondition = cb.equal(grnJoin.get("grnCode"), TFGrnId.getText());

            cq.select(grnItemRoot).where(cb.and(supplierCondition, grnIdCondition));

            // 👉 Apply sorting based on ComboBox selection
            String selectedSort = cbFilterBy.getValue();
            if ("Product Name".equals(selectedSort)) {
                cq.orderBy(cb.asc(productJoin.get("product"))); // assuming getProduct() is the name
            } else if ("Quantity".equals(selectedSort)) {
                cq.orderBy(cb.asc(grnItemRoot.get("qty")));
            } else if ("Cost Price".equals(selectedSort)) {
                cq.orderBy(cb.asc(grnItemRoot.get("costPrice")));
            } else if ("ID".equals(selectedSort)) {
                cq.orderBy(cb.desc(grnItemRoot.get("id"))); // sort by primary key (id)
            }

            List<GrnItem> resultList = em.createQuery(cq).getResultList();
            tableBody.getChildren().clear(); // Clear previous data
            double totalAmount = 0.0;
            for (GrnItem item : resultList) {
                Product product = item.getProductId(); // Correct field name is getProductId()
                String productName = (product != null) ? product.getProduct() : "No Product"; // Correct getter: getProduct()

           
                Grn grn = item.getGrnId();
                grnDateTimeString = "No Date";
                if (grn != null && grn.getDateTime() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
                    grnDateTimeString = grn.getDateTime().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .format(formatter);

                    discount = grn.getDiscount();
                    tfDiscount.setText(String.format("Rs. %,.2f", discount));
                }                
                double amountd = item.getQty() * item.getCostPrice();
                totalAmount += amountd;

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/qb/app/fxmlComponent/reportGrn_TableRow.fxml"));
                    Node grndata = loader.load();
                    ReportGrn_TableRowController controller = loader.getController();
                    controller.setData(product, product.getId(), productName, item.getCostPrice(), item.getQty());
                    grnItemList.add(controller);
                    tableBody.getChildren().add(grndata);

                } catch (IOException e) {
                    e.printStackTrace();
                  getLogger.logger().warning(e.toString());
                }
            }
            tfTotalAmount.setText(String.format("Rs. %,.2f", totalAmount));
        });

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
        cbSupplier.setPromptText("Ex: Munchee - Heshan");
        cbFilterBy.setValue(null);
        cbFilterBy.setPromptText("Select Filter");
        TFGrnId.setText("");
       loadTextField();
        tableBody.getChildren().clear();

    }

    private void printGrnReport() {

        if (isEntriesValid()) {
            if (!grnItemList.isEmpty()) {
                Map<String, Object> params = getJRParams();
                Vector<GrnItemBean> collection = getBeanCollection();
                try {
                    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
                            getClass().getResourceAsStream("/com/qb/app/reports/PharmacyGRN.jasper"));
                    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(collection);
                    JasperPrint report = JasperFillManager.fillReport(jasperReport, params, dataSource);
                    JasperViewer.viewReport(report, false);
                } catch (JRException e) {
                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            } else {
                CustomAlert.showStyledAlert(root, "Report generation failed. Please Load Report First !", Alert.AlertType.WARNING);
            }
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
          tableBody.getChildren().clear();
            grnItemList.clear();
    }

 

}
