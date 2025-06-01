package com.qb.app.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JpaUtil {
    private static final String PERSISTENCE_UNIT_NAME = "quantumRetailDBUnit";
    private static EntityManagerFactory factory;

    static {
        try {
            Map<String, String> properties = new HashMap<>();
            
            // Get credentials from system environment variables
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");
            System.out.println(dbUser);
            System.out.println(dbPassword);
            
            // Validate and add credentials to properties
            if (dbUser != null && !dbUser.isEmpty()) {
                properties.put("jakarta.persistence.jdbc.user", dbUser);
            }
            if (dbPassword != null && !dbPassword.isEmpty()) {
                properties.put("jakarta.persistence.jdbc.password", dbPassword);
            }
            
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create EntityManagerFactory", e);
        }
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void close() {
        if (factory != null) {
            factory.close();
        }
    }
}