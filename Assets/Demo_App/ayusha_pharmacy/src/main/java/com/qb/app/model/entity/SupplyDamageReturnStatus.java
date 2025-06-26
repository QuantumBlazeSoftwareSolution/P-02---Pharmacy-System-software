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
@Table(name = "supply_damage_return_status")
@NamedQueries({
    @NamedQuery(name = "SupplyDamageReturnStatus.findAll", query = "SELECT s FROM SupplyDamageReturnStatus s"),
    @NamedQuery(name = "SupplyDamageReturnStatus.findById", query = "SELECT s FROM SupplyDamageReturnStatus s WHERE s.id = :id"),
    @NamedQuery(name = "SupplyDamageReturnStatus.findByStatus", query = "SELECT s FROM SupplyDamageReturnStatus s WHERE s.status = :status")})
public class SupplyDamageReturnStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplyDamageReturnStatusId")
    private Collection<SupplierDamageReturn> supplierDamageReturnCollection;

    public SupplyDamageReturnStatus() {
    }

    public SupplyDamageReturnStatus(Integer id) {
        this.id = id;
    }

    public SupplyDamageReturnStatus(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Collection<SupplierDamageReturn> getSupplierDamageReturnCollection() {
        return supplierDamageReturnCollection;
    }

    public void setSupplierDamageReturnCollection(Collection<SupplierDamageReturn> supplierDamageReturnCollection) {
        this.supplierDamageReturnCollection = supplierDamageReturnCollection;
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
        if (!(object instanceof SupplyDamageReturnStatus)) {
            return false;
        }
        SupplyDamageReturnStatus other = (SupplyDamageReturnStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.SupplyDamageReturnStatus[ id=" + id + " ]";
    }
    
}
