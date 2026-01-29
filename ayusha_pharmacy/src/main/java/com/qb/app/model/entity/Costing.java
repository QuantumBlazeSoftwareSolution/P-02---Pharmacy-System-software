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
@Table(name = "costing")
@NamedQueries({
    @NamedQuery(name = "Costing.findAll", query = "SELECT c FROM Costing c"),
    @NamedQuery(name = "Costing.findById", query = "SELECT c FROM Costing c WHERE c.id = :id")})
public class Costing implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "child_product", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product childProduct;
    @JoinColumn(name = "parent_product", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product parentProduct;

    public Costing() {
    }

    public Costing(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getChildProduct() {
        return childProduct;
    }

    public void setChildProduct(Product childProduct) {
        this.childProduct = childProduct;
    }

    public Product getParentProduct() {
        return parentProduct;
    }

    public void setParentProduct(Product parentProduct) {
        this.parentProduct = parentProduct;
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
        if (!(object instanceof Costing)) {
            return false;
        }
        Costing other = (Costing) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.Costing[ id=" + id + " ]";
    }
    
}
