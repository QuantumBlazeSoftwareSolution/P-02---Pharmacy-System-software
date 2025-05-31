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
@Table(name = "refund_item")
@NamedQueries({
    @NamedQuery(name = "RefundItem.findAll", query = "SELECT r FROM RefundItem r"),
    @NamedQuery(name = "RefundItem.findById", query = "SELECT r FROM RefundItem r WHERE r.id = :id"),
    @NamedQuery(name = "RefundItem.findByQty", query = "SELECT r FROM RefundItem r WHERE r.qty = :qty")})
public class RefundItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "qty")
    private double qty;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productId;
    @JoinColumn(name = "refund_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Refund refundId;

    public RefundItem() {
    }

    public RefundItem(Integer id) {
        this.id = id;
    }

    public RefundItem(Integer id, double qty) {
        this.id = id;
        this.qty = qty;
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

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Refund getRefundId() {
        return refundId;
    }

    public void setRefundId(Refund refundId) {
        this.refundId = refundId;
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
        if (!(object instanceof RefundItem)) {
            return false;
        }
        RefundItem other = (RefundItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.RefundItem[ id=" + id + " ]";
    }
    
}
