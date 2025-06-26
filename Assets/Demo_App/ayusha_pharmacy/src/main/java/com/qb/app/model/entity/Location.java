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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Vihanga
 */
@Entity
@Table(name = "location")
@NamedQueries({
    @NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l"),
    @NamedQuery(name = "Location.findById", query = "SELECT l FROM Location l WHERE l.id = :id"),
    @NamedQuery(name = "Location.findByAddress", query = "SELECT l FROM Location l WHERE l.address = :address"),
    @NamedQuery(name = "Location.findByTelephone1", query = "SELECT l FROM Location l WHERE l.telephone1 = :telephone1"),
    @NamedQuery(name = "Location.findByTelephone2", query = "SELECT l FROM Location l WHERE l.telephone2 = :telephone2")})
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @Column(name = "telephone_1")
    private String telephone1;
    @Basic(optional = false)
    @Column(name = "telephone_2")
    private String telephone2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "locationId")
    private Collection<LocationReturn> locationReturnCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "locationId")
    private Collection<ProductDistributeHasLocation> productDistributeHasLocationCollection;

    public Location() {
    }

    public Location(Integer id) {
        this.id = id;
    }

    public Location(Integer id, String address, String telephone1, String telephone2) {
        this.id = id;
        this.address = address;
        this.telephone1 = telephone1;
        this.telephone2 = telephone2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getTelephone2() {
        return telephone2;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public Collection<LocationReturn> getLocationReturnCollection() {
        return locationReturnCollection;
    }

    public void setLocationReturnCollection(Collection<LocationReturn> locationReturnCollection) {
        this.locationReturnCollection = locationReturnCollection;
    }

    public Collection<ProductDistributeHasLocation> getProductDistributeHasLocationCollection() {
        return productDistributeHasLocationCollection;
    }

    public void setProductDistributeHasLocationCollection(Collection<ProductDistributeHasLocation> productDistributeHasLocationCollection) {
        this.productDistributeHasLocationCollection = productDistributeHasLocationCollection;
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
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.Location[ id=" + id + " ]";
    }
    
}
