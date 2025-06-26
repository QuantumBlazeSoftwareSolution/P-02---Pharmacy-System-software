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
@Table(name = "product_distribute_has_location")
@NamedQueries({
    @NamedQuery(name = "ProductDistributeHasLocation.findAll", query = "SELECT p FROM ProductDistributeHasLocation p"),
    @NamedQuery(name = "ProductDistributeHasLocation.findById", query = "SELECT p FROM ProductDistributeHasLocation p WHERE p.id = :id")})
public class ProductDistributeHasLocation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Location locationId;
    @JoinColumn(name = "product_distribute_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProductDistribute productDistributeId;

    public ProductDistributeHasLocation() {
    }

    public ProductDistributeHasLocation(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Location getLocationId() {
        return locationId;
    }

    public void setLocationId(Location locationId) {
        this.locationId = locationId;
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
        if (!(object instanceof ProductDistributeHasLocation)) {
            return false;
        }
        ProductDistributeHasLocation other = (ProductDistributeHasLocation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.ProductDistributeHasLocation[ id=" + id + " ]";
    }
    
}
