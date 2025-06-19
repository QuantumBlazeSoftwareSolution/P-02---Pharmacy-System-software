package com.qb.app.controllers;

import com.qb.app.App;
import com.qb.app.model.ComboBoxUtils;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.PopUp;
import com.qb.app.model.SVGIconGroup;
import com.qb.app.model.entity.Company;
import com.qb.app.model.entity.Supplier;
import com.qb.app.model.entity.SupplierStatus;
import com.qb.app.model.getLogger;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    @FXML
    private TextField updateSpTelephone;
    @FXML
    private AnchorPane root;

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
                        CustomAlert.showStyledAlert(root, "Supplier added Successful.", Alert.AlertType.CONFIRMATION);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                CustomAlert.showStyledAlert(root, "This supplier is already added", Alert.AlertType.WARNING);
            }

        }

    }

    private boolean IsSupplierValid() {
        if (supplierName.getText().isEmpty()) {
            CustomAlert.showStyledAlert(root, "Supplier name is required.", Alert.AlertType.WARNING);
            supplierName.requestFocus();
            return false;
        }

        String telephone_1 = supplierTelephone.getText();
        if (telephone_1 != null && !telephone_1.trim().isEmpty()) {
            if (telephone_1.length() != 10) {
                CustomAlert.showStyledAlert(root, "Telephone Number 01 must be exactly 10 digits.", Alert.AlertType.WARNING);
                supplierTelephone.requestFocus();
                return false;
            }
        }

        if (suppilerCompanyComboBox.getValue() == null) {
            CustomAlert.showStyledAlert(root, "Please select a Company.", Alert.AlertType.WARNING);
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
                openPopUp();
            } else {
                loadSupplier();
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
                        loadedSupplier.setTelephone(updateSpTelephone.getText());
                        loadedSupplier.setCompanyId(updateSpCpComboBox.getValue());
                        loadedSupplier.setSupplierStatusId(updateSpStatus.getValue());

                        em.merge(loadedSupplier);
                        clearUpdateSupplierFields();

                        CustomAlert.showStyledAlert(root, "Supplier successfully Updated", Alert.AlertType.CONFIRMATION);

                    } catch (Exception e) {
                        e.printStackTrace();
                        getLogger.logger().warning(e.toString());
                    }

                });
            }

        }

    }

    private boolean UpdateSupplierValid() {
        if (supplierId.getText().isEmpty()) {
            CustomAlert.showStyledAlert(root, "Supplier ID is required.", Alert.AlertType.WARNING);
            supplierId.requestFocus();
            return false;
        }

        if (updateSpName.getText().isEmpty()) {
            CustomAlert.showStyledAlert(root, "Supplier name is required.", Alert.AlertType.WARNING);
            updateSpName.requestFocus();
            return false;
        }

        String telephone_1 = updateSpTelephone.getText();
        if (telephone_1 != null && !telephone_1.trim().isEmpty()) {
            if (telephone_1.length() != 10) {
                CustomAlert.showStyledAlert(root, "Telephone Number 01 must be exactly 10 digits.", Alert.AlertType.WARNING);
                updateSpTelephone.requestFocus();
                return false;
            }
        }
        if (updateSpCpComboBox.getValue() == null) {
//            displayRegistrationMessage("Please select a Company.", false);
            CustomAlert.showStyledAlert(root, "Please select a Company.", Alert.AlertType.WARNING);
            updateSpCpComboBox.requestFocus();
            return false;
        }

        return true;
    }

    private void clearUpdateSupplierFields() {
        supplierId.setText("");
        updateSpName.setText("");
        updateSpTelephone.setText("");
        updateSpCpComboBox.getSelectionModel().clearSelection();
        updateSpStatus.getSelectionModel().clearSelection();

        isSupplierLoaded = false;
        loadedSupplier = null;

    }

    private void openPopUp() {
        try {
            PopUp.showPopupAndWait(
                    "popUpSupplierList.fxml",
                    supplierId,
                    this.root.getScene(),
                    PopUp.PopupType.CENTERED_80_WIDTH,
                    (PopUpSupplierListController controller) -> {
                        controller.saveCallingController(this);
                    }
            );
//            FXMLLoader loader = new FXMLLoader(App.class.getResource("popUpSupplierList.fxml"));
//            Parent root = loader.load();
//
//            // Create a new stage for the popup
//            Stage popupStage = new Stage();
//            popupStage.initOwner(this.root.getScene().getWindow());
//            popupStage.initModality(Modality.APPLICATION_MODAL);
//
//            // Get screen dimensions
//            Screen screen = Screen.getPrimary();
//            Rectangle2D bounds = screen.getVisualBounds();
//
//            // Create scene with full width but original height
//            Scene scene = new Scene(root);
//            popupStage.setScene(scene);
//
//            // Set width to screen width and position at x=0
//            popupStage.setWidth(bounds.getWidth());
//            popupStage.setX(0); // This ensures no left gap
//
//            // Set fixed height (adjust as needed)
//            popupStage.setHeight(600);
//
//            // Center the popup vertically
//            popupStage.setY((bounds.getHeight() - popupStage.getHeight()) / 2);
//
//            popupStage.initStyle(StageStyle.TRANSPARENT);
//
//            // Get controller reference
//            PopUpSupplierListController controller = loader.getController();
//            controller.saveCallingController(this);
//
//            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    public void setSupplierID(String id) {
        supplierId.setText(id);
    }

    private void loadSupplier() {
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
                    updateSpTelephone.setText(supplier.getTelephone());

                } else {
                    CustomAlert.showStyledAlert(root, "Supplier not found.", Alert.AlertType.WARNING);
                }
            } catch (Exception e) {
                e.printStackTrace();
                getLogger.logger().warning(e.toString());
            }
        });
    }

}
