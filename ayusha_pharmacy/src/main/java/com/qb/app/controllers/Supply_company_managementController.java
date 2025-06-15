/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.qb.app.controllers;

import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Company;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

                        System.out.println("save cpmpany okkkkk....");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        }

    }

    private boolean IsCompanyValid() {
        if (spCompanyName.getText().isEmpty()) {
            System.out.println("Company name is required.");
            spCompanyName.requestFocus();
            return false;
        }

        if (spCompanyAddress.getText().isEmpty()) {
            System.out.println("Company Address is required.");
            spCompanyAddress.requestFocus();
            return false;
        }

        if (spCompanyMobile_1.getText().isEmpty()) {
            System.out.println("Telephone Number 01 is required.");
            spCompanyMobile_1.requestFocus();
            return false;
        }

        if (spCompanyMobile_2.getText().isEmpty()) {
            System.out.println("Telephone Number 02 is required.");
            spCompanyMobile_2.requestFocus();
            return false;
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




}
