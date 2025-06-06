package com.qb.app.model;

import com.qb.app.controllers.report.beans.InvoiceItemBean;
import com.qb.app.model.entity.Brand;
import com.qb.app.model.entity.Employee;
import com.qb.app.model.entity.Session;
import com.qb.app.session.CompanyInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.HibernateException;

public class UnitTestingVihanga {

    public static void main(String[] args) {
//        testJPA();
//        getSessionDetails();
//        loadComboBoxData();
//        testRun();
//        passwordTest();
//        testDatabaseResults();
//        testSubReport();
    }

    private static void testJPA() {
        EntityManager entityManager = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Employee employee = entityManager.find(Employee.class, 1L);
            System.out.println("Employee Name: " + employee.getName());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    private static boolean isSignIn;

    private static void getSessionDetails() {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = JpaUtil.getEntityManager(); // Your utility method for getting EntityManager
            transaction = em.getTransaction();
            transaction.begin();

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Session> criteriaQuery = criteriaBuilder.createQuery(Session.class);
            Root<Session> sessionTable = criteriaQuery.from(Session.class);

            LocalDate today = LocalDate.now(); // For '2025-04-27', you can use LocalDate.of(2025, 4, 27);

            // Build Predicate (where DATE(day_in_time) = today)
            Predicate predicate = criteriaBuilder.equal(
                    criteriaBuilder.function("DATE", Date.class, sessionTable.get("dayInTime")),
                    java.sql.Date.valueOf(today)
            );

            criteriaQuery.select(sessionTable).where(predicate);

            try {
                Session sessionToday = em.createQuery(criteriaQuery).getSingleResult();

                if (sessionToday.getStatus().equals("OFF")) {
                    System.out.println("Day Completed.");
                } else {
                    System.out.println("Already Sign In for Today.");
                    System.out.println("Waiting for Sign OFF.");
                    isSignIn = true;
                }
            } catch (NoResultException e) {
                System.out.println("No session found for today.");
                System.out.println("Waiting for Sign In.");
                System.out.println("Sign OFF is not activated.");
            }

        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println("Error during login: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private static void loadComboBoxData() {
        try {
            JPATransaction.runInTransaction((em) -> {
                CriteriaBuilder cBuilder = em.getCriteriaBuilder();
                CriteriaQuery<Brand> cQuery = cBuilder.createQuery(Brand.class);
                Root<Brand> brandTable = cQuery.from(Brand.class);

                cQuery.orderBy(cBuilder.asc(brandTable.get("brand")));

                List<Brand> brandList = em.createQuery(cQuery).getResultList();

                ObservableList<Brand> observableList = FXCollections.observableArrayList(brandList);
                for (Brand brand : observableList) {
                    System.out.println(brand.getBrand());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testRun() {
        double unitPrice = 0;
        double itemQty = 1;
        System.out.println(String.format("Rs. %.2f", unitPrice * itemQty));
    }

    private static void passwordTest() {
//        System.out.println(PasswordEncryption.hashPassword("ASD123"));
//        if (PasswordEncryption.verifyPassword("$argon2i$v=19$m=65536,t=10,p=4$eazGlsy3aWcg9pCFadMIzw$w162xY1rop7uRn5fFqCdrSLmviESEO3PXKUr9QgmBow", "asd321")) {
//        }
        System.out.println("Your password is: " + PasswordEncryption.hashPassword("asd321"));
    }

    private static void testDatabaseResults() {
        try {
            JPATransaction.runInTransaction((em) -> {
                CriteriaBuilder cBuilder = em.getCriteriaBuilder();
                CriteriaQuery<Employee> cQuery = cBuilder.createQuery(Employee.class);
                Root<Employee> rootTable = cQuery.from(Employee.class);

                Predicate condition = cBuilder.equal(rootTable.get("username"), "Cashier");
                cQuery.where(condition);

                Employee emp = em.createQuery(cQuery).getSingleResult();
                System.out.println("Employee Name: " + emp.getName());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testSubReport() {

        List<TestProduct> brand1Products = new ArrayList<>();
        brand1Products.add(new TestProduct("Product 1"));
        brand1Products.add(new TestProduct("Product 2"));

        List<TestProduct> brand2Products = new ArrayList<>();
        brand2Products.add(new TestProduct("Product 3"));

        List<TestBrand> brandList = new ArrayList<>();
        brandList.add(new TestBrand("Brand 1", brand1Products));
        brandList.add(new TestBrand("Brand 2", brand2Products));

        Map<String, Object> params = new HashMap<>();
        try {
            JasperReport subReport = (JasperReport) JRLoader.loadObject(
                    UnitTestingVihanga.class.getResourceAsStream("/com/qb/app/reports/PharmacyStockBalanceSubReport.jasper"));
            params.put("SUB_REPORT_PATH", subReport);
            params.put("BrandList", brandList);

            JasperReport mainReport = (JasperReport) JRLoader.loadObject(
                    UnitTestingVihanga.class.getResourceAsStream("/com/qb/app/reports/PharmacyStockBalance.jasper"));

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(brandList);

            JasperPrint report = JasperFillManager.fillReport(mainReport, params, dataSource);
            JasperViewer.viewReport(report, false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
