package com.qb.app.controllers;

import com.qb.app.model.SVGIconGroup;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.qb.app.model.CustomAlert;
import static com.qb.app.model.JPATransaction.runInTransaction;
import com.qb.app.model.entity.Brand;
import com.qb.app.model.entity.ProductStatus;
import com.qb.app.model.entity.Session;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

public class Product_brand_managementController implements Initializable {

    //<editor-fold desc="FXML init component" defaultstate="collapsed">
    @FXML
    private Group iconPage;
    @FXML
    private TextField tfPrimaryBrandName;
    @FXML
    private Button btnPrimaryClear;
    @FXML
    private Button btnPrimaryRegister;
    //</editor-fold>
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iconPage.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
    }

    @FXML
    private void handleActionEvent(ActionEvent event) {
        if (event.getSource() == btnPrimaryRegister) {
            BrandRegistration();
        }
    }

    private void BrandRegistration() {
        if (checkRegistrationValidity()) {
            if (!isBrandExist()) {
                registerNewBrand();
            } else {
                CustomAlert.showStyledAlert(root, "This brand name is already registered. Please choose a different name.", Alert.AlertType.WARNING);
            }
        }
    }

    private boolean checkRegistrationValidity() {
        if (tfPrimaryBrandName.getText().isEmpty() || tfPrimaryBrandName.getText().equals("")) {
            CustomAlert.showStyledAlert(root, "Brand name is required and cannot be blank.", Alert.AlertType.WARNING);
        } else {
            return true;
        }
        return false;
    }

    private boolean isBrandExist() {
        return runInTransaction(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Brand> cq = cb.createQuery(Brand.class);
            Root<Brand> brandTable = cq.from(Brand.class);

            Predicate predicate = cb.equal(brandTable.get("brand"), tfPrimaryBrandName.getText());
            cq.where(predicate);
            List<Brand> results = em.createQuery(cq).getResultList();
            return !results.isEmpty();
        });
    }

    private void registerNewBrand() {
        runInTransaction(em -> {
            Brand newBrand = new Brand();
            newBrand.setBrand(tfPrimaryBrandName.getText());
            newBrand.setProductStatusId(getProductStatus("Enable"));
            em.persist(newBrand);
        });
    }

    private ProductStatus getProductStatus(String status) {
        runInTransaction(em -> {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<ProductStatus> cQuery = cBuilder.createQuery(ProductStatus.class);
            Root<ProductStatus> productStatusTable = cQuery.from(ProductStatus.class);

            Predicate prediction = cBuilder.equal(productStatusTable.get("status"), status);
            cQuery.where(prediction);

            TypedQuery<ProductStatus> query = em.createQuery(cQuery);
            ProductStatus productStatus = null;

            try {
                productStatus = query.getSingleResult();
            } catch (Exception e) {
                productStatus = null;
            }

            if (productStatus == null) {
                System.out.println("Cannot find the product status");
                return;
            }
        });
        return null;
    }

}
