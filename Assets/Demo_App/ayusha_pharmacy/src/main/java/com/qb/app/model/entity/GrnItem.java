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
@Table(name = "grn_item")
@NamedQueries({
    @NamedQuery(name = "GrnItem.findAll", query = "SELECT g FROM GrnItem g"),
    @NamedQuery(name = "GrnItem.findById", query = "SELECT g FROM GrnItem g WHERE g.id = :id"),
    @NamedQuery(name = "GrnItem.findByCostPrice", query = "SELECT g FROM GrnItem g WHERE g.costPrice = :costPrice"),
    @NamedQuery(name = "GrnItem.findByQty", query = "SELECT g FROM GrnItem g WHERE g.qty = :qty")})
public class GrnItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "cost_price")
    private double costPrice;
    @Basic(optional = false)
    @Column(name = "qty")
    private double qty;
    @JoinColumn(name = "grn_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Grn grnId;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productId;

    public GrnItem() {
    }

    public GrnItem(Integer id) {
        this.id = id;
    }

    public GrnItem(Integer id, double costPrice, double qty) {
        this.id = id;
        this.costPrice = costPrice;
        this.qty = qty;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public Grn getGrnId() {
        return grnId;
    }

    public void setGrnId(Grn grnId) {
        this.grnId = grnId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
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
        if (!(object instanceof GrnItem)) {
            return false;
        }
        GrnItem other = (GrnItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.GrnItem[ id=" + id + " ]";
    }
    
}
