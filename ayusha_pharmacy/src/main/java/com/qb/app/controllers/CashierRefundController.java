package com.qb.app.controllers;

import com.qb.app.model.ControllerClose;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class CashierRefundController implements Initializable, ControllerClose {

    @FXML
    private VBox refundItemContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void close() {
    }

}
