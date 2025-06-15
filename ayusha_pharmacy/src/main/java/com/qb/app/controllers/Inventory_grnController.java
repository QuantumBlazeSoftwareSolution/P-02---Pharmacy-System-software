package com.qb.app.controllers;

import com.qb.app.App;
import com.qb.app.controllers.report.beans.GrnItemBean;
import com.qb.app.model.ComboBoxUtils;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Company;
import com.qb.app.model.entity.Grn;
import com.qb.app.model.entity.GrnItem;
import com.qb.app.model.entity.Product;
import com.qb.app.model.entity.Stock;
import com.qb.app.model.entity.Supplier;
import com.qb.app.model.getLogger;
import com.qb.app.session.ApplicationSession;
import com.qb.app.session.CompanyInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Inventory_grnController implements Initializable {

    // <editor-fold desc="FXML init component" defaultstate="collapsed">
    @FXML
    private ScrollPane grnTableScrollContainer;
    @FXML
    private VBox grnTableBody;
    @FXML
    private ScrollBar grnTableScroller;
    @FXML
    private Group GrnIcon;
    @FXML
    private ComboBox<Company> cbCompany;
    @FXML
    private ComboBox<Supplier> cbSupplier;
    @FXML
    private TextField tfGRNID;
    @FXML
    private TextField tfProductID;
    @FXML
    private TextField tfProductName;
    @FXML
    private TextField tfGenericName;
    @FXML
    private TextField tfCostPrice;
    @FXML
    private TextField tfQty;
    @FXML
    private TextField tfAmount;
    @FXML
    private TextField tfTotal;
    @FXML
    private Button btnAction;
    @FXML
    private AnchorPane root;
    @FXML
    private Label displayMessage;
    //</editor-fold>

    private Product loadedProduct;
    private boolean readyItemToAdd;
    private int itemQty;
    private Grn savedGrn;
    List<InventoryGRN_TableRowController> grnItemList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfQty.setDisable(false);
        DefaultAPI.bindTableScroll(grnTableScroller, grnTableScrollContainer, grnTableBody);
        GrnIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        tfQty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        loadComboBox();

    }

    private void loadComboBox() {
        ComboBoxUtils.loadComboBoxValues(cbCompany, Company.class, "name", Company::getName);
        ComboBoxUtils.loadComboBoxValues(cbSupplier, Supplier.class, "name", Supplier::getName);
    }

    @FXML
    private void actionEvent(ActionEvent event) {
        if (event.getSource() == btnAction) {
            makeGrn();
        }
    }

    private void makeGrn() {
        System.out.println("grnItemList sizee: makeGrn()" + grnItemList.size());

        if (isEntriesValid()) {
            if (isGrnIdAvailable()) {
                if (grnItemList.isEmpty() || grnItemList.size() == 0 || grnItemList == null) {
                    CustomAlert.showStyledAlert(root, "messagee.", "No item to GRN", Alert.AlertType.WARNING);
                } else {
                    createGrn();
                }
            } else {
                CustomAlert.showStyledAlert(root, "This GRN number is already registered in the system.", "GRN Duplication Warning", Alert.AlertType.WARNING);
            }
        }
    }

    private boolean isEntriesValid() {
        if (cbCompany.getValue() == null) {
            displayWarningMessage("Please select the supply company", false);
            cbCompany.requestFocus();
            return false;
        }

        if (cbSupplier.getValue() == null) {
            displayWarningMessage("Please select the supplier", false);
            cbSupplier.requestFocus();
            return false;
        }

        if (tfGRNID.getText().isEmpty()) {
            displayWarningMessage("Please enter the grn id", false);
            tfGRNID.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isGrnIdAvailable() {
        return JPATransaction.runInTransaction((em) -> {
            String grnID = tfGRNID.getText();
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

            return em.createQuery(cq).getSingleResult() == 0;
        });
    }

    private void createGrn() {
        System.out.println("grnItemList sizee: createGrn()" + grnItemList.size());

        JPATransaction.runInTransaction((em) -> {
            Grn grn = new Grn();
            grn.setGrnCode(tfGRNID.getText());
            grn.setDateTime(new Date());
            grn.setSupplierId(cbSupplier.getValue());
            em.persist(grn);
            em.flush();

            this.savedGrn = grn;

            for (InventoryGRN_TableRowController item : grnItemList) {
                GrnItem grnItem = new GrnItem();
                grnItem.setCostPrice(item.getCost());
                grnItem.setQty(item.getQty());
                grnItem.setProductId(item.getProduct());
                grnItem.setGrnId(grn);
                em.persist(grnItem);

                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<Stock> cq = cb.createQuery(Stock.class);
                Root<Stock> stockTable = cq.from(Stock.class);
                cq.where(cb.equal(stockTable.get("productId"), item.getProduct()));

                Stock stock = em.createQuery(cq).getSingleResult();
                stock.setQty(stock.getQty() + (item.getQty() * item.getProduct().getMeasure()));
                em.persist(stock);
            }

            printGrnReport(grn);
            grnItemList.clear();
            grnTableBody.getChildren().clear();
            tfTotal.setText("");
            tfGRNID.setText("");
            cbCompany.setValue(null);
            cbSupplier.setValue(null);
        });
    }

    private void displayWarningMessage(String message, boolean action) {
        if (action) {
            displayMessage.setStyle("-fx-text-fill: #0D9F00;"); // Green
        } else {
            displayMessage.setStyle("-fx-text-fill: #FF3333;"); // Red
        }
        displayMessage.setText(message);

        PauseTransition delay = new PauseTransition(Duration.seconds(10));
        delay.setOnFinished(event -> displayMessage.setText(""));
        delay.play();
    }

    @FXML
    private void handlePopUpProductView(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (tfProductID.getText().isEmpty()) {
                try {
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("popUpProductList.fxml"));
                    Parent root = loader.load();

                    // Create a new stage for the popup
                    Stage popupStage = new Stage();
                    popupStage.initOwner(tfProductID.getScene().getWindow());
                    popupStage.initModality(Modality.APPLICATION_MODAL);

                    // Get screen dimensions
                    Screen screen = Screen.getPrimary();
                    Rectangle2D bounds = screen.getVisualBounds();

                    // Create scene with full width but original height
                    Scene scene = new Scene(root);
                    popupStage.setScene(scene);

                    // Set width to screen width and position at x=0
                    popupStage.setWidth(bounds.getWidth());
                    popupStage.setX(0); // This ensures no left gap

                    // Set fixed height (adjust as needed)
                    popupStage.setHeight(600);

                    // Center the popup vertically
                    popupStage.setY((bounds.getHeight() - popupStage.getHeight()) / 2);

                    popupStage.initStyle(StageStyle.TRANSPARENT);

                    // Get controller reference
                    PopUpProductListController controller = loader.getController();
                    controller.saveProductRegistrationController(this);

                    popupStage.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            } else {
                loadProductDetails();
            }
        }
    }

    public void setParentID(String id) {
        tfProductID.setText(id);
    }

    private void loadProductDetails() {
        JPATransaction.runInTransaction((em) -> {
            try {
                int productID = Integer.parseInt(tfProductID.getText());
                Product product = em.find(Product.class, productID);
                if (product != null) {
                    this.loadedProduct = product;
                    tfProductName.setText(product.getProduct());
                    tfGenericName.setText(product.getGenericName());
                    tfCostPrice.setText(String.valueOf(product.getCostPrice()));
                    tfQty.setDisable(false);
                    tfQty.requestFocus();
                } else {
                    displayWarningMessage("Product not found.", false);
                }
            } catch (Exception e) {
                displayWarningMessage("Invalid Product ID.", false);
            }
        });
    }

    private void calculateLoadedItemAmount() {
        int qty = Integer.parseInt(tfQty.getText());
        this.itemQty = qty;
        double productAmount = loadedProduct.getCostPrice() * qty;
        tfAmount.setText(String.format("Rs. %,.2f", productAmount));
        this.readyItemToAdd = true;
    }

    @FXML
    private void qtyListener(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (this.readyItemToAdd) {
                addItemToList();
            } else {
                String qty = tfQty.getText();
                if (qty.isEmpty() || Integer.parseInt(qty) <= 0) {
                    displayWarningMessage("To proceed, please enter the quantity for this product.", false);
                    this.readyItemToAdd = false;
                } else {
                    calculateLoadedItemAmount();
                }
            }
        } else {
            this.readyItemToAdd = false;
        }
    }

    private void addItemToList() {
        System.out.println("grnItemList sizee: addItemToList()" + grnItemList.size());

        try {
            boolean itemExists = false;

            for (InventoryGRN_TableRowController item : grnItemList) {
                if (item.getProductID() == loadedProduct.getId()) {
                    item.setProductQty(item.getQty() + itemQty);
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/qb/app/fxmlComponent/InventoryGRN_TableRow.fxml"));
                Node grnItem = loader.load();
                InventoryGRN_TableRowController controller = loader.getController();
                controller.setData(loadedProduct, loadedProduct.getId(), loadedProduct.getProduct(), loadedProduct.getCostPrice(), itemQty);

                grnItemList.add(controller);
                grnTableBody.getChildren().add(grnItem);
            }

            calculateTotal();
            clearLoadedTextFields();
            resetFields();
        } catch (IOException e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void resetFields() {
        this.loadedProduct = null;
        this.readyItemToAdd = false;
        this.itemQty = 0;
        tfQty.setDisable(true);
    }

    private void clearLoadedTextFields() {
        tfProductID.setText("");
        tfProductName.setText("");
        tfGenericName.setText("");
        tfCostPrice.setText("");
        tfQty.setText("");
        tfAmount.setText("");

        tfProductID.requestFocus();
    }

    private void printGrnReport(Grn grn) {
        Map<String, Object> params = getJRParams();
        Vector<GrnItemBean> collection = getBeanCollection();

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
                    getClass().getResourceAsStream("/com/qb/app/reports/PharmacyGRN.jasper"));

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(collection);

            JasperPrint report = JasperFillManager.fillReport(jasperReport, params, dataSource);
//            JasperPrintManager.printReport(report, false);
            JasperViewer.viewReport(report, false);
        } catch (JRException e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
            CustomAlert.showStyledAlert(root, "Report generation failed: " + e.getMessage(), "Reporting Error", Alert.AlertType.ERROR);
        }
    }

    private Map<String, Object> getJRParams() {
        Map<String, Object> params = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy hh:mm a");
        String grnTime = savedGrn.getDateTime().toInstant().atZone(ZoneId.systemDefault()).format(formatter);

        params.put("GrnTime", grnTime);
        params.put("GrnID", savedGrn.getGrnCode());
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
        System.out.println("grnItemList sizee: getBeanCollection()" + grnItemList.size());

        Vector<GrnItemBean> collection = new Vector<>();
        for (InventoryGRN_TableRowController item : grnItemList) {
            GrnItemBean bean = new GrnItemBean(
                    String.valueOf(item.getProduct().getId()),
                    item.getProduct().getProduct(),
                    item.getProduct().getGenericName() != null ? item.getProduct().getGenericName() : "",
                    String.format("Rs. %,.2f", item.getProduct().getCostPrice()),
                    String.valueOf(item.getQty()),
                    String.format("Rs. %,.2f", item.getItemAmount())
            );
            collection.add(bean);
        }

        return collection;
    }

    private void getGrnTotal(Map<String, Object> params) {
        System.out.println("grnItemList sizee: getGrnTotal()" + grnItemList.size());

        double total = 0;
        for (InventoryGRN_TableRowController item : grnItemList) {
            total += item.getItemAmount();
        }
        params.put("SubTotal", String.format("Rs. %,.2f", total));
        params.put("Discount", "Rs. 0.00");
        params.put("Total", String.format("Rs. %,.2f", total));
        params.put("TotalQty", String.valueOf(grnItemList.size()));
    }

    private void calculateTotal() {
        double total = 0;
        for (InventoryGRN_TableRowController item : grnItemList) {
            total += item.getItemAmount();
        }
        tfTotal.setText(String.format("Rs. %,.2f", total));
    }

}
