
package com.qb.app.model;

import jakarta.persistence.criteria.CriteriaBuilder;




public class UnitTestingRavishka {
    public static void main(String[] args) {
        System.out.println("Your password is: "+PasswordEncryption.hashPassword("123"));
        JPATest();
        
    }
     
    private static void JPATest(){
        JPATransaction.runInTransaction((em)->{
            CriteriaBuilder cb = em.getCriteriaBuilder();
        });
    }
    
}
