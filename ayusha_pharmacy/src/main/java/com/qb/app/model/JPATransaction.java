/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 *
 * @author Vihanga
 */
public class JPATransaction {

    public static void runInTransaction(EntityManagerCallBack callback) {
        EntityManager em = null;
        EntityTransaction tx = null;

        try {
            em = JpaUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            callback.execute(em);  // Custom interface you'll define

            tx.commit();
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Transaction error: " + ex.getMessage());
            throw new RuntimeException("Database error", ex);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public static <T> T runInTransaction(EntityManagerFunction<T> function) {
        EntityManager em = null;
        EntityTransaction tx = null;

        try {
            em = JpaUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            T result = function.execute(em);

            tx.commit();
            return result;
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Transaction error", ex);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
