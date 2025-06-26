package com.qb.app.session;

import com.qb.app.controllers.PanelCashierController;

public class ApplicationControllers {
    private static PanelCashierController panelCashierController;

    public static PanelCashierController getPanelCashierController() {
        return panelCashierController;
    }

    public static void setPanelCashierController(PanelCashierController controller) {
        panelCashierController = controller;
    }
}
