package com.rencontre.dao;

import com.rencontre.model.Signalement;
import com.rencontre.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des signalements utilisateur.
 */
public class SignalementDAO {

    public Signalement findById(int id) {
        String sql = "SELECT * FROM signalements WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapSignalement(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Signalement> findAll() {
        List<Signalement> list = new ArrayList<>();
        String sql = "SELECT * FROM signalements ORDER BY date_signalement DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapSignalement(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Signalement> findByStatut(String statut) {
        List<Signalement> list = new ArrayList<>();
        String sql = "SELECT * FROM signalements WHERE statut = ? ORDER BY date_signalement DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapSignalement(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(Signalement s) {
        String sql = "INSERT INTO signalements (signalant_id, signale_id, motif, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getSignalantId());
            ps.setInt(2, s.getSignaleId());
            ps.setString(3, s.getMotif());
            ps.setString(4, s.getDescription());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) s.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatut(int id, String statut) {
        String sql = "UPDATE signalements SET statut = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM signalements WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Signalement mapSignalement(ResultSet rs) throws SQLException {
        Signalement s = new Signalement();
        s.setId(rs.getInt("id"));
        s.setSignalantId(rs.getInt("signalant_id"));
        s.setSignaleId(rs.getInt("signale_id"));
        s.setMotif(rs.getString("motif"));
        s.setDescription(rs.getString("description"));
        s.setStatut(rs.getString("statut"));
        Timestamp date = rs.getTimestamp("date_signalement");
        if (date != null) s.setDateSignalement(date.toLocalDateTime());
        return s;
    }
}

