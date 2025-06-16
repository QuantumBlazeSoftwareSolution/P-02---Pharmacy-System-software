
package com.qb.app.controllers.report;

import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.entity.Brand;
import com.qb.app.model.entity.Product;
import com.qb.app.model.entity.ProductStatus;
import com.qb.app.model.entity.Stock;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class ReportStockBalanceController implements Initializable {

    @FXML
    private Group iconPage;
    @FXML
    private ComboBox<String> cbFilter;
    @FXML
    private Button LoadReport;
    @FXML
    private ScrollPane tableScrollContainer;
    @FXML
    private VBox tableBody;
    @FXML
    private ScrollBar tableScroller;
    private TextField TFTotalValue;
    @FXML
    private TextField TFTotalProfit;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button ViewReport;
    @FXML
    private AnchorPane root;
    @FXML
    private TextField TFTotalStockValue;
    @FXML
    private TextField TFTotalSaleValue;

    /**
     * Initializes the controller class.
     */
    
     List<ReportStockBalance_TableRowController> stockItemList = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            DefaultAPI.bindTableScroll(tableScroller, tableScrollContainer, tableBody);
        setEventListner();
        loadFilterCombo();
       
    }
    
    private void loadData() {
         stockItemList.clear();

        JPATransaction.runInTransaction((em) -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Stock> cq = cb.createQuery(Stock.class);
            Root<Stock> stockRoot = cq.from(Stock.class);

            // Join to Product
            Join<Stock, Product> productJoin = stockRoot.join("productId");

            // Join to Brand
            Join<Product, Brand> brandJoin = productJoin.join("brandId");

            // Join to ProductStatus
            Join<Product, ProductStatus> PstatusJoin = productJoin.join("productStatusId");
            
//            Join<Brand, ProductStatus> BstatusJoin = productJoin.join("product_status_id");
            // Filters: ProductStatus = "Enable" AND Brand.status = "Enable"
            Predicate productStatusEnabled = cb.equal(PstatusJoin.get("status"), "Enable");
//            Predicate brandStatusEnabled = cb.equal(BstatusJoin.get("status"), "Enable");
//            Predicate brandStatusEnabled = cb.equal(brandJoin.get("status"), "Enable");

            cq.select(stockRoot).where(cb.and(productStatusEnabled));
            String selectedSort = cbFilter.getValue();
            if ("Product Name".equals(selectedSort)) {
                cq.orderBy(cb.asc(productJoin.get("product")));
            } else if ("Quantity".equals(selectedSort)) {
                cq.orderBy(cb.asc(stockRoot.get("qty")));
            } else if ("Brand Name".equals(selectedSort)) {
                cq.orderBy(cb.asc(brandJoin.get("brand")));
            } else if ("ID".equals(selectedSort)) {
                cq.orderBy(cb.asc(stockRoot.get("id"))); // assuming stockRoot represents Stock entity
            }

            List<Stock> stockList = em.createQuery(cq).getResultList();

            // Now loop through and extract needed info
            for (Stock stock : stockList) {
                Product product = stock.getProductId();
                Brand brand = product.getBrandId();

                String productId = product.getId().toString();
                String productName = product.getProduct(); // or getProductName()
                String brandName = brand.getBrand();
                double qty = stock.getQty();
                double costPrice = product.getCostPrice();
                double salePrice = product.getSalePrice();
                
                System.out.println("Product ID: " + productId);
                System.out.println("Brand: " + brandName);
                System.out.println("Product: " + productName);
                System.out.println("Qty: " + qty);
                System.out.println("Cost Price: " + costPrice);
                System.out.println("Sale Price: " + salePrice);
                double profit = product.getSalePrice() - product.getCostPrice();
                System.out.println(String.valueOf(profit));
                System.out.println("-----------------------------");
                
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/qb/app/fxmlComponent/ReportStockBalance_TableRow.fxml"));
                    Node stockdata = loader.load();
                    ReportStockBalance_TableRowController controller = loader.getController();
                    controller.setData(product,brand,qty);
                    stockItemList.add(controller);
                    tableBody.getChildren().add(stockdata);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void loadFilterCombo(){
        cbFilter.getItems().addAll("ID",  "Brand Name","Product Name", "Quantity");
        cbFilter.setValue("Select Filter");
    }
    
    private void refreshInterface() {
        cbFilter.setValue(null);
        TFTotalProfit.setText("");
        TFTotalSaleValue.setText("");
        TFTotalStockValue.setText("");
        tableBody.getChildren().clear();
        
    }

    @FXML
    private void Refresh(ActionEvent event) {
         if (event.getSource() == btnRefresh) {
            refreshInterface();
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
    private void Loadreport(ActionEvent event) {
         if (event.getSource() == LoadReport) {
              loadData();
         }
        
    }

    
}
