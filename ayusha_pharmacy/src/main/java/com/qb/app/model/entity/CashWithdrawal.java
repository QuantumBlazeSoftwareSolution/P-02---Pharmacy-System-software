/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.model.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@Table(name = "cash_withdrawal")
@NamedQueries({
    @NamedQuery(name = "CashWithdrawal.findAll", query = "SELECT c FROM CashWithdrawal c"),
    @NamedQuery(name = "CashWithdrawal.findById", query = "SELECT c FROM CashWithdrawal c WHERE c.id = :id"),
    @NamedQuery(name = "CashWithdrawal.findByAmount", query = "SELECT c FROM CashWithdrawal c WHERE c.amount = :amount"),
    @NamedQuery(name = "CashWithdrawal.findByDateTime", query = "SELECT c FROM CashWithdrawal c WHERE c.dateTime = :dateTime")})
public class CashWithdrawal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "amount")
    private double amount;
    @Basic(optional = false)
    @Lob
    @Column(name = "reason")
    private String reason;
    @Basic(optional = false)
    @Column(name = "date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Session sessionId;

    public CashWithdrawal() {
    }

    public CashWithdrawal(Integer id) {
        this.id = id;
    }

    public CashWithdrawal(Integer id, double amount, String reason, Date dateTime) {
        this.id = id;
        this.amount = amount;
        this.reason = reason;
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Session getSessionId() {
        return sessionId;
    }

    public void setSessionId(Session sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CashWithdrawal)) {
            return false;
        }
        CashWithdrawal other = (CashWithdrawal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.CashWithdrawal[ id=" + id + " ]";
    }
    
}
