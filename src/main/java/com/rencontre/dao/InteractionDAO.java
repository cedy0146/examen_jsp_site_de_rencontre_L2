package com.rencontre.dao;

import com.rencontre.model.Interaction;
import com.rencontre.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des interactions (likes, vues, blocages, etc.).
 */
public class InteractionDAO {

    public Interaction findById(int id) {
        String sql = "SELECT * FROM interactions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapInteraction(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Interaction> findByExpediteurId(int expediteurId) {
        List<Interaction> list = new ArrayList<>();
        String sql = "SELECT * FROM interactions WHERE expediteur_id = ? ORDER BY date_interaction DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, expediteurId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapInteraction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Interaction> findByDestinataireId(int destinataireId) {
        List<Interaction> list = new ArrayList<>();
        String sql = "SELECT * FROM interactions WHERE destinataire_id = ? ORDER BY date_interaction DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, destinataireId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapInteraction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Interaction> findByType(int utilisateurId, String type) {
        List<Interaction> list = new ArrayList<>();
        String sql = "SELECT * FROM interactions WHERE (expediteur_id = ? OR destinataire_id = ?) AND type = ? ORDER BY date_interaction DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ps.setInt(2, utilisateurId);
            ps.setString(3, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapInteraction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean existsLike(int expediteurId, int destinataireId) {
        String sql = "SELECT COUNT(*) FROM interactions WHERE expediteur_id = ? AND destinataire_id = ? AND type = 'LIKE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, expediteurId);
            ps.setInt(2, destinataireId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isBlocked(int blockerId, int blockedId) {
        String sql = "SELECT COUNT(*) FROM interactions WHERE expediteur_id = ? AND destinataire_id = ? AND type = 'BLOCAGE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, blockerId);
            ps.setInt(2, blockedId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean create(Interaction i) {
        String sql = "INSERT INTO interactions (expediteur_id, destinataire_id, type, contenu, lu) VALUES (?,?,?,?,FALSE)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i.getExpediteurId());
            ps.setInt(2, i.getDestinataireId());
            ps.setString(3, i.getType());
            ps.setString(4, i.getContenu());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) i.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean markAsRead(int id) {
        String sql = "UPDATE interactions SET lu = TRUE WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM interactions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteLike(int expediteurId, int destinataireId) {
        String sql = "DELETE FROM interactions WHERE expediteur_id = ? AND destinataire_id = ? AND type = 'LIKE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, expediteurId);
            ps.setInt(2, destinataireId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countByTypeAndDestinataire(String type, int destinataireId) {
        String sql = "SELECT COUNT(*) FROM interactions WHERE type = ? AND destinataire_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setInt(2, destinataireId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Interaction mapInteraction(ResultSet rs) throws SQLException {
        Interaction i = new Interaction();
        i.setId(rs.getInt("id"));
        i.setExpediteurId(rs.getInt("expediteur_id"));
        i.setDestinataireId(rs.getInt("destinataire_id"));
        i.setType(rs.getString("type"));
        i.setContenu(rs.getString("contenu"));
        Timestamp date = rs.getTimestamp("date_interaction");
        if (date != null) i.setDateInteraction(date.toLocalDateTime());
        i.setLu(rs.getBoolean("lu"));
        return i;
    }
}

