package com.rencontre.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour la gestion de la connexion à la base de données MySQL.
 * Implémente le pattern Singleton pour une connexion unique.
 */
public class DBConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/site_rencontre?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Erreur de chargement du driver MySQL", e);
        }
    }
    
    /**
     * Obtient une connexion à la base de données.
     * @return Connection active
     * @throws SQLException si la connexion échoue
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    /**
     * Ferme une connexion de manière sécurisée.
     * @param conn La connexion à fermer
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
    
    /**
     * Ferme une connexion et un PreparedStatement de manière sécurisée.
     */
    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    System.err.println("Erreur lors de la fermeture d'une ressource : " + e.getMessage());
                }
            }
        }
    }
}

