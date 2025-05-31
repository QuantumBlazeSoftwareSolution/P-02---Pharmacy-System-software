package com.qb.app.controllers;

import com.qb.app.model.ControllerClose;
import com.qb.app.model.DefaultAPI;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class CashierRePrintController implements Initializable, ControllerClose {

    @FXML
    private VBox rePrintInvoiceItemContainer;
    @FXML
    private VBox rePrintItemContainer;
    @FXML
    private Label invoiceNumber;
    @FXML
    private ScrollPane rePrintInvoiceScrollContainer;
    @FXML
    private ScrollBar rePrintInvoiceScroller;
    @FXML
    private ScrollPane rePrintInvoiceItemScrollContainer;
    @FXML
    private ScrollBar rePrintInvoiceItemScroller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int i = 0; i < 20; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/qb/app/fxmlComponent/rePrintInvoiceItem.fxml"));
                Node rePrintInvoiceItem = loader.load();

                rePrintInvoiceItemContainer.getChildren().add(rePrintInvoiceItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        DefaultAPI.bindTableScroll(rePrintInvoiceScroller, rePrintInvoiceScrollContainer, rePrintItemContainer);
        DefaultAPI.bindTableScroll(rePrintInvoiceItemScroller, rePrintInvoiceItemScrollContainer, rePrintInvoiceItemContainer);
    }

    @Override
    public void close() {
    }
}
