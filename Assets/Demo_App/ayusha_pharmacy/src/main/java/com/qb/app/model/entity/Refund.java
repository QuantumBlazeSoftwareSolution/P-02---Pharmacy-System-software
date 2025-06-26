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
@Table(name = "refund")
@NamedQueries({
    @NamedQuery(name = "Refund.findAll", query = "SELECT r FROM Refund r"),
    @NamedQuery(name = "Refund.findById", query = "SELECT r FROM Refund r WHERE r.id = :id"),
    @NamedQuery(name = "Refund.findByDateTime", query = "SELECT r FROM Refund r WHERE r.dateTime = :dateTime"),
    @NamedQuery(name = "Refund.findByComission", query = "SELECT r FROM Refund r WHERE r.comission = :comission"),
    @NamedQuery(name = "Refund.findByRefundAmount", query = "SELECT r FROM Refund r WHERE r.refundAmount = :refundAmount")})
public class Refund implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    @Basic(optional = false)
    @Column(name = "comission")
    private double comission;
    @Basic(optional = false)
    @Column(name = "refund_amount")
    private double refundAmount;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refundId")
    private Collection<RefundItem> refundItemCollection;
    @JoinColumn(name = "refund_status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RefundStatus refundStatusId;
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Session sessionId;

    public Refund() {
    }

    public Refund(Integer id) {
        this.id = id;
    }

    public Refund(Integer id, Date dateTime, double comission, double refundAmount) {
        this.id = id;
        this.dateTime = dateTime;
        this.comission = comission;
        this.refundAmount = refundAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public double getComission() {
        return comission;
    }

    public void setComission(double comission) {
        this.comission = comission;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Collection<RefundItem> getRefundItemCollection() {
        return refundItemCollection;
    }

    public void setRefundItemCollection(Collection<RefundItem> refundItemCollection) {
        this.refundItemCollection = refundItemCollection;
    }

    public RefundStatus getRefundStatusId() {
        return refundStatusId;
    }

    public void setRefundStatusId(RefundStatus refundStatusId) {
        this.refundStatusId = refundStatusId;
    }

    public Session getSessionId() {
        return sessionId;
    }

    public void setSessionId(Session sessionId) {
        this.sessionId = sessionId;
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
        if (!(object instanceof Refund)) {
            return false;
        }
        Refund other = (Refund) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.Refund[ id=" + id + " ]";
    }
    
}
