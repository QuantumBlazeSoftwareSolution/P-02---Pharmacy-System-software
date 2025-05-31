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
import jakarta.persistence.Lob;
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
@Table(name = "stock_adjustment")
@NamedQueries({
    @NamedQuery(name = "StockAdjustment.findAll", query = "SELECT s FROM StockAdjustment s"),
    @NamedQuery(name = "StockAdjustment.findById", query = "SELECT s FROM StockAdjustment s WHERE s.id = :id"),
    @NamedQuery(name = "StockAdjustment.findByDateTime", query = "SELECT s FROM StockAdjustment s WHERE s.dateTime = :dateTime"),
    @NamedQuery(name = "StockAdjustment.findByLocation", query = "SELECT s FROM StockAdjustment s WHERE s.location = :location")})
public class StockAdjustment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    @Basic(optional = false)
    @Column(name = "location")
    private String location;
    @Basic(optional = false)
    @Lob
    @Column(name = "reason")
    private String reason;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stockAdjustmentId")
    private Collection<StockAdjustmentItem> stockAdjustmentItemCollection;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employee employeeId;

    public StockAdjustment() {
    }

    public StockAdjustment(Integer id) {
        this.id = id;
    }

    public StockAdjustment(Integer id, Date dateTime, String location, String reason) {
        this.id = id;
        this.dateTime = dateTime;
        this.location = location;
        this.reason = reason;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Collection<StockAdjustmentItem> getStockAdjustmentItemCollection() {
        return stockAdjustmentItemCollection;
    }

    public void setStockAdjustmentItemCollection(Collection<StockAdjustmentItem> stockAdjustmentItemCollection) {
        this.stockAdjustmentItemCollection = stockAdjustmentItemCollection;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
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
        if (!(object instanceof StockAdjustment)) {
            return false;
        }
        StockAdjustment other = (StockAdjustment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.StockAdjustment[ id=" + id + " ]";
    }
    
}
