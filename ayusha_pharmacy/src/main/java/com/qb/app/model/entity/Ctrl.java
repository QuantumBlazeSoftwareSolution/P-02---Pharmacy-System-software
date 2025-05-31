/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.model.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Vihanga
 */
@Entity
@Table(name = "ctrl")
@NamedQueries({
    @NamedQuery(name = "Ctrl.findAll", query = "SELECT c FROM Ctrl c"),
    @NamedQuery(name = "Ctrl.findByPrimaryKey", query = "SELECT c FROM Ctrl c WHERE c.primaryKey = :primaryKey"),
    @NamedQuery(name = "Ctrl.findByInventorySystem", query = "SELECT c FROM Ctrl c WHERE c.inventorySystem = :inventorySystem"),
    @NamedQuery(name = "Ctrl.findByEmployeeManagement", query = "SELECT c FROM Ctrl c WHERE c.employeeManagement = :employeeManagement"),
    @NamedQuery(name = "Ctrl.findByDiscount", query = "SELECT c FROM Ctrl c WHERE c.discount = :discount"),
    @NamedQuery(name = "Ctrl.findByRefund", query = "SELECT c FROM Ctrl c WHERE c.refund = :refund"),
    @NamedQuery(name = "Ctrl.findByCreditPayment", query = "SELECT c FROM Ctrl c WHERE c.creditPayment = :creditPayment"),
    @NamedQuery(name = "Ctrl.findByWithdrawal", query = "SELECT c FROM Ctrl c WHERE c.withdrawal = :withdrawal"),
    @NamedQuery(name = "Ctrl.findByStockAdjCount", query = "SELECT c FROM Ctrl c WHERE c.stockAdjCount = :stockAdjCount"),
    @NamedQuery(name = "Ctrl.findByTemporaryChance", query = "SELECT c FROM Ctrl c WHERE c.temporaryChance = :temporaryChance"),
    @NamedQuery(name = "Ctrl.findByTemporaryChanceUpdatedDate", query = "SELECT c FROM Ctrl c WHERE c.temporaryChanceUpdatedDate = :temporaryChanceUpdatedDate")})
public class Ctrl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "primary_key")
    private String primaryKey;
    @Basic(optional = false)
    @Column(name = "inventory_system")
    private String inventorySystem;
    @Basic(optional = false)
    @Column(name = "employee_management")
    private String employeeManagement;
    @Basic(optional = false)
    @Column(name = "discount")
    private String discount;
    @Basic(optional = false)
    @Column(name = "refund")
    private String refund;
    @Basic(optional = false)
    @Column(name = "credit_payment")
    private String creditPayment;
    @Basic(optional = false)
    @Column(name = "withdrawal")
    private String withdrawal;
    @Basic(optional = false)
    @Column(name = "stock_adj_count")
    private int stockAdjCount;
    @Basic(optional = false)
    @Column(name = "temporary_chance")
    private int temporaryChance;
    @Basic(optional = false)
    @Column(name = "temporary_chance_updated_date")
    @Temporal(TemporalType.DATE)
    private Date temporaryChanceUpdatedDate;

    public Ctrl() {
    }

    public Ctrl(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Ctrl(String primaryKey, String inventorySystem, String employeeManagement, String discount, String refund, String creditPayment, String withdrawal, int stockAdjCount, int temporaryChance, Date temporaryChanceUpdatedDate) {
        this.primaryKey = primaryKey;
        this.inventorySystem = inventorySystem;
        this.employeeManagement = employeeManagement;
        this.discount = discount;
        this.refund = refund;
        this.creditPayment = creditPayment;
        this.withdrawal = withdrawal;
        this.stockAdjCount = stockAdjCount;
        this.temporaryChance = temporaryChance;
        this.temporaryChanceUpdatedDate = temporaryChanceUpdatedDate;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getInventorySystem() {
        return inventorySystem;
    }

    public void setInventorySystem(String inventorySystem) {
        this.inventorySystem = inventorySystem;
    }

    public String getEmployeeManagement() {
        return employeeManagement;
    }

    public void setEmployeeManagement(String employeeManagement) {
        this.employeeManagement = employeeManagement;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public String getCreditPayment() {
        return creditPayment;
    }

    public void setCreditPayment(String creditPayment) {
        this.creditPayment = creditPayment;
    }

    public String getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(String withdrawal) {
        this.withdrawal = withdrawal;
    }

    public int getStockAdjCount() {
        return stockAdjCount;
    }

    public void setStockAdjCount(int stockAdjCount) {
        this.stockAdjCount = stockAdjCount;
    }

    public int getTemporaryChance() {
        return temporaryChance;
    }

    public void setTemporaryChance(int temporaryChance) {
        this.temporaryChance = temporaryChance;
    }

    public Date getTemporaryChanceUpdatedDate() {
        return temporaryChanceUpdatedDate;
    }

    public void setTemporaryChanceUpdatedDate(Date temporaryChanceUpdatedDate) {
        this.temporaryChanceUpdatedDate = temporaryChanceUpdatedDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (primaryKey != null ? primaryKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ctrl)) {
            return false;
        }
        Ctrl other = (Ctrl) object;
        if ((this.primaryKey == null && other.primaryKey != null) || (this.primaryKey != null && !this.primaryKey.equals(other.primaryKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.Ctrl[ primaryKey=" + primaryKey + " ]";
    }
    
}
