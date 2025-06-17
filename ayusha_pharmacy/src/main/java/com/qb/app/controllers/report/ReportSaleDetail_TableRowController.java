
package com.qb.app.controllers.report;

import com.qb.app.model.entity.Invoice;
import com.qb.app.model.entity.InvoiceItem;
import com.qb.app.model.entity.Product;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author ravis
 */
public class ReportSaleDetail_TableRowController implements Initializable {

    @FXML
    private Label Discount;

    public InvoiceItem getInvoiceItem() {
        return invoiceItem;
    }

    public void setInvoiceItem(InvoiceItem invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @FXML
    private Label ID;
    @FXML
    private Label InvoiceNumber;
    @FXML
    private Label ProductName;
    @FXML
    private Label UnitPrice;
    @FXML
    private Label Qty;
    @FXML
    private Label Amount;

    /**
     * Initializes the controller class.
     */
    
    private Product product;
    private Invoice invoice;
    private InvoiceItem invoiceItem;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
      public void setData(Invoice invoice,Product product,InvoiceItem invoiceItem) {
          setInvoice(invoice);
          setProduct(product);
          setInvoiceItem(invoiceItem);
          
          ID.setText(String.valueOf(product.getId()));
          InvoiceNumber.setText(String.valueOf(invoice.getId()));
          ProductName.setText(product.getProduct());
          UnitPrice.setText(String.format("Rs. %,.2f", invoiceItem.getSalePrice()));
          Qty.setText(String.valueOf(invoiceItem.getQty()));
          Amount.setText(String.format("Rs. %,.2f", invoiceItem.getSalePrice()*invoiceItem.getQty()));
          Discount.setText(String.format("Rs. %,.2f", invoiceItem.getDiscount()));
          
          
          
      
      }
}
