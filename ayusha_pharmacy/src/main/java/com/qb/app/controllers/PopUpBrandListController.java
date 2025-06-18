package com.qb.app.controllers;

import com.qb.app.model.DefaultAPI;
import com.qb.app.model.InterfaceAction;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Brand;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class PopUpBrandListController implements Initializable {

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

    public Product_brand_managementController callingController;
    
    @FXML
    private AnchorPane root;
    @FXML
    private TextField tfSearch;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DefaultAPI.bindTableScroll(TableScroller, TableScrollContainer, TableBody);
        pageIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        closeIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/close-icon.svg"));
        loadBrands(null);
    }

    public void saveCallingController(Product_brand_managementController controller) {
        this.callingController = controller;
    }

    private void loadBrands(String searchTerm) {
        JPATransaction.runInTransaction((em) -> {
            TableBody.getChildren().clear();

            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Brand> cQuery = cBuilder.createQuery(Brand.class);
            Root<Brand> brand = cQuery.from(Brand.class);

            if (searchTerm != null && !searchTerm.isEmpty()) {
                String likePattern = "%" + searchTerm.toLowerCase() + "%";
                Predicate searchCondition
                        = cBuilder.like(cBuilder.lower(brand.get("brand")), likePattern);
                cQuery.where(searchCondition);
            }

            cQuery.select(brand);
            List<Brand> brandList = em.createQuery(cQuery).getResultList();

            // Create table rows for each product
            for (Brand item : brandList) {
                createBrandTableRow(item);
            }
        });
    }

    private void createBrandTableRow(Brand item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/qb/app/fxmlComponent/popUp_BrandList_TableRow.fxml"));
            Node tableRow = loader.load();
            PopUp_BrandList_TableRowController controller = loader.getController();

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
        loadBrands(searchTerm);
    }

}
