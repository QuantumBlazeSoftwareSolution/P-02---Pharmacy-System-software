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
@Table(name = "stock_adjustment_item")
@NamedQueries({
    @NamedQuery(name = "StockAdjustmentItem.findAll", query = "SELECT s FROM StockAdjustmentItem s"),
    @NamedQuery(name = "StockAdjustmentItem.findById", query = "SELECT s FROM StockAdjustmentItem s WHERE s.id = :id"),
    @NamedQuery(name = "StockAdjustmentItem.findByQty", query = "SELECT s FROM StockAdjustmentItem s WHERE s.qty = :qty")})
public class StockAdjustmentItem implements Serializable {

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
    @JoinColumn(name = "stock_adjustment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private StockAdjustment stockAdjustmentId;

    public StockAdjustmentItem() {
    }

    public StockAdjustmentItem(Integer id) {
        this.id = id;
    }

    public StockAdjustmentItem(Integer id, double qty) {
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

    public StockAdjustment getStockAdjustmentId() {
        return stockAdjustmentId;
    }

    public void setStockAdjustmentId(StockAdjustment stockAdjustmentId) {
        this.stockAdjustmentId = stockAdjustmentId;
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
        if (!(object instanceof StockAdjustmentItem)) {
            return false;
        }
        StockAdjustmentItem other = (StockAdjustmentItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.StockAdjustmentItem[ id=" + id + " ]";
    }
    
}
