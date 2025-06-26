package com.qb.app.controllers;

import com.qb.app.model.DefaultAPI;
import com.qb.app.model.InterfaceAction;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Supplier;
import com.qb.app.model.getLogger;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class PopUpSupplierListController implements Initializable {

    @FXML
    private Group pageIcon;
    @FXML
    private Group closeIcon;
    @FXML
    private ScrollPane TableScrollContainer;
    @FXML
    private VBox TableBody;
    @FXML
    private ScrollBar TableScroller;
    @FXML
    private TextField tfSearch;
    @FXML
    private AnchorPane root;

    public Supply_supplier_managementController callingController;
    @FXML
    private ComboBox<String> FilterBy;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DefaultAPI.bindTableScroll(TableScroller, TableScrollContainer, TableBody);
        pageIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        closeIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/close-icon.svg"));
        loadSuppliers(null);
        loadComboBox();
    }
    private void loadComboBox() {
        FilterBy.getItems().addAll("ID", "Supplier Name", "Company Name");
        FilterBy.setValue("Select Filter");
    }

    public void saveCallingController(Supply_supplier_managementController controller) {
        this.callingController = controller;
    }

    private void loadSuppliers(String searchTerm) {
        JPATransaction.runInTransaction((em) -> {
            TableBody.getChildren().clear();

            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Supplier> cQuery = cBuilder.createQuery(Supplier.class);
            Root<Supplier> supplier = cQuery.from(Supplier.class);

            if (searchTerm != null && !searchTerm.isEmpty()) {
                String likePattern = "%" + searchTerm.toLowerCase() + "%";
                Predicate searchCondition
                        = cBuilder.like(cBuilder.lower(supplier.get("name")), likePattern);
                cQuery.where(searchCondition);
            }
                    String selectedSort = FilterBy.getValue();
        if ("Supplier Name".equals(selectedSort)) {
            cQuery.orderBy(cBuilder.asc(supplier.get("name")));
        } else if ("Company Name".equals(selectedSort)) {
            cQuery.orderBy(cBuilder.asc(supplier.get("companyId")));
        } else if ("ID".equals(selectedSort)) {
            cQuery.orderBy(cBuilder.asc(supplier.get("id")));
        }

            cQuery.select(supplier);
            List<Supplier> brandList = em.createQuery(cQuery).getResultList();

            // Create table rows for each product
            for (Supplier item : brandList) {
                createSupplierTableRow(item);
            }
        });
    }

    private void createSupplierTableRow(Supplier item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/qb/app/fxmlComponent/popUp_SupplierList_TableRow.fxml"));
            Node tableRow = loader.load();
            PopUp_SupplierList_TableRowController controller = loader.getController();

            // Set controllers
            controller.setPopUpController(this);

            // Set item data
            controller.setItems(item);
            TableBody.getChildren().add(tableRow);
        } catch (IOException e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    @FXML
    private void closePopUp(MouseEvent event) {
        InterfaceAction.closeWindow(root);
    }

    public void closeWindow() {
        InterfaceAction.closeWindow(root);
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        String searchTerm = tfSearch.getText().trim();
        loadSuppliers(searchTerm);
    }

    @FXML
    private void FilterActiion(ActionEvent event) {
          String searchTerm = tfSearch.getText().trim();
    loadSuppliers(searchTerm);
    } 

}
