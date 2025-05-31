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
@Table(name = "damage_item")
@NamedQueries({
    @NamedQuery(name = "DamageItem.findAll", query = "SELECT d FROM DamageItem d"),
    @NamedQuery(name = "DamageItem.findById", query = "SELECT d FROM DamageItem d WHERE d.id = :id"),
    @NamedQuery(name = "DamageItem.findByQty", query = "SELECT d FROM DamageItem d WHERE d.qty = :qty")})
public class DamageItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "qty")
    private double qty;
    @JoinColumn(name = "damage_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Damage damageId;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productId;

    public DamageItem() {
    }

    public DamageItem(Integer id) {
        this.id = id;
    }

    public DamageItem(Integer id, double qty) {
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

    public Damage getDamageId() {
        return damageId;
    }

    public void setDamageId(Damage damageId) {
        this.damageId = damageId;
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
        if (!(object instanceof DamageItem)) {
            return false;
        }
        DamageItem other = (DamageItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.DamageItem[ id=" + id + " ]";
    }
    
}
