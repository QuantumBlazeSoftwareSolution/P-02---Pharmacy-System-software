/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.session;

import com.qb.app.model.entity.Employee;
import com.qb.app.model.entity.Session;

/**
 *
 * @author Vihanga
 */
public class ApplicationSession {

    private static Employee employee;
    private static Session session;

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
}
