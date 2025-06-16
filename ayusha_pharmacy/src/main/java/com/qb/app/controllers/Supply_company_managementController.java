/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers;

import com.qb.app.App;
import com.qb.app.model.CustomAlert;
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
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Vihanga
 */
public class Supply_company_managementController implements Initializable {

    @FXML
    private Group iconSupplyCompanyManagementTopic;
    @FXML
    private TextField spCompanyName;
    @FXML
    private TextField spCompanyAddress;
    @FXML
    private TextField spCompanyMobile_1;
    @FXML
    private TextField spCompanyMobile_2;
    @FXML
    private Button btnAddCompany;
    @FXML
    private Button btnClearCompany;
    @FXML
    private TextField companyId;
    @FXML
    private TextField updateCompanyName;
    @FXML
    private TextField updateCompanyAddress;
    @FXML
    private TextField updateCompanyTelephone_1;
    @FXML
    private TextField updateCompanyTelephone_2;
    @FXML
    private Button btnUpdateCompanyDetails;

    private Company loadedCompany;
    private boolean isCompanyLoaded;
    @FXML
    private Button btnUpdateClearCompany;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIcon();
    }

    private void setIcon() {
        iconSupplyCompanyManagementTopic.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
    }

    @FXML
    private void addCompanyActionEvent(ActionEvent event) {
        if (event.getSource() == btnAddCompany) {
            addCompany();
        } else if (event.getSource() == btnClearCompany) {
            clearAddCompanyFields();
        }
    }

    private void addCompany() {

        if (IsCompanyValid()) {
            if (!isCompanyExist()) {

                JPATransaction.runInTransaction(em -> {
                    try {
                        //save a company
                        Company company = new Company();
                        company.setName(spCompanyName.getText());
                        company.setAddress(spCompanyAddress.getText());
                        company.setTelephone1(spCompanyMobile_1.getText());
                        company.setTelephone2(spCompanyMobile_2.getText());

                        em.persist(company);
                        System.out.println("Company added Successful");
                        clearAddCompanyFields();
                        CustomAlert.showStyledAlert(root, "Company added Successful", Alert.AlertType.CONFIRMATION);

                    } catch (Exception e) {
                        e.printStackTrace();
                        getLogger.logger().warning(e.toString());
                    }
                });

            }
        }

    }

    private boolean IsCompanyValid() {
        String companyName = spCompanyName.getText().trim();
        if (companyName.isEmpty()) {
            CustomAlert.showStyledAlert(root, "Company name is required.", Alert.AlertType.WARNING);
            spCompanyName.requestFocus();
            return false;
        }

        String telephone_1 = spCompanyMobile_1.getText().trim();
        if (!telephone_1.isEmpty()) {
            if (!telephone_1.matches("\\d{10}")) {
                CustomAlert.showStyledAlert(root, "Telephone Number 01 must be exactly 10 digits.", Alert.AlertType.WARNING);
                spCompanyMobile_1.requestFocus();
                return false;
            }
        }

        String telephone_2 = spCompanyMobile_2.getText().trim();
        if (!telephone_2.isEmpty()) {
            if (!telephone_2.matches("\\d{10}")) {
                CustomAlert.showStyledAlert(root, "Telephone Number 02 must be exactly 10 digits.", Alert.AlertType.WARNING);
                spCompanyMobile_2.requestFocus();
                return false;
            }
        }

        return true;
    }

    private boolean isCompanyExist() {
        return JPATransaction.runInTransaction((em) -> {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Company> cQuery = cBuilder.createQuery(Company.class);
            Root<Company> companyTable = cQuery.from(Company.class);

            Predicate companyPredicate1 = cBuilder.equal(
                    cBuilder.lower(companyTable.get("name")),
                    spCompanyName.getText().toLowerCase()
            );

            Predicate companyPredicate2 = cBuilder.equal(
                    cBuilder.lower(companyTable.get("address")),
                    spCompanyAddress.getText().toLowerCase()
            );

            // Use OR instead of AND
            cQuery.where(cBuilder.or(companyPredicate1, companyPredicate2));

            return !em.createQuery(cQuery).getResultList().isEmpty();
        });
    }

    private void clearAddCompanyFields() {
        spCompanyName.setText("");
        spCompanyAddress.setText("");
        spCompanyMobile_1.setText("");
        spCompanyMobile_2.setText("");
    }

    @FXML
    private void handlePopUpCompanyView(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (companyId.getText().isEmpty()) {
                try {
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("popUpCompanyList.fxml"));
                    Parent root = loader.load();

                    // Create a new stage for the popup
                    Stage popupStage = new Stage();
                    popupStage.initOwner(this.root.getScene().getWindow());
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
                    PopUpCompanyListController controller = loader.getController();
                    controller.saveCallingController(this);

                    popupStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            } else {
                JPATransaction.runInTransaction((em) -> {

                    try {
                        int CompanyId = Integer.parseInt(companyId.getText());
                        Company company = em.find(Company.class, CompanyId);
                        if (company != null) {
                            this.loadedCompany = company;
                            isCompanyLoaded = true;
                            updateCompanyName.setText(company.getName());
                            updateCompanyAddress.setText(company.getAddress());
                            updateCompanyTelephone_1.setText(String.valueOf(company.getTelephone1()));
                            updateCompanyTelephone_2.setText(String.valueOf(company.getTelephone2()));

                        } else {
                            CustomAlert.showStyledAlert(root, "Company not found", Alert.AlertType.WARNING);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getLogger.logger().warning(e.toString());
                    }

                });
            }

        }

    }

    private boolean isValidCompanyID() {
        return JPATransaction.runInTransaction((em) -> {

            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Company> cQuery = cBuilder.createQuery(Company.class);
            Root<Company> companyTable = cQuery.from(Company.class);

            Predicate predicate2 = cBuilder.equal(companyTable.get("id"), Integer.parseInt(companyId.getText()));
            cQuery.where(predicate2);

            return !em.createQuery(cQuery).getResultList().isEmpty();
        });
    }

    @FXML
    private void updateCompanyActionEvent(ActionEvent event) {

        if (event.getSource() == btnUpdateCompanyDetails) {
            updateCompany();
        } else if (event.getSource() == btnUpdateClearCompany) {
            clearUpdateCompanyFields();
        }
    }

    private void updateCompany() {

        if (IsValidCompanyDetails()) {
            if (isCompanyLoaded) {
                JPATransaction.runInTransaction((em) -> {
                    try {

                        Company company = new Company();
                        loadedCompany.setName(updateCompanyName.getText());
                        loadedCompany.setAddress(updateCompanyAddress.getText());
                        loadedCompany.setTelephone1(updateCompanyTelephone_1.getText());
                        loadedCompany.setTelephone2(updateCompanyTelephone_2.getText());

                        em.merge(loadedCompany);
                        clearUpdateCompanyFields();

                        CustomAlert.showStyledAlert(root, "Company successfully Updated", Alert.AlertType.CONFIRMATION);

                    } catch (Exception e) {
                        e.printStackTrace();
                        getLogger.logger().warning(e.toString());
                    }

                });

            }

        }
    }

    private boolean IsValidCompanyDetails() {
        if (companyId.getText().isEmpty()) {
            CustomAlert.showStyledAlert(root, "Company ID is required.", Alert.AlertType.WARNING);
            companyId.requestFocus();
            return false;
        }

        if (updateCompanyName.getText().trim().isEmpty()) {
            CustomAlert.showStyledAlert(root, "Company Name is required.", Alert.AlertType.WARNING);
            updateCompanyName.requestFocus();
            return false;
        }

        String telephone_1 = updateCompanyTelephone_1.getText();
        if (telephone_1 != null && !telephone_1.trim().isEmpty()) {
            if (telephone_1.length() != 10) {
                CustomAlert.showStyledAlert(root, "Telephone Number 01 must be exactly 10 digits.", Alert.AlertType.WARNING);
                updateCompanyTelephone_1.requestFocus();
                return false;
            }
        }

        String telephone_2 = updateCompanyTelephone_2.getText();
        if (telephone_2 != null && !telephone_2.trim().isEmpty()) {
            if (telephone_2.length() != 10) {
                CustomAlert.showStyledAlert(root, "Telephone Number 02 must be exactly 10 digits.", Alert.AlertType.WARNING);
                updateCompanyTelephone_2.requestFocus();
                return false;
            }
        }
        return true;
    }

    private void clearUpdateCompanyFields() {
        companyId.setText("");
        updateCompanyName.setText("");
        updateCompanyAddress.setText("");
        updateCompanyTelephone_1.setText("");
        updateCompanyTelephone_2.setText("");

        isCompanyLoaded = false;
        loadedCompany = null;
    }

    public void setCompanyID(String id) {
        companyId.setText(id);
    }

}
