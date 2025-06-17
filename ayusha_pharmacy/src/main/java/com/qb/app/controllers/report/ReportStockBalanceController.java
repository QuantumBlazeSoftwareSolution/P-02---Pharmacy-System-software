
package com.qb.app.controllers.report;

import com.qb.app.controllers.report.beans.BrandBean;
import com.qb.app.controllers.report.beans.ProductBean;
import com.qb.app.model.ComboBoxUtils;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.TestBrand;
import com.qb.app.model.TestProduct;
import com.qb.app.model.UnitTestingVihanga;
import com.qb.app.model.entity.Brand;
import com.qb.app.model.entity.Product;
import com.qb.app.model.entity.ProductStatus;
import com.qb.app.model.entity.Stock;
import com.qb.app.model.getLogger;
import com.qb.app.session.CompanyInfo;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

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
    @FXML
    private ComboBox<Brand> cbBrand;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            DefaultAPI.bindTableScroll(tableScroller, tableScrollContainer, tableBody);
        setEventListner();
        loadFilterCombo();
        loadBandCombo();
        
       
    }
    private void loadBandCombo(){
        ComboBoxUtils.loadComboBoxValues(cbBrand, Brand.class, "brand", Brand::getBrand);
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
            
            Predicate productStatusEnabled = cb.equal(PstatusJoin.get("status"), "Enable");

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
            tableBody.getChildren().clear();
            double totalStockValue=0;
            double totalSaleValue=0;
            double totalProfit=0;

            // Now loop through and extract needed info
            for (Stock stock : stockList) {
                Product product = stock.getProductId();
                Brand brand = product.getBrandId();

                double qty = stock.getQty();
                double rowCostprice = qty*product.getCostPrice();
                totalStockValue += rowCostprice;
                double rowSalePrice = qty*product.getSalePrice();
                totalSaleValue += rowSalePrice;
                double rowProfilt = rowSalePrice-rowCostprice  ;
                totalProfit += rowProfilt;
          
                
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
      
            TFTotalProfit.setText(String.format("Rs. %,.2f", totalProfit));
            TFTotalSaleValue.setText(String.format("Rs. %,.2f", totalSaleValue));
            TFTotalStockValue.setText(String.format("Rs. %,.2f", totalStockValue));
        });

    }

    private void loadFilterCombo(){
        cbFilter.getItems().addAll("ID",  "Brand Name","Product Name", "Quantity");
        cbFilter.setValue("Select Filter");
    }
    
    private void refreshInterface() {
        cbFilter.setValue(null);
        cbBrand.setValue(null);
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

    @FXML
    private void viewReport(ActionEvent event) {
             if (event.getSource() == ViewReport) {
              printGrnReport();
         }
    }
    
    private void printGrnReport() {
        JPATransaction.runInTransaction((em) -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Brand> brandQuery = cb.createQuery(Brand.class);
            Root<Brand> brandRoot = brandQuery.from(Brand.class);
            brandQuery.select(brandRoot);
            List<Brand> brandList = em.createQuery(brandQuery).getResultList();

            List<BrandBean> brandListBean = new ArrayList<>();
            double grandTotalSaleAmount = 0;
            double grandTotalStockAmount = 0;
            int grandTotalQty = 0;
            for (Brand brand : brandList) {
                
                // Step 2: Get all products for this brand with status "Enable"
                CriteriaQuery<Product> productQuery = cb.createQuery(Product.class);
                Root<Product> productRoot = productQuery.from(Product.class);
                productQuery.select(productRoot)
                        .where(
                                cb.and(
                                        cb.equal(productRoot.get("brandId"), brand)
                                )
                        );

                List<Product> productList = em.createQuery(productQuery).getResultList();
                List<ProductBean> productBeanList = new ArrayList<>();

                double brandTotalSaleAmount = 0;
                double brandTotalStockAmount = 0;
                int brandTotalQty = 0;

                for (Product product : productList) {
                  
                    // Step 3: Get stock qty for this product
                    CriteriaQuery<Stock> stockQuery = cb.createQuery(Stock.class);
                    Root<Stock> stockRoot = stockQuery.from(Stock.class);
                    stockQuery.select(stockRoot)
                            .where(cb.equal(stockRoot.get("productId"), product));
                             Stock stockdetails = em.createQuery(stockQuery).getSingleResult();
                    double qty = stockdetails.getQty();
                    double tSaleAmount = qty * product.getSalePrice(); // or SalePrice if needed
                    double tCostAmount = qty * product.getCostPrice(); // or SalePrice if needed

                    brandTotalQty += qty;
                    brandTotalSaleAmount += tSaleAmount;
                    brandTotalStockAmount += tCostAmount;

                    productBeanList.add(new ProductBean(
                            String.valueOf(product.getId()),
                            product.getProduct(),
                            product.getGenericName(),
                            String.format("Rs. %,.2f", product.getSalePrice()),
                            String.valueOf(qty),
                            String.format("Rs. %,.2f", tSaleAmount)
                    ));
                }
                  grandTotalSaleAmount += brandTotalSaleAmount;
                  grandTotalStockAmount += brandTotalStockAmount;

                brandListBean.add(new BrandBean(
                        brand.getBrand(),
                        productBeanList,
                        String.valueOf(productBeanList.size()),
                        String.format("Rs. %,.2f", brandTotalSaleAmount),
                        String.valueOf(brandTotalQty)
                ));
            }

            Map<String, Object> params = new HashMap<>();
            params.put("companyName", CompanyInfo.companyName);
            params.put("Address", CompanyInfo.address);
            params.put("Contact", CompanyInfo.mobile);
            params.put("ExpectedProfit", String.format("Rs. %,.2f", grandTotalSaleAmount-grandTotalStockAmount));
            params.put("TotalSaleValue",String.format("Rs. %,.2f", grandTotalSaleAmount ));
            params.put("TotalStockValue",String.format("Rs. %,.2f", grandTotalStockAmount));

            try {
                URL imageUrl = UnitTestingVihanga.class.getResource("/com/qb/app/assets/images/logo.png");
                params.put("Logo", imageUrl);
            } catch (Exception e) {
                e.printStackTrace();
                getLogger.logger().warning(e.toString());
            }

            try {
                JasperReport subReport = (JasperReport) JRLoader.loadObject(
                        UnitTestingVihanga.class.getResourceAsStream("/com/qb/app/reports/Pharmacy_Stock_Balance_Sub_Report.jasper"));
                params.put("SUB_REPORT_PATH", subReport);

                JasperReport mainReport = (JasperReport) JRLoader.loadObject(
                        UnitTestingVihanga.class.getResourceAsStream("/com/qb/app/reports/Pharmacy_Stock_Balance.jasper"));

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(brandListBean);

                JasperPrint report = JasperFillManager.fillReport(mainReport, params, dataSource);
                JasperViewer.viewReport(report, false);
            } catch (JRException e) {
                e.printStackTrace();
            }
        });

    }

}
