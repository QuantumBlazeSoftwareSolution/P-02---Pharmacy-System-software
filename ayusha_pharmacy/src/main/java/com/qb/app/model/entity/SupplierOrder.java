/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.model.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "supplier_order")
@NamedQueries({
    @NamedQuery(name = "SupplierOrder.findAll", query = "SELECT s FROM SupplierOrder s"),
    @NamedQuery(name = "SupplierOrder.findById", query = "SELECT s FROM SupplierOrder s WHERE s.id = :id"),
    @NamedQuery(name = "SupplierOrder.findByRequiredDate", query = "SELECT s FROM SupplierOrder s WHERE s.requiredDate = :requiredDate")})
public class SupplierOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "required_date")
    @Temporal(TemporalType.DATE)
    private Date requiredDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplierOrderId")
    private Collection<SupplierOrderItem> supplierOrderItemCollection;
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Supplier supplierId;

    public SupplierOrder() {
    }

    public SupplierOrder(Integer id) {
        this.id = id;
    }

    public SupplierOrder(Integer id, Date requiredDate) {
        this.id = id;
        this.requiredDate = requiredDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }

    public Collection<SupplierOrderItem> getSupplierOrderItemCollection() {
        return supplierOrderItemCollection;
    }

    public void setSupplierOrderItemCollection(Collection<SupplierOrderItem> supplierOrderItemCollection) {
        this.supplierOrderItemCollection = supplierOrderItemCollection;
    }

    public Supplier getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Supplier supplierId) {
        this.supplierId = supplierId;
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
        if (!(object instanceof SupplierOrder)) {
            return false;
        }
        SupplierOrder other = (SupplierOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.SupplierOrder[ id=" + id + " ]";
    }
    
}
