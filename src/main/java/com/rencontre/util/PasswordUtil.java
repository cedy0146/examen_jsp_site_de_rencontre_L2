package com.rencontre.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Classe utilitaire pour le chiffrement des mots de passe.
 * Utilise SHA-256 avec un sel aléatoire pour une sécurité renforcée.
 */
public class PasswordUtil {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    
    /**
     * Génère un sel aléatoire.
     * @return Le sel encodé en Base64
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Hache un mot de passe avec un sel donné.
     * @param password Le mot de passe en clair
     * @param salt Le sel
     * @return Le hash encodé en Base64
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithme de hachage non disponible", e);
        }
    }
    
    /**
     * Hache un mot de passe avec un nouveau sel généré automatiquement.
     * Format du résultat : salt:hash
     * @param password Le mot de passe en clair
     * @return Le sel et le hash combinés
     */
    public static String hashPassword(String password) {
        String salt = generateSalt();
        String hash = hashPassword(password, salt);
        return salt + ":" + hash;
    }
    
    /**
     * Vérifie si un mot de passe correspond au hash stocké.
     * @param password Le mot de passe en clair
     * @param storedHash Le hash stocké (format: salt:hash)
     * @return true si le mot de passe correspond
     */
    public static boolean verifyPassword(String password, String storedHash) {
        String[] parts = storedHash.split(":");
        if (parts.length != 2) {
            return false;
        }
        String salt = parts[0];
        String hash = parts[1];
        String computedHash = hashPassword(password, salt);
        return hash.equals(computedHash);
    }
    
    /**
     * Génère un token sécurisé pour la session ou la 2FA.
     * @return Token aléatoire
     */
    public static String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        byte[] token = new byte[32];
        random.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }
}

