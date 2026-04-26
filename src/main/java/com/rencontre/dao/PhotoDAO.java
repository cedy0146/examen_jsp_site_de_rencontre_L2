package com.rencontre.dao;

import com.rencontre.model.Photo;
import com.rencontre.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des photos de la galerie utilisateur.
 */
public class PhotoDAO {

    public Photo findById(int id) {
        String sql = "SELECT * FROM photos WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapPhoto(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Photo> findByUtilisateurId(int utilisateurId) {
        List<Photo> list = new ArrayList<>();
        String sql = "SELECT * FROM photos WHERE utilisateur_id = ? ORDER BY ordre, date_ajout";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapPhoto(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(Photo p) {
        String sql = "INSERT INTO photos (utilisateur_id, url, ordre) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getUtilisateurId());
            ps.setString(2, p.getUrl());
            ps.setInt(3, p.getOrdre());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) p.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Photo p) {
        String sql = "UPDATE photos SET url = ?, ordre = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getUrl());
            ps.setInt(2, p.getOrdre());
            ps.setInt(3, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM photos WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteByUtilisateurId(int utilisateurId) {
        String sql = "DELETE FROM photos WHERE utilisateur_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Photo mapPhoto(ResultSet rs) throws SQLException {
        Photo p = new Photo();
        p.setId(rs.getInt("id"));
        p.setUtilisateurId(rs.getInt("utilisateur_id"));
        p.setUrl(rs.getString("url"));
        p.setOrdre(rs.getInt("ordre"));
        Timestamp date = rs.getTimestamp("date_ajout");
        if (date != null) p.setDateAjout(date.toLocalDateTime());
        return p;
    }
}

