/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.model;

import com.qb.app.model.entity.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

/**
 *
 * @author user
 */
public class UnitTestingSupun {
    
     
    
    
    public static void main(String[] args) {
       
        JPAtest();
        System.out.println("Your password is: "+PasswordEncryption.hashPassword("1234"));
    }
    
    public static void JPAtest(){
        JPATransaction.runInTransaction((em)->{
            CriteriaBuilder cb01 = em.getCriteriaBuilder();
            CriteriaQuery<Employee> cq01 = cb01.createQuery(Employee.class); //SELECT * FROM
            Root<Employee> employeeTable = cq01.from(Employee.class); // 
            
            List<Employee> emplist = em.createQuery(cq01).getResultList();
            
            for (Employee employee : emplist) {
                System.out.println("Your name : "+employee.getName());
                
            }
        });
    }
    
}
