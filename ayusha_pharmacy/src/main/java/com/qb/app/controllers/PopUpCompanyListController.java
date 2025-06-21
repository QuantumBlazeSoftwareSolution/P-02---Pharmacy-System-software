package com.qb.app.controllers;

import com.qb.app.model.DefaultAPI;
import com.qb.app.model.InterfaceAction;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Company;
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

public class PopUpCompanyListController implements Initializable {

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

    public Supply_company_managementController callingController;
    @FXML
    private ComboBox<String> FilterBY;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DefaultAPI.bindTableScroll(TableScroller, TableScrollContainer, TableBody);
        pageIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        closeIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/close-icon.svg"));
        loadCompanies(null);
        LoadComboBox();
    }
    private void LoadComboBox(){
        FilterBY.getItems().addAll("ID", "Company Name");
        FilterBY.setValue("Select Filter");
    }

    public void saveCallingController(Supply_company_managementController controller) {
        this.callingController = controller;
    }

    private void loadCompanies(String searchTerm) {
        JPATransaction.runInTransaction((em) -> {
            // Clear existing items
            TableBody.getChildren().clear();

            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Company> cQuery = cBuilder.createQuery(Company.class);
            Root<Company> companny = cQuery.from(Company.class);

            // Add search condition if search term exists
            if (searchTerm != null && !searchTerm.isEmpty()) {
                String likePattern = "%" + searchTerm.toLowerCase() + "%";
                Predicate searchCondition
                        = cBuilder.like(cBuilder.lower(companny.get("name")), likePattern);
                cQuery.where(searchCondition);
            }
              String selectedSort = FilterBY.getValue();
        if ("Company Name".equals(selectedSort)) {
            cQuery.orderBy(cBuilder.asc(companny.get("name")));
        } else if ("ID".equals(selectedSort)) {
            cQuery.orderBy(cBuilder.asc(companny.get("id")));
        }

            cQuery.select(companny);
            List<Company> companyList = em.createQuery(cQuery).getResultList();

            // Create table rows for each product
            for (Company item : companyList) {
                createCompanyTableRow(item);
            }
        });
    }

    private void createCompanyTableRow(Company item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/qb/app/fxmlComponent/popUp_CompanyList_TableRow.fxml"));
            Node tableRow = loader.load();
            PopUp_CompanyList_TableRowController controller = loader.getController();

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
        loadCompanies(searchTerm);
    }

    @FXML
    private void FilterBYAction(ActionEvent event) {
    String searchTerm = tfSearch.getText().trim();
    loadCompanies(searchTerm);
    } 
    

}
