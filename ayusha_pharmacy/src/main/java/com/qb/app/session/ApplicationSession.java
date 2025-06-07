package com.qb.app.session;

import com.qb.app.model.entity.Employee;
import com.qb.app.model.entity.Session;

public class ApplicationSession {

    private static Employee employee;
    private static Session session;
    private static final String applicationName = "Ayusha Pharmacy & Grocery";
    private static final String address = "No: 55 /1/2, welikadamulla, Attanagalla.";
    private static final String mobile = "Contact: 0776085969";

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
