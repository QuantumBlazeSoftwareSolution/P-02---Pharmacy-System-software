/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.model.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Vihanga
 */
@Entity
@Table(name = "session")
@NamedQueries({
    @NamedQuery(name = "Session.findAll", query = "SELECT s FROM Session s"),
    @NamedQuery(name = "Session.findById", query = "SELECT s FROM Session s WHERE s.id = :id"),
    @NamedQuery(name = "Session.findByDayInTime", query = "SELECT s FROM Session s WHERE s.dayInTime = :dayInTime"),
    @NamedQuery(name = "Session.findByDayOutTime", query = "SELECT s FROM Session s WHERE s.dayOutTime = :dayOutTime"),
    @NamedQuery(name = "Session.findByPettyCash", query = "SELECT s FROM Session s WHERE s.pettyCash = :pettyCash"),
    @NamedQuery(name = "Session.findByCollection", query = "SELECT s FROM Session s WHERE s.collection = :collection"),
    @NamedQuery(name = "Session.findByStatus", query = "SELECT s FROM Session s WHERE s.status = :status")})
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "day_in_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dayInTime;
    @Column(name = "day_out_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dayOutTime;
    @Basic(optional = false)
    @Column(name = "petty_cash")
    private double pettyCash;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "collection")
    private Double collection;
    @Column(name = "status")
    private String status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sessionId")
    private Collection<CloseSale> closeSaleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sessionId")
    private Collection<Refund> refundCollection;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employee employeeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sessionId")
    private Collection<CashWithdrawal> cashWithdrawalCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sessionId")
    private Collection<Invoice> invoiceCollection;

    public Session() {
    }

    public Session(Integer id) {
        this.id = id;
    }

    public Session(Integer id, Date dayInTime, double pettyCash) {
        this.id = id;
        this.dayInTime = dayInTime;
        this.pettyCash = pettyCash;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDayInTime() {
        return dayInTime;
    }

    public void setDayInTime(Date dayInTime) {
        this.dayInTime = dayInTime;
    }

    public Date getDayOutTime() {
        return dayOutTime;
    }

    public void setDayOutTime(Date dayOutTime) {
        this.dayOutTime = dayOutTime;
    }

    public double getPettyCash() {
        return pettyCash;
    }

    public void setPettyCash(double pettyCash) {
        this.pettyCash = pettyCash;
    }

    public Double getCollection() {
        return collection;
    }

    public void setCollection(Double collection) {
        this.collection = collection;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Collection<CloseSale> getCloseSaleCollection() {
        return closeSaleCollection;
    }

    public void setCloseSaleCollection(Collection<CloseSale> closeSaleCollection) {
        this.closeSaleCollection = closeSaleCollection;
    }

    public Collection<Refund> getRefundCollection() {
        return refundCollection;
    }

    public void setRefundCollection(Collection<Refund> refundCollection) {
        this.refundCollection = refundCollection;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public Collection<CashWithdrawal> getCashWithdrawalCollection() {
        return cashWithdrawalCollection;
    }

    public void setCashWithdrawalCollection(Collection<CashWithdrawal> cashWithdrawalCollection) {
        this.cashWithdrawalCollection = cashWithdrawalCollection;
    }

    public Collection<Invoice> getInvoiceCollection() {
        return invoiceCollection;
    }

    public void setInvoiceCollection(Collection<Invoice> invoiceCollection) {
        this.invoiceCollection = invoiceCollection;
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
        if (!(object instanceof Session)) {
            return false;
        }
        Session other = (Session) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.Session[ id=" + id + " ]";
    }
    
}
