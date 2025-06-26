package com.qb.app.session;

import com.qb.app.controllers.CashierSessionController.Employee;
import com.qb.app.model.entity.Session;

public class ApplicationSession {

    private static Employee employee;
    private static Session session;
    private static final String applicationName = "POS System Name";
    private static final String address = "No: Company Address";
    private static final String mobile = "Contact: Company Telephone";

    public static Employee getEmployee() {
        return employee;
    }

    public static void setEmployee(Employee param) {
        employee = param;
    }

    public static Session getSession() {
        return session;
    }

    public static void setSession(Session param) {
        session = param;
    }

    public static String getApplicationName() {
        return applicationName;
    }

    public static String getAddress() {
        return address;
    }

    public static String getMobile() {
        return mobile;
    }
}
