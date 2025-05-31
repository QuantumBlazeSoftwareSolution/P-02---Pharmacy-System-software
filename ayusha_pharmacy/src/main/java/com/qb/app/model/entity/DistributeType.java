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
@Table(name = "distribute_type")
@NamedQueries({
    @NamedQuery(name = "DistributeType.findAll", query = "SELECT d FROM DistributeType d"),
    @NamedQuery(name = "DistributeType.findById", query = "SELECT d FROM DistributeType d WHERE d.id = :id"),
    @NamedQuery(name = "DistributeType.findByType", query = "SELECT d FROM DistributeType d WHERE d.type = :type")})
public class DistributeType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distributeTypeId")
    private Collection<ProductDistribute> productDistributeCollection;

    public DistributeType() {
    }

    public DistributeType(Integer id) {
        this.id = id;
    }

    public DistributeType(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Collection<ProductDistribute> getProductDistributeCollection() {
        return productDistributeCollection;
    }

    public void setProductDistributeCollection(Collection<ProductDistribute> productDistributeCollection) {
        this.productDistributeCollection = productDistributeCollection;
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
        if (!(object instanceof DistributeType)) {
            return false;
        }
        DistributeType other = (DistributeType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.DistributeType[ id=" + id + " ]";
    }
    
}
