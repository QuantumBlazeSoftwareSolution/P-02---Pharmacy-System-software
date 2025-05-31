/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qb.app.model;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author Vihanga
 */
public class PasswordEncryption {

    private static final Argon2 ARGON2 = Argon2Factory.create();

    // Adjust these parameters based on your security needs and hardware
    private static final int ITERATIONS = 10;
    private static final int MEMORY = 65536; // 64MB
    private static final int PARALLELISM = 4;

    /**
     * Hashes a password with a random salt using Argon2
     */
    public static String hashPassword(String password) {
        // Generate a random salt (already handled by argon2 internally)
        try {
            return ARGON2.hash(ITERATIONS, MEMORY, PARALLELISM, password.toCharArray());
        } finally {
            // Wipe the internal buffers
            ARGON2.wipeArray(password.toCharArray());
        }
    }

    /**
     * Verifies a password against a stored hash
     */
    public static boolean verifyPassword(String hash, String password) {
        try {
            return ARGON2.verify(hash, password.toCharArray());
        } finally {
            // Wipe the internal buffers
            ARGON2.wipeArray(password.toCharArray());
        }
    }

    /**
     * Generates a random password (for admin creating employee accounts)
     */
    public static String generateRandomPassword() {
        // 12 characters with letters and numbers
        return RandomStringUtils.randomAlphanumeric(12);
    }
}
