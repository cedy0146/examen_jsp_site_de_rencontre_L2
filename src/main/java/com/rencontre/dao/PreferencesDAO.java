package com.rencontre.dao;

import com.rencontre.model.PreferencesRecherche;
import com.rencontre.util.DBConnection;

import java.sql.*;

/**
 * DAO pour la gestion des préférences de recherche.
 */
public class PreferencesDAO {

    public PreferencesRecherche findById(int id) {
        String sql = "SELECT * FROM preferences_recherche WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapPreferences(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreferencesRecherche findByUtilisateurId(int utilisateurId) {
        String sql = "SELECT * FROM preferences_recherche WHERE utilisateur_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapPreferences(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean create(PreferencesRecherche pr) {
        String sql = "INSERT INTO preferences_recherche (utilisateur_id, age_min, age_max, sexe_recherche, localisation_max_km, type_relation, importance_interets, importance_localisation, importance_age) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pr.getUtilisateurId());
            ps.setInt(2, pr.getAgeMin());
            ps.setInt(3, pr.getAgeMax());
            ps.setString(4, pr.getSexeRecherche());
            ps.setInt(5, pr.getLocalisationMaxKm());
            ps.setString(6, pr.getTypeRelation());
            ps.setInt(7, pr.getImportanceInterets());
            ps.setInt(8, pr.getImportanceLocalisation());
            ps.setInt(9, pr.getImportanceAge());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) pr.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(PreferencesRecherche pr) {
        String sql = "UPDATE preferences_recherche SET age_min=?, age_max=?, sexe_recherche=?, localisation_max_km=?, type_relation=?, importance_interets=?, importance_localisation=?, importance_age=? WHERE utilisateur_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pr.getAgeMin());
            ps.setInt(2, pr.getAgeMax());
            ps.setString(3, pr.getSexeRecherche());
            ps.setInt(4, pr.getLocalisationMaxKm());
            ps.setString(5, pr.getTypeRelation());
            ps.setInt(6, pr.getImportanceInterets());
            ps.setInt(7, pr.getImportanceLocalisation());
            ps.setInt(8, pr.getImportanceAge());
            ps.setInt(9, pr.getUtilisateurId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM preferences_recherche WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private PreferencesRecherche mapPreferences(ResultSet rs) throws SQLException {
        PreferencesRecherche pr = new PreferencesRecherche();
        pr.setId(rs.getInt("id"));
        pr.setUtilisateurId(rs.getInt("utilisateur_id"));
        pr.setAgeMin(rs.getInt("age_min"));
        pr.setAgeMax(rs.getInt("age_max"));
        pr.setSexeRecherche(rs.getString("sexe_recherche"));
        pr.setLocalisationMaxKm(rs.getInt("localisation_max_km"));
        pr.setTypeRelation(rs.getString("type_relation"));
        pr.setImportanceInterets(rs.getInt("importance_interets"));
        pr.setImportanceLocalisation(rs.getInt("importance_localisation"));
        pr.setImportanceAge(rs.getInt("importance_age"));
        return pr;
    }
}

