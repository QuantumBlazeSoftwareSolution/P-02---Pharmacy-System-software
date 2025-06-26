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
@Table(name = "supplier_damage_return")
@NamedQueries({
    @NamedQuery(name = "SupplierDamageReturn.findAll", query = "SELECT s FROM SupplierDamageReturn s"),
    @NamedQuery(name = "SupplierDamageReturn.findById", query = "SELECT s FROM SupplierDamageReturn s WHERE s.id = :id"),
    @NamedQuery(name = "SupplierDamageReturn.findByDate", query = "SELECT s FROM SupplierDamageReturn s WHERE s.date = :date")})
public class SupplierDamageReturn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Basic(optional = false)
    @Lob
    @Column(name = "reason")
    private String reason;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplierDamageReturnId")
    private Collection<SupplierDamageReturnItem> supplierDamageReturnItemCollection;
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Supplier supplierId;
    @JoinColumn(name = "supply_damage_return_status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SupplyDamageReturnStatus supplyDamageReturnStatusId;

    public SupplierDamageReturn() {
    }

    public SupplierDamageReturn(Integer id) {
        this.id = id;
    }

    public SupplierDamageReturn(Integer id, Date date, String reason) {
        this.id = id;
        this.date = date;
        this.reason = reason;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Collection<SupplierDamageReturnItem> getSupplierDamageReturnItemCollection() {
        return supplierDamageReturnItemCollection;
    }

    public void setSupplierDamageReturnItemCollection(Collection<SupplierDamageReturnItem> supplierDamageReturnItemCollection) {
        this.supplierDamageReturnItemCollection = supplierDamageReturnItemCollection;
    }

    public Supplier getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Supplier supplierId) {
        this.supplierId = supplierId;
    }

    public SupplyDamageReturnStatus getSupplyDamageReturnStatusId() {
        return supplyDamageReturnStatusId;
    }

    public void setSupplyDamageReturnStatusId(SupplyDamageReturnStatus supplyDamageReturnStatusId) {
        this.supplyDamageReturnStatusId = supplyDamageReturnStatusId;
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
        if (!(object instanceof SupplierDamageReturn)) {
            return false;
        }
        SupplierDamageReturn other = (SupplierDamageReturn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.SupplierDamageReturn[ id=" + id + " ]";
    }
    
}
