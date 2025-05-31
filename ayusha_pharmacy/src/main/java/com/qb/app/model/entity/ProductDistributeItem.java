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
@Table(name = "product_distribute_item")
@NamedQueries({
    @NamedQuery(name = "ProductDistributeItem.findAll", query = "SELECT p FROM ProductDistributeItem p"),
    @NamedQuery(name = "ProductDistributeItem.findById", query = "SELECT p FROM ProductDistributeItem p WHERE p.id = :id"),
    @NamedQuery(name = "ProductDistributeItem.findByQty", query = "SELECT p FROM ProductDistributeItem p WHERE p.qty = :qty"),
    @NamedQuery(name = "ProductDistributeItem.findByProductPrice", query = "SELECT p FROM ProductDistributeItem p WHERE p.productPrice = :productPrice")})
public class ProductDistributeItem implements Serializable {

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
    @Column(name = "product_price")
    private double productPrice;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productId;
    @JoinColumn(name = "product_distribute_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProductDistribute productDistributeId;

    public ProductDistributeItem() {
    }

    public ProductDistributeItem(Integer id) {
        this.id = id;
    }

    public ProductDistributeItem(Integer id, double qty, double productPrice) {
        this.id = id;
        this.qty = qty;
        this.productPrice = productPrice;
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

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public ProductDistribute getProductDistributeId() {
        return productDistributeId;
    }

    public void setProductDistributeId(ProductDistribute productDistributeId) {
        this.productDistributeId = productDistributeId;
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
        if (!(object instanceof ProductDistributeItem)) {
            return false;
        }
        ProductDistributeItem other = (ProductDistributeItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.ProductDistributeItem[ id=" + id + " ]";
    }
    
}
