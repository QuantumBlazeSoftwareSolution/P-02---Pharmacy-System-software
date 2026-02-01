/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.controllers.beans;

/**
 *
 * @author Vihanga
 */
public class PaymentResult {

    public final double cashSettled;
    public final double cardSettled;
    public final double change;
    public final boolean success;

    public PaymentResult(double cashSettled, double cardSettled, double change, boolean success) {
        this.cashSettled = cashSettled;
        this.cardSettled = cardSettled;
        this.change = change;
        this.success=success;
    }   
}
