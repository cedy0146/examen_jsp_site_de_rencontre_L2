package com.rencontre.dao;

import com.rencontre.model.Abonnement;
import com.rencontre.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des abonnements.
 */
public class AbonnementDAO {

    public Abonnement findById(int id) {
        String sql = "SELECT * FROM abonnements WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapAbonnement(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Abonnement findActiveByUtilisateurId(int utilisateurId) {
        String sql = "SELECT * FROM abonnements WHERE utilisateur_id = ? AND statut = 'ACTIF' ORDER BY date_debut DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapAbonnement(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Abonnement> findByUtilisateurId(int utilisateurId) {
        List<Abonnement> list = new ArrayList<>();
        String sql = "SELECT * FROM abonnements WHERE utilisateur_id = ? ORDER BY date_debut DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAbonnement(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Abonnement> findAll() {
        List<Abonnement> list = new ArrayList<>();
        String sql = "SELECT * FROM abonnements ORDER BY date_debut DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapAbonnement(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(Abonnement a) {
        String sql = "INSERT INTO abonnements (utilisateur_id, type, date_debut, date_fin, statut, prix) VALUES (?,?,NOW(),?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getUtilisateurId());
            ps.setString(2, a.getType());
            if (a.getDateFin() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(a.getDateFin()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }
            ps.setString(4, a.getStatut() != null ? a.getStatut() : "ACTIF");
            ps.setDouble(5, a.getPrix());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) a.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Abonnement a) {
        String sql = "UPDATE abonnements SET type=?, date_fin=?, statut=?, prix=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getType());
            if (a.getDateFin() != null) {
                ps.setTimestamp(2, Timestamp.valueOf(a.getDateFin()));
            } else {
                ps.setNull(2, Types.TIMESTAMP);
            }
            ps.setString(3, a.getStatut());
            ps.setDouble(4, a.getPrix());
            ps.setInt(5, a.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cancel(int id) {
        String sql = "UPDATE abonnements SET statut = 'ANNULE' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean expireOldAbonnements() {
        String sql = "UPDATE abonnements SET statut = 'EXPIRE' WHERE statut = 'ACTIF' AND date_fin IS NOT NULL AND date_fin < NOW()";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countByType(String type) {
        String sql = "SELECT COUNT(*) FROM abonnements WHERE type = ? AND statut = 'ACTIF'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Abonnement mapAbonnement(ResultSet rs) throws SQLException {
        Abonnement a = new Abonnement();
        a.setId(rs.getInt("id"));
        a.setUtilisateurId(rs.getInt("utilisateur_id"));
        a.setType(rs.getString("type"));
        Timestamp debut = rs.getTimestamp("date_debut");
        if (debut != null) a.setDateDebut(debut.toLocalDateTime());
        Timestamp fin = rs.getTimestamp("date_fin");
        if (fin != null) a.setDateFin(fin.toLocalDateTime());
        a.setStatut(rs.getString("statut"));
        a.setPrix(rs.getDouble("prix"));
        return a;
    }
}

