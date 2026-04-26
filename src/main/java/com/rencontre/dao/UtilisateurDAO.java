package com.rencontre.dao;

import com.rencontre.model.Utilisateur;
import com.rencontre.model.CentreInteret;
import com.rencontre.model.Abonnement;
import com.rencontre.model.PreferencesRecherche;
import com.rencontre.util.DBConnection;
import com.rencontre.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des utilisateurs (CRUD complet).
 */
public class UtilisateurDAO {

    public Utilisateur findById(int id) {
        String sql = "SELECT * FROM utilisateurs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUtilisateur(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Utilisateur findByEmail(String email) {
        String sql = "SELECT * FROM utilisateurs WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUtilisateur(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Utilisateur> findAll() {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs WHERE statut != 'SUPPRIME'";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapUtilisateur(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean create(Utilisateur u) {
        String sql = "INSERT INTO utilisateurs (email, mot_de_passe, nom, prenom, date_naissance, sexe, localisation, latitude, longitude, photo_profil, bio, role, statut, visibilite) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getMotDePasse());
            ps.setString(3, u.getNom());
            ps.setString(4, u.getPrenom());
            ps.setDate(5, Date.valueOf(u.getDateNaissance()));
            ps.setString(6, u.getSexe());
            ps.setString(7, u.getLocalisation());
            ps.setObject(8, u.getLatitude());
            ps.setObject(9, u.getLongitude());
            ps.setString(10, u.getPhotoProfil());
            ps.setString(11, u.getBio());
            ps.setString(12, u.getRole() != null ? u.getRole() : "UTILISATEUR");
            ps.setString(13, "ACTIF");
            ps.setString(14, u.getVisibilite() != null ? u.getVisibilite() : "PUBLIC");
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    u.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Utilisateur u) {
        String sql = "UPDATE utilisateurs SET email=?, nom=?, prenom=?, date_naissance=?, sexe=?, localisation=?, latitude=?, longitude=?, photo_profil=?, bio=?, visibilite=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getNom());
            ps.setString(3, u.getPrenom());
            ps.setDate(4, Date.valueOf(u.getDateNaissance()));
            ps.setString(5, u.getSexe());
            ps.setString(6, u.getLocalisation());
            ps.setObject(7, u.getLatitude());
            ps.setObject(8, u.getLongitude());
            ps.setString(9, u.getPhotoProfil());
            ps.setString(10, u.getBio());
            ps.setString(11, u.getVisibilite());
            ps.setInt(12, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE utilisateurs SET mot_de_passe = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "UPDATE utilisateurs SET statut = 'SUPPRIME' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean blockUser(int id) {
        String sql = "UPDATE utilisateurs SET statut = 'BLOQUE' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unblockUser(int id) {
        String sql = "UPDATE utilisateurs SET statut = 'ACTIF' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateLastLogin(int id) {
        String sql = "UPDATE utilisateurs SET derniere_connexion = NOW() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateLastActivity(int id) {
        String sql = "UPDATE utilisateurs SET derniere_activite = NOW() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isOnline(int id) {
        String sql = "SELECT derniere_activite FROM utilisateurs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp ts = rs.getTimestamp("derniere_activite");
                if (ts != null) {
                    long diffMinutes = (System.currentTimeMillis() - ts.getTime()) / (1000 * 60);
                    return diffMinutes <= 5;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void recordVisit(int visiteurId, int visiteId) {
        String sql = "INSERT INTO visites_profil (visiteur_id, visite_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, visiteurId);
            ps.setInt(2, visiteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Utilisateur> findRecentVisitors(int utilisateurId) {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT DISTINCT u.* FROM utilisateurs u JOIN visites_profil v ON u.id = v.visiteur_id WHERE v.visite_id = ? ORDER BY v.date_visite DESC LIMIT 10";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapUtilisateur(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean blockUser(int bloqueurId, int bloqueId) {
        String sql = "INSERT INTO utilisateurs_bloques (bloqueur_id, bloque_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bloqueurId);
            ps.setInt(2, bloqueId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unblockUser(int bloqueurId, int bloqueId) {
        String sql = "DELETE FROM utilisateurs_bloques WHERE bloqueur_id = ? AND bloque_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bloqueurId);
            ps.setInt(2, bloqueId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isBlockedBy(int utilisateurId, int bloqueurId) {
        String sql = "SELECT 1 FROM utilisateurs_bloques WHERE bloqueur_id = ? AND bloque_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bloqueurId);
            ps.setInt(2, utilisateurId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAccount(int id) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Utilisateur> searchWithDistance(String sexe, Integer ageMin, Integer ageMax, String localisation, Integer interetId, Double lat, Double lon, Integer distanceMax) {
        List<Utilisateur> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT DISTINCT u.* FROM utilisateurs u WHERE u.statut = 'ACTIF' AND u.role != 'ADMIN'");
        List<Object> params = new ArrayList<>();

        if (sexe != null && !sexe.isEmpty()) {
            sql.append(" AND u.sexe = ?");
            params.add(sexe);
        }
        if (ageMin != null) {
            sql.append(" AND TIMESTAMPDIFF(YEAR, u.date_naissance, CURDATE()) >= ?");
            params.add(ageMin);
        }
        if (ageMax != null) {
            sql.append(" AND TIMESTAMPDIFF(YEAR, u.date_naissance, CURDATE()) <= ?");
            params.add(ageMax);
        }
        if (localisation != null && !localisation.isEmpty()) {
            sql.append(" AND u.localisation LIKE ?");
            params.add("%" + localisation + "%");
        }
        if (interetId != null) {
            sql.append(" AND EXISTS (SELECT 1 FROM utilisateur_interets ui WHERE ui.utilisateur_id = u.id AND ui.interet_id = ?)");
            params.add(interetId);
        }
        if (lat != null && lon != null && distanceMax != null) {
            sql.append(" AND (6371 * ACOS(COS(RADIANS(?)) * COS(RADIANS(u.latitude)) * COS(RADIANS(u.longitude) - RADIANS(?)) + SIN(RADIANS(?)) * SIN(RADIANS(u.latitude)))) <= ?");
            params.add(lat);
            params.add(lon);
            params.add(lat);
            params.add(distanceMax);
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapUtilisateur(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public List<Utilisateur> search(String sexe, Integer ageMin, Integer ageMax, String localisation, Integer interetId) {
        List<Utilisateur> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT DISTINCT u.* FROM utilisateurs u WHERE u.statut = 'ACTIF' AND u.role != 'ADMIN'");
        List<Object> params = new ArrayList<>();

        if (sexe != null && !sexe.isEmpty()) {
            sql.append(" AND u.sexe = ?");
            params.add(sexe);
        }
        if (ageMin != null) {
            sql.append(" AND TIMESTAMPDIFF(YEAR, u.date_naissance, CURDATE()) >= ?");
            params.add(ageMin);
        }
        if (ageMax != null) {
            sql.append(" AND TIMESTAMPDIFF(YEAR, u.date_naissance, CURDATE()) <= ?");
            params.add(ageMax);
        }
        if (localisation != null && !localisation.isEmpty()) {
            sql.append(" AND u.localisation LIKE ?");
            params.add("%" + localisation + "%");
        }
        if (interetId != null) {
            sql.append(" AND EXISTS (SELECT 1 FROM utilisateur_interets ui WHERE ui.utilisateur_id = u.id AND ui.interet_id = ?)");
            params.add(interetId);
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapUtilisateur(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Utilisateur> findAllExcept(int excludeId) {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs WHERE id != ? AND statut = 'ACTIF' AND role != 'ADMIN'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, excludeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapUtilisateur(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean authenticate(String email, String password) {
        Utilisateur u = findByEmail(email);
        if (u == null) return false;
        return PasswordUtil.verifyPassword(password, u.getMotDePasse());
    }

    public void loadRelations(Utilisateur u) {
        u.setInterets(new CentreInteretDAO().findByUtilisateurId(u.getId()));
        u.setPreferences(new PreferencesDAO().findByUtilisateurId(u.getId()));
        u.setAbonnement(new AbonnementDAO().findActiveByUtilisateurId(u.getId()));
    }

    private Utilisateur mapUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setId(rs.getInt("id"));
        u.setEmail(rs.getString("email"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setDateNaissance(rs.getDate("date_naissance").toLocalDate());
        u.setSexe(rs.getString("sexe"));
        u.setLocalisation(rs.getString("localisation"));
        u.setLatitude((Double) rs.getObject("latitude"));
        u.setLongitude((Double) rs.getObject("longitude"));
        u.setPhotoProfil(rs.getString("photo_profil"));
        u.setBio(rs.getString("bio"));
        u.setRole(rs.getString("role"));
        u.setStatut(rs.getString("statut"));
        u.setDateInscription(rs.getTimestamp("date_inscription").toLocalDateTime());
        Timestamp last = rs.getTimestamp("derniere_connexion");
        if (last != null) u.setDerniereConnexion(last.toLocalDateTime());
        Timestamp activity = rs.getTimestamp("derniere_activite");
        if (activity != null) u.setDerniereActivite(activity.toLocalDateTime());
        u.setVisibilite(rs.getString("visibilite"));
        return u;
    }
}

