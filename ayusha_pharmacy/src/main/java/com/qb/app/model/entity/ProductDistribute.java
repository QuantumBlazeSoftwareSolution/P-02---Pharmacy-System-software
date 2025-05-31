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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "product_distribute")
@NamedQueries({
    @NamedQuery(name = "ProductDistribute.findAll", query = "SELECT p FROM ProductDistribute p"),
    @NamedQuery(name = "ProductDistribute.findById", query = "SELECT p FROM ProductDistribute p WHERE p.id = :id"),
    @NamedQuery(name = "ProductDistribute.findByDateTime", query = "SELECT p FROM ProductDistribute p WHERE p.dateTime = :dateTime"),
    @NamedQuery(name = "ProductDistribute.findByReceiver", query = "SELECT p FROM ProductDistribute p WHERE p.receiver = :receiver")})
public class ProductDistribute implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "date_time")
    private String dateTime;
    @Basic(optional = false)
    @Column(name = "receiver")
    private String receiver;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productDistributeId")
    private Collection<ProductDistributeItem> productDistributeItemCollection;
    @JoinColumn(name = "distribute_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DistributeType distributeTypeId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employee employeeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productDistributeId")
    private Collection<ProductDistributeHasLocation> productDistributeHasLocationCollection;

    public ProductDistribute() {
    }

    public ProductDistribute(Integer id) {
        this.id = id;
    }

    public ProductDistribute(Integer id, String dateTime, String receiver) {
        this.id = id;
        this.dateTime = dateTime;
        this.receiver = receiver;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Collection<ProductDistributeItem> getProductDistributeItemCollection() {
        return productDistributeItemCollection;
    }

    public void setProductDistributeItemCollection(Collection<ProductDistributeItem> productDistributeItemCollection) {
        this.productDistributeItemCollection = productDistributeItemCollection;
    }

    public DistributeType getDistributeTypeId() {
        return distributeTypeId;
    }

    public void setDistributeTypeId(DistributeType distributeTypeId) {
        this.distributeTypeId = distributeTypeId;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
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
        if (!(object instanceof ProductDistribute)) {
            return false;
        }
        ProductDistribute other = (ProductDistribute) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.ProductDistribute[ id=" + id + " ]";
    }
    
}
