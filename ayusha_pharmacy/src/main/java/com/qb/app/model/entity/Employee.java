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
@Table(name = "employee")
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findById", query = "SELECT e FROM Employee e WHERE e.id = :id"),
    @NamedQuery(name = "Employee.findByName", query = "SELECT e FROM Employee e WHERE e.name = :name"),
    @NamedQuery(name = "Employee.findByUsername", query = "SELECT e FROM Employee e WHERE e.username = :username"),
    @NamedQuery(name = "Employee.findByPassword", query = "SELECT e FROM Employee e WHERE e.password = :password")})
public class Employee implements Serializable {

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
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId")
    private Collection<StockAdjustment> stockAdjustmentCollection;
    @JoinColumn(name = "employee_role_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EmployeeRole employeeRoleId;
    @JoinColumn(name = "employee_status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EmployeeStatus employeeStatusId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId")
    private Collection<ProductDistribute> productDistributeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId")
    private Collection<Damage> damageCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId")
    private Collection<Session> sessionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId")
    private Collection<LocationReturn> locationReturnCollection;

    public Employee() {
    }

    public Employee(Integer id) {
        this.id = id;
    }

    public Employee(Integer id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<StockAdjustment> getStockAdjustmentCollection() {
        return stockAdjustmentCollection;
    }

    public void setStockAdjustmentCollection(Collection<StockAdjustment> stockAdjustmentCollection) {
        this.stockAdjustmentCollection = stockAdjustmentCollection;
    }

    public EmployeeRole getEmployeeRoleId() {
        return employeeRoleId;
    }

    public void setEmployeeRoleId(EmployeeRole employeeRoleId) {
        this.employeeRoleId = employeeRoleId;
    }

    public EmployeeStatus getEmployeeStatusId() {
        return employeeStatusId;
    }

    public void setEmployeeStatusId(EmployeeStatus employeeStatusId) {
        this.employeeStatusId = employeeStatusId;
    }

    public Collection<ProductDistribute> getProductDistributeCollection() {
        return productDistributeCollection;
    }

    public void setProductDistributeCollection(Collection<ProductDistribute> productDistributeCollection) {
        this.productDistributeCollection = productDistributeCollection;
    }

    public Collection<Damage> getDamageCollection() {
        return damageCollection;
    }

    public void setDamageCollection(Collection<Damage> damageCollection) {
        this.damageCollection = damageCollection;
    }

    public Collection<Session> getSessionCollection() {
        return sessionCollection;
    }

    public void setSessionCollection(Collection<Session> sessionCollection) {
        this.sessionCollection = sessionCollection;
    }

    public Collection<LocationReturn> getLocationReturnCollection() {
        return locationReturnCollection;
    }

    public void setLocationReturnCollection(Collection<LocationReturn> locationReturnCollection) {
        this.locationReturnCollection = locationReturnCollection;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.Employee[ id=" + id + " ]";
    }
    
}
