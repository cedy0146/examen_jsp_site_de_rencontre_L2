package com.rencontre.dao;

import com.rencontre.model.Match;
import com.rencontre.model.Utilisateur;
import com.rencontre.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des matchs.
 */
public class MatchDAO {

    public Match findById(int id) {
        String sql = "SELECT * FROM matchs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapMatch(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Match findByUsers(int user1Id, int user2Id) {
        String sql = "SELECT * FROM matchs WHERE (utilisateur1_id = ? AND utilisateur2_id = ?) OR (utilisateur1_id = ? AND utilisateur2_id = ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            ps.setInt(3, user2Id);
            ps.setInt(4, user1Id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapMatch(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Match> findByUtilisateurId(int utilisateurId) {
        List<Match> list = new ArrayList<>();
        String sql = "SELECT * FROM matchs WHERE utilisateur1_id = ? OR utilisateur2_id = ? ORDER BY date_match DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ps.setInt(2, utilisateurId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapMatch(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Match> findAcceptedByUtilisateurId(int utilisateurId) {
        List<Match> list = new ArrayList<>();
        String sql = "SELECT * FROM matchs WHERE (utilisateur1_id = ? OR utilisateur2_id = ?) AND statut = 'ACCEPTE' ORDER BY date_match DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ps.setInt(2, utilisateurId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapMatch(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Match> findSuggestionsForUser(int utilisateurId) {
        List<Match> list = new ArrayList<>();
        String sql = "SELECT * FROM matchs WHERE (utilisateur1_id = ? OR utilisateur2_id = ?) AND statut = 'EN_ATTENTE' ORDER BY score_compatibilite DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ps.setInt(2, utilisateurId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapMatch(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(Match m) {
        String sql = "INSERT INTO matchs (utilisateur1_id, utilisateur2_id, score_compatibilite, statut) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int u1 = m.getUtilisateur1Id();
            int u2 = m.getUtilisateur2Id();
            if (u1 > u2) { int tmp = u1; u1 = u2; u2 = tmp; }
            ps.setInt(1, u1);
            ps.setInt(2, u2);
            ps.setDouble(3, m.getScoreCompatibilite());
            ps.setString(4, m.getStatut() != null ? m.getStatut() : "EN_ATTENTE");
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) m.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE matchs SET statut = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean markAsDejaRencontre(int id) {
        return updateStatus(id, "DEJA_RENCONTRE");
    }

    public boolean acceptMatch(int id) {
        return updateStatus(id, "ACCEPTE");
    }

    public boolean refuseMatch(int id) {
        return updateStatus(id, "REFUSE");
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM matchs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loadUsers(Match m) {
        UtilisateurDAO dao = new UtilisateurDAO();
        m.setUtilisateur1(dao.findById(m.getUtilisateur1Id()));
        m.setUtilisateur2(dao.findById(m.getUtilisateur2Id()));
    }

    private Match mapMatch(ResultSet rs) throws SQLException {
        Match m = new Match();
        m.setId(rs.getInt("id"));
        m.setUtilisateur1Id(rs.getInt("utilisateur1_id"));
        m.setUtilisateur2Id(rs.getInt("utilisateur2_id"));
        m.setScoreCompatibilite(rs.getDouble("score_compatibilite"));
        m.setStatut(rs.getString("statut"));
        Timestamp date = rs.getTimestamp("date_match");
        if (date != null) m.setDateMatch(date.toLocalDateTime());
        return m;
    }
}

