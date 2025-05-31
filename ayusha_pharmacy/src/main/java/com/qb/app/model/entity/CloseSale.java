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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Vihanga
 */
@Entity
@Table(name = "close_sale")
@NamedQueries({
    @NamedQuery(name = "CloseSale.findAll", query = "SELECT c FROM CloseSale c"),
    @NamedQuery(name = "CloseSale.findById", query = "SELECT c FROM CloseSale c WHERE c.id = :id"),
    @NamedQuery(name = "CloseSale.findByC5000", query = "SELECT c FROM CloseSale c WHERE c.c5000 = :c5000"),
    @NamedQuery(name = "CloseSale.findByC1000", query = "SELECT c FROM CloseSale c WHERE c.c1000 = :c1000"),
    @NamedQuery(name = "CloseSale.findByC500", query = "SELECT c FROM CloseSale c WHERE c.c500 = :c500"),
    @NamedQuery(name = "CloseSale.findByC100", query = "SELECT c FROM CloseSale c WHERE c.c100 = :c100"),
    @NamedQuery(name = "CloseSale.findByC50", query = "SELECT c FROM CloseSale c WHERE c.c50 = :c50"),
    @NamedQuery(name = "CloseSale.findByC20", query = "SELECT c FROM CloseSale c WHERE c.c20 = :c20"),
    @NamedQuery(name = "CloseSale.findByC10", query = "SELECT c FROM CloseSale c WHERE c.c10 = :c10"),
    @NamedQuery(name = "CloseSale.findByC5", query = "SELECT c FROM CloseSale c WHERE c.c5 = :c5"),
    @NamedQuery(name = "CloseSale.findByDateTime", query = "SELECT c FROM CloseSale c WHERE c.dateTime = :dateTime")})
public class CloseSale implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "c5000")
    private int c5000;
    @Basic(optional = false)
    @Column(name = "c1000")
    private int c1000;
    @Basic(optional = false)
    @Column(name = "c500")
    private int c500;
    @Basic(optional = false)
    @Column(name = "c100")
    private int c100;
    @Basic(optional = false)
    @Column(name = "c50")
    private int c50;
    @Basic(optional = false)
    @Column(name = "c20")
    private int c20;
    @Basic(optional = false)
    @Column(name = "c10")
    private int c10;
    @Basic(optional = false)
    @Column(name = "c5")
    private int c5;
    @Basic(optional = false)
    @Column(name = "date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Session sessionId;

    public CloseSale() {
    }

    public CloseSale(Integer id) {
        this.id = id;
    }

    public CloseSale(Integer id, int c5000, int c1000, int c500, int c100, int c50, int c20, int c10, int c5, Date dateTime) {
        this.id = id;
        this.c5000 = c5000;
        this.c1000 = c1000;
        this.c500 = c500;
        this.c100 = c100;
        this.c50 = c50;
        this.c20 = c20;
        this.c10 = c10;
        this.c5 = c5;
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getC5000() {
        return c5000;
    }

    public void setC5000(int c5000) {
        this.c5000 = c5000;
    }

    public int getC1000() {
        return c1000;
    }

    public void setC1000(int c1000) {
        this.c1000 = c1000;
    }

    public int getC500() {
        return c500;
    }

    public void setC500(int c500) {
        this.c500 = c500;
    }

    public int getC100() {
        return c100;
    }

    public void setC100(int c100) {
        this.c100 = c100;
    }

    public int getC50() {
        return c50;
    }

    public void setC50(int c50) {
        this.c50 = c50;
    }

    public int getC20() {
        return c20;
    }

    public void setC20(int c20) {
        this.c20 = c20;
    }

    public int getC10() {
        return c10;
    }

    public void setC10(int c10) {
        this.c10 = c10;
    }

    public int getC5() {
        return c5;
    }

    public void setC5(int c5) {
        this.c5 = c5;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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
        if (!(object instanceof CloseSale)) {
            return false;
        }
        CloseSale other = (CloseSale) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.CloseSale[ id=" + id + " ]";
    }
    
}
