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
@Table(name = "refund_status")
@NamedQueries({
    @NamedQuery(name = "RefundStatus.findAll", query = "SELECT r FROM RefundStatus r"),
    @NamedQuery(name = "RefundStatus.findById", query = "SELECT r FROM RefundStatus r WHERE r.id = :id"),
    @NamedQuery(name = "RefundStatus.findByStatus", query = "SELECT r FROM RefundStatus r WHERE r.status = :status")})
public class RefundStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refundStatusId")
    private Collection<Refund> refundCollection;

    public RefundStatus() {
    }

    public RefundStatus(Integer id) {
        this.id = id;
    }

    public RefundStatus(Integer id, String status) {
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

    public Collection<Refund> getRefundCollection() {
        return refundCollection;
    }

    public void setRefundCollection(Collection<Refund> refundCollection) {
        this.refundCollection = refundCollection;
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
        if (!(object instanceof RefundStatus)) {
            return false;
        }
        RefundStatus other = (RefundStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.RefundStatus[ id=" + id + " ]";
    }
    
}
