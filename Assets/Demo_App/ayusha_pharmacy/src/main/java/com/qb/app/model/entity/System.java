/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.model.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "system")
@NamedQueries({
    @NamedQuery(name = "System.findAll", query = "SELECT s FROM System s"),
    @NamedQuery(name = "System.findByPrimaryKey", query = "SELECT s FROM System s WHERE s.primaryKey = :primaryKey"),
    @NamedQuery(name = "System.findBySystemName", query = "SELECT s FROM System s WHERE s.systemName = :systemName"),
    @NamedQuery(name = "System.findByInitialDate", query = "SELECT s FROM System s WHERE s.initialDate = :initialDate"),
    @NamedQuery(name = "System.findByDeactiveDate", query = "SELECT s FROM System s WHERE s.deactiveDate = :deactiveDate"),
    @NamedQuery(name = "System.findByTelephone1", query = "SELECT s FROM System s WHERE s.telephone1 = :telephone1"),
    @NamedQuery(name = "System.findByTelephone2", query = "SELECT s FROM System s WHERE s.telephone2 = :telephone2"),
    @NamedQuery(name = "System.findByAddress", query = "SELECT s FROM System s WHERE s.address = :address"),
    @NamedQuery(name = "System.findByDiscountAmount", query = "SELECT s FROM System s WHERE s.discountAmount = :discountAmount"),
    @NamedQuery(name = "System.findByDiscountPercentage", query = "SELECT s FROM System s WHERE s.discountPercentage = :discountPercentage"),
    @NamedQuery(name = "System.findByDiscountRange", query = "SELECT s FROM System s WHERE s.discountRange = :discountRange")})
public class System implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "primary_key")
    private String primaryKey;
    @Basic(optional = false)
    @Column(name = "system_name")
    private String systemName;
    @Basic(optional = false)
    @Column(name = "initial_date")
    @Temporal(TemporalType.DATE)
    private Date initialDate;
    @Basic(optional = false)
    @Column(name = "deactive_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deactiveDate;
    @Basic(optional = false)
    @Column(name = "telephone_1")
    private String telephone1;
    @Basic(optional = false)
    @Column(name = "telephone_2")
    private String telephone2;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @Column(name = "discount_amount")
    private double discountAmount;
    @Basic(optional = false)
    @Column(name = "discount_percentage")
    private double discountPercentage;
    @Basic(optional = false)
    @Column(name = "discount_range")
    private double discountRange;
    @JoinColumn(name = "discount_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DiscountType discountTypeId;
    @JoinColumn(name = "license_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private License licenseId;

    public System() {
    }

    public System(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public System(String primaryKey, String systemName, Date initialDate, Date deactiveDate, String telephone1, String telephone2, String address, double discountAmount, double discountPercentage, double discountRange) {
        this.primaryKey = primaryKey;
        this.systemName = systemName;
        this.initialDate = initialDate;
        this.deactiveDate = deactiveDate;
        this.telephone1 = telephone1;
        this.telephone2 = telephone2;
        this.address = address;
        this.discountAmount = discountAmount;
        this.discountPercentage = discountPercentage;
        this.discountRange = discountRange;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getDeactiveDate() {
        return deactiveDate;
    }

    public void setDeactiveDate(Date deactiveDate) {
        this.deactiveDate = deactiveDate;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getDiscountRange() {
        return discountRange;
    }

    public void setDiscountRange(double discountRange) {
        this.discountRange = discountRange;
    }

    public DiscountType getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(DiscountType discountTypeId) {
        this.discountTypeId = discountTypeId;
    }

    public License getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(License licenseId) {
        this.licenseId = licenseId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (primaryKey != null ? primaryKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof System)) {
            return false;
        }
        System other = (System) object;
        if ((this.primaryKey == null && other.primaryKey != null) || (this.primaryKey != null && !this.primaryKey.equals(other.primaryKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.System[ primaryKey=" + primaryKey + " ]";
    }
    
}
