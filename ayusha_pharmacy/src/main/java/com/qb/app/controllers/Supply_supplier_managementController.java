package com.qb.app.controllers;

import com.qb.app.model.ComboBoxUtils;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Company;
import com.qb.app.model.entity.Supplier;
import com.qb.app.model.entity.SupplierStatus;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class Supply_supplier_managementController implements Initializable {

    @FXML
    private Group suppierManagementIcon;
    @FXML
    private TextField supplierName;
    @FXML
    private TextField supplierTelephone;
    @FXML
    private Button btnAddSupplier;
    @FXML
    private ComboBox<Company> suppilerCompanyComboBox;
    @FXML
    private ComboBox<Company> updateSpCpComboBox;
    @FXML
    private Button btnSupplierDetailsClear;
    @FXML
    private TextField supplierId;
    @FXML
    private ComboBox<SupplierStatus> updateSpStatus;
    @FXML
    private Button btnClearUpdateSpDetails;
    @FXML
    private Button btnUpdateSpDetails;
    @FXML
    private TextField updateSpName;

    private Supplier loadedSupplier;
    private boolean isSupplierLoaded;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        suppierManagementIcon.getChildren().add(new SVGIconGroup("/com/qb/app/assets/icons/page-icon.svg"));
        loadSupplierComboBox();
    }

    private void loadSupplierComboBox() {
        ComboBoxUtils.loadComboBoxValues(suppilerCompanyComboBox, Company.class, "name", Company::getName);
        ComboBoxUtils.loadComboBoxValues(updateSpCpComboBox, Company.class, "name", Company::getName);
        ComboBoxUtils.loadComboBoxValues(updateSpStatus, SupplierStatus.class, "status", SupplierStatus::getStatus);
    }

    @FXML
    private void addSupplierActionEvent(ActionEvent event) {
        if (event.getSource() == btnAddSupplier) {
            addSupplier();
        } else if (event.getSource() == btnSupplierDetailsClear) {
            clearAddSupplierFields();
        }
    }

    private void addSupplier() {

        if (IsSupplierValid()) {
            if (!isSupplierExist()) {
                JPATransaction.runInTransaction(em -> {
                    try {
                        //save a Supplier
                        Supplier supplier = new Supplier();
                        supplier.setName(supplierName.getText());
                        supplier.setTelephone(supplierTelephone.getText());
                        supplier.setCompanyId(suppilerCompanyComboBox.getValue());
                        supplier.setSupplierStatusId(getSupplierStatus());

                        em.persist(supplier);

                        clearAddSupplierFields();
                        System.out.println("save cpmpany okkkkk....");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                System.out.println("this supplier is already added");
            }

        }

    }

    private boolean IsSupplierValid() {
        if (supplierName.getText().isEmpty()) {
            System.out.println("Company name is required.");
            supplierName.requestFocus();
            return false;
        }

        String telephone_1 = supplierTelephone.getText();
        if (telephone_1 != null && !telephone_1.trim().isEmpty()) {
            if (telephone_1.length() != 10) {
                System.out.println("Telephone Number 01 must be exactly 10 digits.");
                supplierTelephone.requestFocus();
                return false;
            }
        }

        if (suppilerCompanyComboBox.getValue() == null) {
//            displayRegistrationMessage("Please select a Company.", false);
            System.out.println("Please select a Company.");
            suppilerCompanyComboBox.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isSupplierExist() {
        return JPATransaction.runInTransaction((em) -> {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Supplier> cQuery = cBuilder.createQuery(Supplier.class);
            Root<Supplier> supplierTable = cQuery.from(Supplier.class);

            Predicate supplierPredicate1 = cBuilder.equal(
                    cBuilder.lower(supplierTable.get("name")),
                    supplierName.getText().toLowerCase()
            );

            Predicate supplierPredicate2 = cBuilder.equal(
                    cBuilder.lower(supplierTable.get("telephone")),
                    supplierTelephone.getText().toLowerCase()
            );

            // Use OR instead of AND
            cQuery.where(cBuilder.or(supplierPredicate1, supplierPredicate2));

            return !em.createQuery(cQuery).getResultList().isEmpty();
        });
    }

    private SupplierStatus getSupplierStatus() {
        return JPATransaction.runInTransaction((em) -> {
            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<SupplierStatus> cq = cb.createQuery(SupplierStatus.class);
                Root<SupplierStatus> root = cq.from(SupplierStatus.class);

                cq.where(cb.equal(root.get("status"), "Active"));

                return em.createQuery(cq).getSingleResult();

            } catch (NoResultException e) {
                // Handle case where no "Enable" status exists
                System.err.println("No SupplierStatus with status='Active' found");
                return null;
            }
        });
    }

    private void clearAddSupplierFields() {
        supplierName.setText("");
        supplierTelephone.setText("");
        suppilerCompanyComboBox.getSelectionModel().clearSelection();
        suppilerCompanyComboBox.setPromptText("Select");
    }

    // Supplier update Process 
    @FXML
    private void handlePopUpSupplierView(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (supplierId.getText().isEmpty()) {
                // view popup window
            } else {
                JPATransaction.runInTransaction((em) -> {

                    try {
                        int SupplierId = Integer.parseInt(supplierId.getText());
                        Supplier supplier = em.find(Supplier.class, SupplierId);
                        if (supplier != null) {
                            this.loadedSupplier = supplier;
                            isSupplierLoaded = true;
                            updateSpName.setText(supplier.getName());
                            updateSpCpComboBox.setValue(supplier.getCompanyId());
                            updateSpStatus.setValue(supplier.getSupplierStatusId());

                        } else {
                            // displayWarningMessage("Supplier not found.", false);
                            System.out.println("Supplier not found.");
                        }
                    } catch (Exception e) {
//                displayWarningMessage("Invalid Supplier ID.", false);
                    }

                });

            }

        }
    }

    @FXML
    private void updateSupplierActionEvent(ActionEvent event) {

        if (event.getSource() == btnUpdateSpDetails) {
            UpdateSupplier();
        } else if (event.getSource() == btnClearUpdateSpDetails) {
            clearUpdateSupplierFields();
        }
        
    }

    private void UpdateSupplier() {
        
        if (UpdateSupplierValid()) {
            if (isSupplierLoaded) {
                JPATransaction.runInTransaction((em) -> {
                    try {

                        Supplier supplier = new Supplier();
                        loadedSupplier.setName(updateSpName.getText());
                        loadedSupplier.setCompanyId(updateSpCpComboBox.getValue());
                        loadedSupplier.setSupplierStatusId(updateSpStatus.getValue());

                        em.merge(loadedSupplier);
                        clearUpdateSupplierFields();

                        System.out.println("Company successfully Updated");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
            }
            
        }
        

    }

    private boolean UpdateSupplierValid() {
        if (supplierId.getText().isEmpty()) {
            System.out.println("Supplier ID is required.");
            supplierId.requestFocus();
            return false;
        }

        if (updateSpName.getText().isEmpty()) {
            System.out.println("Company name is required.");
            updateSpName.requestFocus();
            return false;
        }

//        String telephone_1 = supplierTelephone.getText();
//        if (telephone_1 != null && !telephone_1.trim().isEmpty()) {
//            if (telephone_1.length() != 10) {
//                System.out.println("Telephone Number 01 must be exactly 10 digits.");
//                supplierTelephone.requestFocus();
//                return false;
//            }
//        }
        if (updateSpCpComboBox.getValue() == null) {
//            displayRegistrationMessage("Please select a Company.", false);
            System.out.println("Please select a Company.");
            updateSpCpComboBox.requestFocus();
            return false;
        }

        return true;
    }
    
    private void clearUpdateSupplierFields() {
        supplierId.setText("");
        updateSpName.setText("");
        updateSpCpComboBox.getSelectionModel().clearSelection();
        updateSpStatus.getSelectionModel().clearSelection();

        
        isSupplierLoaded = false;
        loadedSupplier = null;    
        
        

    }

}
