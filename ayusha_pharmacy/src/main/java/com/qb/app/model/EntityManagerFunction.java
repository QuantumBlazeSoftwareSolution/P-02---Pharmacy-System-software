/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qb.app.model;

import jakarta.persistence.EntityManager;

/**
 *
 * @author Vihanga
 */
@FunctionalInterface
public interface EntityManagerFunction<T> {

    T execute(EntityManager em);
}
