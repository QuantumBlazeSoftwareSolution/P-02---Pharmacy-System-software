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
@Table(name = "supplier")
@NamedQueries({
    @NamedQuery(name = "Supplier.findAll", query = "SELECT s FROM Supplier s"),
    @NamedQuery(name = "Supplier.findById", query = "SELECT s FROM Supplier s WHERE s.id = :id"),
    @NamedQuery(name = "Supplier.findByName", query = "SELECT s FROM Supplier s WHERE s.name = :name"),
    @NamedQuery(name = "Supplier.findByTelephone", query = "SELECT s FROM Supplier s WHERE s.telephone = :telephone")})
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "telephone")
    private String telephone;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Company companyId;
    @JoinColumn(name = "supplier_status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SupplierStatus supplierStatusId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplierId")
    private Collection<SupplierDamageReturn> supplierDamageReturnCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplierId")
    private Collection<SupplierOrder> supplierOrderCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplierId")
    private Collection<Grn> grnCollection;

    public Supplier() {
    }

    public Supplier(Integer id) {
        this.id = id;
    }

    public Supplier(Integer id, String name, String telephone) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Company getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Company companyId) {
        this.companyId = companyId;
    }

    public SupplierStatus getSupplierStatusId() {
        return supplierStatusId;
    }

    public void setSupplierStatusId(SupplierStatus supplierStatusId) {
        this.supplierStatusId = supplierStatusId;
    }

    public Collection<SupplierDamageReturn> getSupplierDamageReturnCollection() {
        return supplierDamageReturnCollection;
    }

    public void setSupplierDamageReturnCollection(Collection<SupplierDamageReturn> supplierDamageReturnCollection) {
        this.supplierDamageReturnCollection = supplierDamageReturnCollection;
    }

    public Collection<SupplierOrder> getSupplierOrderCollection() {
        return supplierOrderCollection;
    }

    public void setSupplierOrderCollection(Collection<SupplierOrder> supplierOrderCollection) {
        this.supplierOrderCollection = supplierOrderCollection;
    }

    public Collection<Grn> getGrnCollection() {
        return grnCollection;
    }

    public void setGrnCollection(Collection<Grn> grnCollection) {
        this.grnCollection = grnCollection;
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
        if (!(object instanceof Supplier)) {
            return false;
        }
        Supplier other = (Supplier) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.Supplier[ id=" + id + " ]";
    }
    
}
