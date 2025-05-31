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
@Table(name = "location_return")
@NamedQueries({
    @NamedQuery(name = "LocationReturn.findAll", query = "SELECT l FROM LocationReturn l"),
    @NamedQuery(name = "LocationReturn.findById", query = "SELECT l FROM LocationReturn l WHERE l.id = :id"),
    @NamedQuery(name = "LocationReturn.findByDateTime", query = "SELECT l FROM LocationReturn l WHERE l.dateTime = :dateTime")})
public class LocationReturn implements Serializable {

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
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employee employeeId;
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Location locationId;
    @JoinColumn(name = "location_return_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LocationReturnType locationReturnTypeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "locationReturnId")
    private Collection<LocationReturnItem> locationReturnItemCollection;

    public LocationReturn() {
    }

    public LocationReturn(Integer id) {
        this.id = id;
    }

    public LocationReturn(Integer id, Date dateTime) {
        this.id = id;
        this.dateTime = dateTime;
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

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public Location getLocationId() {
        return locationId;
    }

    public void setLocationId(Location locationId) {
        this.locationId = locationId;
    }

    public LocationReturnType getLocationReturnTypeId() {
        return locationReturnTypeId;
    }

    public void setLocationReturnTypeId(LocationReturnType locationReturnTypeId) {
        this.locationReturnTypeId = locationReturnTypeId;
    }

    public Collection<LocationReturnItem> getLocationReturnItemCollection() {
        return locationReturnItemCollection;
    }

    public void setLocationReturnItemCollection(Collection<LocationReturnItem> locationReturnItemCollection) {
        this.locationReturnItemCollection = locationReturnItemCollection;
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
        if (!(object instanceof LocationReturn)) {
            return false;
        }
        LocationReturn other = (LocationReturn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.LocationReturn[ id=" + id + " ]";
    }
    
}
