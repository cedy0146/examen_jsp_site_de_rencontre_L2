package com.rencontre.dao;

import com.rencontre.model.Statistiques;
import com.rencontre.util.DBConnection;

import java.sql.*;

/**
 * DAO pour les statistiques utilisateur et admin.
 */
public class StatistiquesDAO {

    public Statistiques findByUtilisateurId(int utilisateurId) {
        Statistiques stats = new Statistiques();
        String sqlVues = "SELECT COUNT(*) FROM interactions WHERE destinataire_id = ? AND type = 'VUE'";
        String sqlLikes = "SELECT COUNT(*) FROM interactions WHERE destinataire_id = ? AND type = 'LIKE'";
        String sqlMessages = "SELECT COUNT(*) FROM messages WHERE expediteur_id = ? OR destinataire_id = ?";
        String sqlMatchs = "SELECT COUNT(*) FROM matchs WHERE (utilisateur1_id = ? OR utilisateur2_id = ?) AND statut = 'ACCEPTE'";
        String sqlConnexions = "SELECT COUNT(*) FROM historique_connexions WHERE utilisateur_id = ?";
        String sqlInteractions = "SELECT COUNT(*) FROM interactions WHERE expediteur_id = ? OR destinataire_id = ?";
        String sqlTaux = "SELECT AVG(score_compatibilite) FROM matchs WHERE utilisateur1_id = ? OR utilisateur2_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            stats.setNombreVues(count(conn, sqlVues, utilisateurId));
            stats.setNombreLikes(count(conn, sqlLikes, utilisateurId));
            stats.setNombreMessages(count2(conn, sqlMessages, utilisateurId, utilisateurId));
            stats.setNombreMatchs(count2(conn, sqlMatchs, utilisateurId, utilisateurId));
            stats.setNombreConnexions(count(conn, sqlConnexions, utilisateurId));
            stats.setNombreInteractions(count2(conn, sqlInteractions, utilisateurId, utilisateurId));
            stats.setTauxCompatibiliteMoyen(avg(conn, sqlTaux, utilisateurId, utilisateurId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public Statistiques findGlobalStats() {
        Statistiques stats = new Statistiques();
        try (Connection conn = DBConnection.getConnection()) {
            stats.setTotalUtilisateurs(count(conn, "SELECT COUNT(*) FROM utilisateurs WHERE statut != 'SUPPRIME'"));
            stats.setUtilisateursActifs(count(conn, "SELECT COUNT(*) FROM utilisateurs WHERE statut = 'ACTIF'"));
            stats.setTotalAbonnementsPremium(count(conn, "SELECT COUNT(*) FROM abonnements WHERE type = 'PREMIUM' AND statut = 'ACTIF'"));
            stats.setTotalAbonnementsVip(count(conn, "SELECT COUNT(*) FROM abonnements WHERE type = 'VIP' AND statut = 'ACTIF'"));
            stats.setRevenusTotaux(sum(conn, "SELECT SUM(prix) FROM abonnements WHERE statut = 'ACTIF'"));
            stats.setNouveauxUtilisateursMois(count(conn, "SELECT COUNT(*) FROM utilisateurs WHERE date_inscription >= DATE_SUB(NOW(), INTERVAL 1 MONTH)"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    private int count(Connection conn, String sql, int param) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, param);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private int count2(Connection conn, String sql, int p1, int p2) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p1);
            ps.setInt(2, p2);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private int count(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private double sum(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }

    private double avg(Connection conn, String sql, int p1, int p2) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p1);
            ps.setInt(2, p2);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }
}

