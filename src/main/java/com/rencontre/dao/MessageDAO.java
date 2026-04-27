package com.rencontre.dao;

import com.rencontre.model.Message;
import com.rencontre.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des messages.
 */
public class MessageDAO {

    public Message findById(int id) {
        String sql = "SELECT * FROM messages WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapMessage(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> findConversation(int user1Id, int user2Id) {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE (expediteur_id = ? AND destinataire_id = ?) OR (expediteur_id = ? AND destinataire_id = ?) ORDER BY date_envoi ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            ps.setInt(3, user2Id);
            ps.setInt(4, user1Id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Message> findByDestinataireId(int destinataireId) {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE destinataire_id = ? ORDER BY date_envoi DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, destinataireId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Message> findUnreadByDestinataireId(int destinataireId) {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE destinataire_id = ? AND lu = FALSE ORDER BY date_envoi DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, destinataireId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Integer> findConversationPartners(int userId) {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT DISTINCT CASE WHEN expediteur_id = ? THEN destinataire_id ELSE expediteur_id END AS partner FROM messages WHERE expediteur_id = ? OR destinataire_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getInt("partner"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(Message m) {
        String sql = "INSERT INTO messages (expediteur_id, destinataire_id, contenu, lu) VALUES (?,?,?,FALSE)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getExpediteurId());
            ps.setInt(2, m.getDestinataireId());
            ps.setString(3, m.getContenu());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) m.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean markAsRead(int id) {
        String sql = "UPDATE messages SET lu = TRUE WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean markConversationAsRead(int userId, int partnerId) {
        String sql = "UPDATE messages SET lu = TRUE WHERE destinataire_id = ? AND expediteur_id = ? AND lu = FALSE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, partnerId);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM messages WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countUnread(int destinataireId) {
        String sql = "SELECT COUNT(*) FROM messages WHERE destinataire_id = ? AND lu = FALSE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, destinataireId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Message mapMessage(ResultSet rs) throws SQLException {
        Message m = new Message();
        m.setId(rs.getInt("id"));
        m.setExpediteurId(rs.getInt("expediteur_id"));
        m.setDestinataireId(rs.getInt("destinataire_id"));
        m.setContenu(rs.getString("contenu"));
        Timestamp date = rs.getTimestamp("date_envoi");
        if (date != null) m.setDateEnvoi(date.toLocalDateTime());
        m.setLu(rs.getBoolean("lu"));
        return m;
    }
}

