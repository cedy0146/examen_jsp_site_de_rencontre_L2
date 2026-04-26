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
     * Retourne le mot de passe tel quel (hachage désactivé).
     * @param password Le mot de passe en clair
     * @return Le mot de passe en clair
     */
    public static String hashPassword(String password) {
        return password;
    }
    
    /**
     * Vérifie si un mot de passe correspond à la valeur stockée (comparaison directe).
     * @param password Le mot de passe en clair
     * @param storedHash La valeur stockée
     * @return true si le mot de passe correspond
     */
    public static boolean verifyPassword(String password, String storedHash) {
        return password != null && password.equals(storedHash);
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

    /**
     * Vérifie si un mot de passe respecte les règles de complexité.
     * Au moins : 1 minuscule, 1 majuscule, 1 chiffre, 1 caractère spécial, 8 caractères minimum.
     * @param password Le mot de passe à vérifier
     * @return true si le mot de passe est valide
     */
    public static boolean isPasswordValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        return hasLower && hasUpper && hasDigit && hasSpecial;
    }

    /**
     * Retourne un message décrivant les règles de complexité du mot de passe.
     * @return Message d'erreur descriptif
     */
    public static String getPasswordRulesMessage() {
        return "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.";
    }
}
