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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author Vihanga
 */
@Entity
@Table(name = "supplier_damage_return_item")
@NamedQueries({
    @NamedQuery(name = "SupplierDamageReturnItem.findAll", query = "SELECT s FROM SupplierDamageReturnItem s"),
    @NamedQuery(name = "SupplierDamageReturnItem.findById", query = "SELECT s FROM SupplierDamageReturnItem s WHERE s.id = :id"),
    @NamedQuery(name = "SupplierDamageReturnItem.findByQty", query = "SELECT s FROM SupplierDamageReturnItem s WHERE s.qty = :qty"),
    @NamedQuery(name = "SupplierDamageReturnItem.findByReturnPrice", query = "SELECT s FROM SupplierDamageReturnItem s WHERE s.returnPrice = :returnPrice")})
public class SupplierDamageReturnItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "qty")
    private double qty;
    @Basic(optional = false)
    @Column(name = "return_price")
    private double returnPrice;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productId;
    @JoinColumn(name = "supplier_damage_return_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SupplierDamageReturn supplierDamageReturnId;

    public SupplierDamageReturnItem() {
    }

    public SupplierDamageReturnItem(Integer id) {
        this.id = id;
    }

    public SupplierDamageReturnItem(Integer id, double qty, double returnPrice) {
        this.id = id;
        this.qty = qty;
        this.returnPrice = returnPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(double returnPrice) {
        this.returnPrice = returnPrice;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public SupplierDamageReturn getSupplierDamageReturnId() {
        return supplierDamageReturnId;
    }

    public void setSupplierDamageReturnId(SupplierDamageReturn supplierDamageReturnId) {
        this.supplierDamageReturnId = supplierDamageReturnId;
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
        if (!(object instanceof SupplierDamageReturnItem)) {
            return false;
        }
        SupplierDamageReturnItem other = (SupplierDamageReturnItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.SupplierDamageReturnItem[ id=" + id + " ]";
    }
    
}
