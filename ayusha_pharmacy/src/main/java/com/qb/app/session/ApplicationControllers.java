/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.session;

import com.qb.app.controllers.PanelCashierController;

/**
 *
 * @author Vihanga
 */
public class ApplicationControllers {
    private static PanelCashierController panelCashierController;

    public static PanelCashierController getPanelCashierController() {
        return panelCashierController;
    }

    public static void setPanelCashierController(PanelCashierController controller) {
        panelCashierController = controller;
    }
}
