
package com.qb.app.model;

import com.qb.app.model.entity.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;




public class UnitTestingRavishka {
    public static void main(String[] args) {
        System.out.println("Your password is: "+PasswordEncryption.hashPassword("123"));
        JpaTest();
        
    }
     

    
       private static void JpaTest() {
        JPATransaction.runInTransaction((em) -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);  //Select All from Employee
            Root<Employee> employeeTable = cq.from(Employee.class);

            Predicate usernameCondition = cb.equal(employeeTable.get("username"), "Vihanga");
            Predicate passwordCondition = cb.equal(employeeTable.get("username"), "Vihanga");

            cq.where(cb.and(usernameCondition, passwordCondition));

//            Employee emp = em.createQuery(cq).getSingleResult();
            List<Employee> empList = em.createQuery(cq).getResultList();

            for (Employee employee : empList) {
                System.out.println("Your Name: " + employee.getName());
            }

        });
    }
    
}
