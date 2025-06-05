package com.qb.app.controllers;

import com.qb.app.controllers.report.beans.InvoiceItemBean;
import com.qb.app.model.ControllerClose;
import com.qb.app.model.CustomAlert;
import com.qb.app.model.DefaultAPI;
import com.qb.app.model.JPATransaction;
import com.qb.app.model.PasswordEncryption;
import com.qb.app.model.entity.CloseSale;
import com.qb.app.model.entity.Invoice;
import com.qb.app.model.entity.InvoiceItem;
import com.qb.app.model.entity.Session;
import com.qb.app.session.ApplicationControllers;
import com.qb.app.session.ApplicationSession;
import com.qb.app.session.CompanyInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class CashierCloseSaleController implements Initializable, ControllerClose {

    // <editor-fold desc="FXML init component" defaultstate="collapsed">
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TextField tfCashier;
    @FXML
    private TextField tfDayIn;
    @FXML
    private TextField tfDayOff;
    @FXML
    private TextField tf5000Qty;
    @FXML
    private TextField tf1000Qty;
    @FXML
    private TextField tf1000Value;
    @FXML
    private TextField tf500Qty;
    @FXML
    private TextField tf500Value;
    @FXML
    private TextField tf100Qty;
    @FXML
    private TextField tf100Value;
    @FXML
    private TextField tf50Qty;
    @FXML
    private TextField tf50Value;
    @FXML
    private TextField tf20Qty;
    @FXML
    private TextField tf20Value;
    @FXML
    private TextField tf10Qty;
    @FXML
    private TextField tf10Value;
    @FXML
    private TextField tf5Qty;
    @FXML
    private TextField tf5Value;
    @FXML
    private TextField tfCollection;
    @FXML
    private Button btnAction;
    @FXML
    private TextField tf5000Value;
    @FXML
    private AnchorPane root;
    // </editor-fold>
    private CloseSale closeSale;
    private double physicalBalance;
    private double systemBalance;
    private Collection<Invoice> invoiceCollection;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializingTextFields();
    }

    private void initializingTextFields() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        tf5000Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf1000Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf500Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf100Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf50Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf20Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf10Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        tf5Qty.setTextFormatter(DefaultAPI.createNumericTextFormatter());
        if (ApplicationSession.getSession() != null) {
            tfCashier.setText(ApplicationSession.getSession().getEmployeeId().getName());
            tfDayIn.setText(dateFormat.format(ApplicationSession.getSession().getDayInTime()));
            tfDayOff.setText(dateFormat.format(ApplicationSession.getSession().getDayOutTime()));
        }
    }

    @FXML
    private void handleKeyEvent(KeyEvent event) {
        if (event.getSource() == tf5000Qty) {
            setCashValue(tf5000Qty, tf5000Value, 5000);
        }

        if (event.getSource() == tf1000Qty) {
            setCashValue(tf1000Qty, tf1000Value, 1000);
        }

        if (event.getSource() == tf500Qty) {
            setCashValue(tf500Qty, tf500Value, 500);
        }

        if (event.getSource() == tf100Qty) {
            setCashValue(tf100Qty, tf100Value, 100);
        }

        if (event.getSource() == tf50Qty) {
            setCashValue(tf50Qty, tf50Value, 50);
        }

        if (event.getSource() == tf20Qty) {
            setCashValue(tf20Qty, tf20Value, 20);
        }

        if (event.getSource() == tf10Qty) {
            setCashValue(tf10Qty, tf10Value, 10);
        }

        if (event.getSource() == tf5Qty) {
            setCashValue(tf5Qty, tf5Value, 5);
        }

        double totalCollection = calculateTotalCollection();
        tfCollection.setText(String.format("Rs. %, .2f", totalCollection));
    }

    private void setCashValue(TextField getter, TextField setter, int value) {
        if (!getter.getText().isEmpty()) {
            int cash = Integer.parseInt(getter.getText());
            setter.setText(String.format("Rs. %,d.00", (cash * value)));
        }
    }

    private double calculateTotalCollection() {
        System.out.println("calculateTotalCollection() method was triggered");
        double total = 0;

        // Add each denomination's total value
        total += getDenominationTotal(tf5000Qty, 5000);
        total += getDenominationTotal(tf1000Qty, 1000);
        total += getDenominationTotal(tf500Qty, 500);
        total += getDenominationTotal(tf100Qty, 100);
        total += getDenominationTotal(tf50Qty, 50);
        total += getDenominationTotal(tf20Qty, 20);
        total += getDenominationTotal(tf10Qty, 10);
        total += getDenominationTotal(tf5Qty, 5);
        return total;
    }

    private double getDenominationTotal(TextField quantityField, int denominationValue) {
        System.out.println("getDenominationTotal() method was triggered");
        try {
            if (!quantityField.getText().isEmpty()) {
                int quantity = Integer.parseInt(quantityField.getText());
                return quantity * denominationValue;
            } else {
                System.out.println("It's empty");
            }
        } catch (NumberFormatException e) {
            // Handle invalid number format (optional)
            e.printStackTrace();
        }
        return 0;
    }

    private int parseTextField(TextField textField) {
        try {
            return textField.getText().isEmpty() ? 0 : Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }

    }

    @FXML
    private void ActionEvent(ActionEvent event) {
        if (event.getSource() == btnAction) {
            if (tfUsername.getText().isEmpty()) {
                CustomAlert.showStyledAlert(root, "Username field cannot be empty. Please enter your username.", "Username Required", Alert.AlertType.WARNING);
            } else if (tfPassword.getText().isEmpty()) {
                CustomAlert.showStyledAlert(root, "Password field cannot be empty. Please enter your password.", "Password Required", Alert.AlertType.WARNING);
            } else {
                if (tfUsername.getText().equals(ApplicationSession.getEmployee().getUsername()) && PasswordEncryption.verifyPassword(ApplicationSession.getSession().getEmployeeId().getPassword(), tfPassword.getText())) {
                    JPATransaction.runInTransaction((em) -> {
                        CloseSale closeSale = new CloseSale();
                        closeSale.setC5(parseTextField(tf5Qty));
                        closeSale.setC10(parseTextField(tf10Qty));
                        closeSale.setC20(parseTextField(tf20Qty));
                        closeSale.setC50(parseTextField(tf50Qty));
                        closeSale.setC100(parseTextField(tf100Qty));
                        closeSale.setC500(parseTextField(tf500Qty));
                        closeSale.setC1000(parseTextField(tf1000Qty));
                        closeSale.setC5000(parseTextField(tf5000Qty));
                        closeSale.setDateTime(new Date());
                        closeSale.setSessionId(ApplicationSession.getSession());
                        em.persist(closeSale);

                        this.closeSale = closeSale;

                        Thread printThread = new Thread(() -> {
                            try {
                                PrintReport();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        printThread.start();

                        clearCloseSale();
                        CustomAlert.showStyledAlert(root, "The sale has been successfully closed.", "Sale Closed Successfully", Alert.AlertType.INFORMATION);
                        ApplicationControllers.getPanelCashierController().changePanel(
                                "/com/qb/app/cashierDashboard.fxml",
                                "Dashboard"
                        );
                    });
                } else {
                    CustomAlert.showStyledAlert(root, "The username or password you entered is incorrect. Please try again.", "Invalid Credentials", Alert.AlertType.WARNING);
                }
            }
        }
    }

    private void clearCloseSale() {
        tf5Qty.setText("");
        tf10Qty.setText("");
        tf20Qty.setText("");
        tf50Qty.setText("");
        tf100Qty.setText("");
        tf500Qty.setText("");
        tf1000Qty.setText("");
        tf5000Qty.setText("");
        tf5Value.setText("");
        tf10Value.setText("");
        tf20Value.setText("");
        tf50Value.setText("");
        tf100Value.setText("");
        tf500Value.setText("");
        tf1000Value.setText("");
        tf5000Value.setText("");
        tfUsername.setText("");
        tfPassword.setText("");
        tfCollection.setText("");
        tfDayIn.setText("");
        tfDayOff.setText("");
        tfCashier.setText("");
    }

    @Override
    public void close() {

    }

    private void PrintReport() {
        Map<String, Object> params = getJRParams();
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
                    getClass().getResourceAsStream("/com/qb/app/reports/PharmacyCloseSale.jasper"));

            JREmptyDataSource dataSource = new JREmptyDataSource();

            JasperPrint report = JasperFillManager.fillReport(jasperReport, params, dataSource);
//            JasperPrintManager.printReport(report, false);
            JasperViewer.viewReport(report, false);
        } catch (JRException e) {
            e.printStackTrace();
            CustomAlert.showStyledAlert(root, "Report generation failed: " + e.getMessage(), "Reporting Error", Alert.AlertType.ERROR);
        }
    }

    private Map<String, Object> getJRParams() {
        Map<String, Object> params = new HashMap<>();

        try {
            URL imageUrl = getClass().getResource("/com/qb/app/assets/images/logo.png");
            params.put("Logo", imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.systemBalance = getSystemBalance();
        this.physicalBalance = getPhysicalBalance();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy hh:mm a");
        String dayInTime = ApplicationSession.getSession().getDayInTime().toInstant().atZone(ZoneId.systemDefault()).format(formatter);
        String dayOutTime = ApplicationSession.getSession().getDayOutTime().toInstant().atZone(ZoneId.systemDefault()).format(formatter);

        params.put("CompanyName", CompanyInfo.applicationName);
        params.put("Cashier", ApplicationSession.getSession().getEmployeeId().getName());
        params.put("Shift", dayInTime + " - " + dayOutTime);
        params.put("PettyCash", String.format("Rs. %,.2f", ApplicationSession.getSession().getPettyCash()));
        params.put("Collection", String.format("Rs. %,.2f", ApplicationSession.getSession().getCollection()));
        params.put("Total5000", String.format("Rs. %,.2f", this.closeSale.getC5000() * 5000.0));
        params.put("Total1000", String.format("Rs. %,.2f", this.closeSale.getC1000() * 1000.0));
        params.put("Total500", String.format("Rs. %,.2f", this.closeSale.getC500() * 500.0));
        params.put("Total100", String.format("Rs. %,.2f", this.closeSale.getC100() * 100.0));
        params.put("Total50", String.format("Rs. %,.2f", this.closeSale.getC50() * 50.0));
        params.put("Total20", String.format("Rs. %,.2f", this.closeSale.getC20() * 20.0));
        params.put("Total10", String.format("Rs. %,.2f", this.closeSale.getC10() * 10.0));
        params.put("Total5", String.format("Rs. %,.2f", this.closeSale.getC5() * 5.0));

        params.put("Qty5", String.valueOf(this.closeSale.getC5()));
        params.put("Qty10", String.valueOf(this.closeSale.getC10()));
        params.put("Qty20", String.valueOf(this.closeSale.getC20()));
        params.put("Qty50", String.valueOf(this.closeSale.getC50()));
        params.put("Qty100", String.valueOf(this.closeSale.getC100()));
        params.put("Qty500", String.valueOf(this.closeSale.getC500()));
        params.put("Qty1000", String.valueOf(this.closeSale.getC1000()));
        params.put("Qty5000", String.valueOf(this.closeSale.getC5000()));
        params.put("SystemBalance", String.format("Rs. %,.2f", ApplicationSession.getSession().getPettyCash() + this.systemBalance));
        params.put("PhysicalBalance", String.format("Rs. %,.2f", this.physicalBalance));
        params.put("CashVariance", String.format("Rs. %,.2f", getCashVariance()));
        params.put("TotalDiscount", String.format("Rs. %,d.00", getTotalDiscount()));
        params.put("TotalSale", String.format("Rs. %,.2f", this.systemBalance));
        params.put("InvoiceCount", String.valueOf(getInvoiceCount()));
        params.put("TotalCash", String.format("Rs. %,.2f", this.physicalBalance));
        return params;
    }

    private int getSystemBalance() {
        return JPATransaction.runInTransaction((em) -> {
            Session session = em.find(Session.class, ApplicationSession.getSession().getId());

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Invoice> cq = cb.createQuery(Invoice.class);
            Root<Invoice> invoiceTable = cq.from(Invoice.class);

            Predicate sessionPredicate = cb.equal(invoiceTable.get("sessionId"), session);
            cq.where(sessionPredicate);
            List<Invoice> invoices = em.createQuery(cq).getResultList();

            this.invoiceCollection = invoices;
            return (int) invoiceCollection.stream()
                    .mapToDouble(Invoice::getBillAmount)
                    .sum();
        });
    }

    private int getPhysicalBalance() {
        return JPATransaction.runInTransaction((em) -> {

            double balance = 0;
            balance += this.closeSale.getC5() * 5;
            balance += this.closeSale.getC10() * 10;
            balance += this.closeSale.getC20() * 20;
            balance += this.closeSale.getC50() * 50;
            balance += this.closeSale.getC100() * 100;
            balance += this.closeSale.getC500() * 500;
            balance += this.closeSale.getC1000() * 1000;
            balance += this.closeSale.getC5000() * 5000;
            return (int) balance;
        });
    }

    private double getCashVariance() {
        return this.physicalBalance - this.systemBalance;
    }

    private int getTotalDiscount() {
        return JPATransaction.runInTransaction((em) -> {
            int discount = 0;
            for (Invoice invoice : invoiceCollection) {

                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<InvoiceItem> cq = cb.createQuery(InvoiceItem.class);
                Root<InvoiceItem> invoiceItemTable = cq.from(InvoiceItem.class);

                Predicate sessionPredicate = cb.equal(invoiceItemTable.get("invoiceId"), invoice);
                cq.where(sessionPredicate);
                List<InvoiceItem> invoiceItems = em.createQuery(cq).getResultList();

                Collection<InvoiceItem> invoiceItemCollection = invoiceItems;
                for (InvoiceItem invoiceItem : invoiceItemCollection) {
                    discount += invoiceItem.getDiscount() * invoiceItem.getQty();
                }
            }
            return discount;
        });
    }

    private int getInvoiceCount() {
        return this.invoiceCollection.size();
    }
}
