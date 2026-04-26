package com.rencontre.dao;

import com.rencontre.model.CentreInteret;
import com.rencontre.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des centres d'intérêt.
 */
public class CentreInteretDAO {

    public CentreInteret findById(int id) {
        String sql = "SELECT * FROM centres_interet WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapCentreInteret(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CentreInteret> findAll() {
        List<CentreInteret> list = new ArrayList<>();
        String sql = "SELECT * FROM centres_interet ORDER BY nom";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapCentreInteret(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<CentreInteret> findByUtilisateurId(int utilisateurId) {
        List<CentreInteret> list = new ArrayList<>();
        String sql = "SELECT ci.* FROM centres_interet ci JOIN utilisateur_interets ui ON ci.id = ui.interet_id WHERE ui.utilisateur_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapCentreInteret(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(CentreInteret ci) {
        String sql = "INSERT INTO centres_interet (nom, categorie, description) VALUES (?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ci.getNom());
            ps.setString(2, ci.getCategorie());
            ps.setString(3, ci.getDescription());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) ci.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(CentreInteret ci) {
        String sql = "UPDATE centres_interet SET nom=?, categorie=?, description=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ci.getNom());
            ps.setString(2, ci.getCategorie());
            ps.setString(3, ci.getDescription());
            ps.setInt(4, ci.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM centres_interet WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addInteretToUser(int utilisateurId, int interetId) {
        String sql = "INSERT INTO utilisateur_interets (utilisateur_id, interet_id) VALUES (?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ps.setInt(2, interetId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeInteretFromUser(int utilisateurId, int interetId) {
        String sql = "DELETE FROM utilisateur_interets WHERE utilisateur_id = ? AND interet_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ps.setInt(2, interetId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setUserInterets(int utilisateurId, List<Integer> interetIds) {
        String deleteSql = "DELETE FROM utilisateur_interets WHERE utilisateur_id = ?";
        String insertSql = "INSERT INTO utilisateur_interets (utilisateur_id, interet_id) VALUES (?,?)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
                psDel.setInt(1, utilisateurId);
                psDel.executeUpdate();
            }
            try (PreparedStatement psIns = conn.prepareStatement(insertSql)) {
                for (int interetId : interetIds) {
                    psIns.setInt(1, utilisateurId);
                    psIns.setInt(2, interetId);
                    psIns.addBatch();
                }
                psIns.executeBatch();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private CentreInteret mapCentreInteret(ResultSet rs) throws SQLException {
        CentreInteret ci = new CentreInteret();
        ci.setId(rs.getInt("id"));
        ci.setNom(rs.getString("nom"));
        ci.setCategorie(rs.getString("categorie"));
        ci.setDescription(rs.getString("description"));
        return ci;
    }
}

