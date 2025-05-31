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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Vihanga
 */
@Entity
@Table(name = "grn")
@NamedQueries({
    @NamedQuery(name = "Grn.findAll", query = "SELECT g FROM Grn g"),
    @NamedQuery(name = "Grn.findById", query = "SELECT g FROM Grn g WHERE g.id = :id"),
    @NamedQuery(name = "Grn.findByGrnCode", query = "SELECT g FROM Grn g WHERE g.grnCode = :grnCode"),
    @NamedQuery(name = "Grn.findByDateTime", query = "SELECT g FROM Grn g WHERE g.dateTime = :dateTime")})
public class Grn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "grn_code")
    private String grnCode;
    @Basic(optional = false)
    @Column(name = "date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grnId")
    private Collection<GrnItem> grnItemCollection;
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Supplier supplierId;

    public Grn() {
    }

    public Grn(Integer id) {
        this.id = id;
    }

    public Grn(Integer id, String grnCode, Date dateTime) {
        this.id = id;
        this.grnCode = grnCode;
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGrnCode() {
        return grnCode;
    }

    public void setGrnCode(String grnCode) {
        this.grnCode = grnCode;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Collection<GrnItem> getGrnItemCollection() {
        return grnItemCollection;
    }

    public void setGrnItemCollection(Collection<GrnItem> grnItemCollection) {
        this.grnItemCollection = grnItemCollection;
    }

    public Supplier getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Supplier supplierId) {
        this.supplierId = supplierId;
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
        if (!(object instanceof Grn)) {
            return false;
        }
        Grn other = (Grn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.Grn[ id=" + id + " ]";
    }
    
}
