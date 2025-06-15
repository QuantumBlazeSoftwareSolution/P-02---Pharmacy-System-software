/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers.report;

import com.qb.app.controllers.InventoryGRN_TableRowController;
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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DefaultAPI.bindTableScroll(tableScroller, tableScrollContainer, tableBody);
        iconPage.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        LoadComboBox();
        LoadFilterComboBox();

    }    
    
    private void LoadComboBox(){
            ComboBoxUtils.loadComboBoxValues(cbSupplier, Supplier.class, "name", Supplier::getName);
            
    }
    private void LoadFilterComboBox() {
        cbFilterBy.getItems().addAll("ID","Product Name", "Quantity", "Cost Price");
        cbFilterBy.setValue("Select Filter");
    }

    @FXML
    private void loadReportAcion(ActionEvent event) {
         if (event.getSource() == btnLoadReport) {
          loadGRN();
        }
    }
    
    private void loadGRN(){
        if(isEntriesValid()){
            loadDataToTable();
        }
        
    }
    
    private  void loadDataToTable(){
        
JPATransaction.runInTransaction((em) -> {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<GrnItem> cq = cb.createQuery(GrnItem.class);

    Root<GrnItem> grnItemRoot = cq.from(GrnItem.class);

    // Join GRN table from GRNItem
    Join<GrnItem, Grn> grnJoin = grnItemRoot.join("grnId"); // make sure "grn" is the field name in GRNItem.java

    // Join Product table from GRNItem
    Join<GrnItem, Product> productJoin = grnItemRoot.join("productId"); // same here

    // Add condition: grn.supplier.id == targetSupplierId
    Predicate supplierCondition = cb.equal(grnJoin.get("supplierId"), cbSupplier.getValue());

    // (Optional) If you want to filter by specific GRN ID too
    Predicate grnIdCondition = cb.equal(grnJoin.get("grnCode"), TFGrnId.getText()); // replace 5L with target GRN id

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
    cq.orderBy(cb.asc(grnItemRoot.get("id"))); // sort by primary key (id)
}

        List<GrnItem> resultList = em.createQuery(cq).getResultList();
        tableBody.getChildren().clear(); // Clear previous data
    
    double totalAmount = 0.0; 
   

for (GrnItem item : resultList) {
    Product product = item.getProductId(); // Correct field name is getProductId()
    String productName = (product != null) ? product.getProduct() : "No Product"; // Correct getter: getProduct()

    System.out.println("Product: " + productName);
    System.out.println("Qty: " + item.getQty());
    System.out.println("Cost: " + item.getCostPrice());
    
    double amountd = item.getQty() * item.getCostPrice();
    totalAmount += amountd;

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/qb/app/fxmlComponent/reportGrn_TableRow.fxml"));
        Node grndata = loader.load();
        ReportGrn_TableRowController controller = loader.getController();
                controller.setData(product, product.getId(), productName, item.getCostPrice(), item.getQty());

        tableBody.getChildren().add(grndata);

    } catch (IOException e) {
        e.printStackTrace();
        
    } 
}

 tfTotalAmount.setText(String.valueOf(totalAmount));

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

    @FXML
    private void refresh(ActionEvent event) {
        if (event.getSource() == btnRefresh) {
            refreshInterface();
        }
    }

    private void refreshInterface() {
        cbSupplier.setValue(null);
        cbSupplier.setPromptText("Select Supplier");
        cbFilterBy.setPromptText("Ex: Qty");
        cbFilterBy.setValue(null);
        TFGrnId.setText("");
          tableBody.getChildren().clear();

    }
      

}
