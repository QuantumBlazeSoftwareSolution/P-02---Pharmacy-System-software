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
@Table(name = "employee_role")
@NamedQueries({
    @NamedQuery(name = "EmployeeRole.findAll", query = "SELECT e FROM EmployeeRole e"),
    @NamedQuery(name = "EmployeeRole.findById", query = "SELECT e FROM EmployeeRole e WHERE e.id = :id"),
    @NamedQuery(name = "EmployeeRole.findByRole", query = "SELECT e FROM EmployeeRole e WHERE e.role = :role")})
public class EmployeeRole implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "role")
    private String role;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeRoleId")
    private Collection<EmployeeRoleHasInterface> employeeRoleHasInterfaceCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeRoleId")
    private Collection<Employee> employeeCollection;
    @JoinColumn(name = "employee_role_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EmployeeRoleType employeeRoleTypeId;

    public EmployeeRole() {
    }

    public EmployeeRole(Integer id) {
        this.id = id;
    }

    public EmployeeRole(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Collection<EmployeeRoleHasInterface> getEmployeeRoleHasInterfaceCollection() {
        return employeeRoleHasInterfaceCollection;
    }

    public void setEmployeeRoleHasInterfaceCollection(Collection<EmployeeRoleHasInterface> employeeRoleHasInterfaceCollection) {
        this.employeeRoleHasInterfaceCollection = employeeRoleHasInterfaceCollection;
    }

    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
    }

    public EmployeeRoleType getEmployeeRoleTypeId() {
        return employeeRoleTypeId;
    }

    public void setEmployeeRoleTypeId(EmployeeRoleType employeeRoleTypeId) {
        this.employeeRoleTypeId = employeeRoleTypeId;
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
        if (!(object instanceof EmployeeRole)) {
            return false;
        }
        EmployeeRole other = (EmployeeRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qb.app.model.entity.EmployeeRole[ id=" + id + " ]";
    }
    
}
