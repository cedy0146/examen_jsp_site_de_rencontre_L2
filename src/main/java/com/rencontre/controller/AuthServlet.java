package com.rencontre.controller;

import com.rencontre.dao.UtilisateurDAO;
import com.rencontre.dao.PreferencesDAO;
import com.rencontre.dao.AbonnementDAO;
import com.rencontre.model.Utilisateur;
import com.rencontre.model.PreferencesRecherche;
import com.rencontre.model.Abonnement;
import com.rencontre.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.rencontre.util.DBConnection;

@WebServlet(urlPatterns = {"/login", "/logout", "/register", "/forgot-password", "/reset-password", "/delete-account"})
public class AuthServlet extends HttpServlet {
    
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private PreferencesDAO preferencesDAO = new PreferencesDAO();
    private AbonnementDAO abonnementDAO = new AbonnementDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        
        if ("/login".equals(path)) {
            handleLogin(req, resp);
        } else if ("/register".equals(path)) {
            handleRegister(req, resp);
        } else if ("/forgot-password".equals(path)) {
            handleForgotPassword(req, resp);
        } else if ("/reset-password".equals(path)) {
            handleResetPassword(req, resp);
        } else if ("/delete-account".equals(path)) {
            handleDeleteAccount(req, resp);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Utilisateur user = utilisateurDAO.findByEmail(email);
        
        if (user != null && PasswordUtil.verifyPassword(password, user.getMotDePasse())) {
            HttpSession session = req.getSession();
            session.setAttribute("utilisateur", user);
            utilisateurDAO.updateLastLogin(user.getId());
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=1");
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Vérification des mots de passe
            String password = req.getParameter("password");
            String confirmPassword = req.getParameter("confirm_password");
            if (password == null || !password.equals(confirmPassword)) {
                resp.sendRedirect(req.getContextPath() + "/register.jsp?error=2");
                return;
            }

            // Vérification de la complexité du mot de passe
            if (!PasswordUtil.isPasswordValid(password)) {
                resp.sendRedirect(req.getContextPath() + "/register.jsp?error=3");
                return;
            }

            Utilisateur user = new Utilisateur();
            user.setEmail(req.getParameter("email"));
            user.setNom(req.getParameter("nom"));
            user.setPrenom(req.getParameter("prenom"));
            // Passer le mot de passe en clair, le DAO s'occupe du hachage
            user.setMotDePasse(password);
            user.setRole("UTILISATEUR");
            
            // Lecture des champs supplémentaires obligatoires pour la BDD
            String dateNaissanceStr = req.getParameter("dateNaissance");
            if (dateNaissanceStr != null && !dateNaissanceStr.isEmpty()) {
                user.setDateNaissance(LocalDate.parse(dateNaissanceStr));
            }
            user.setSexe(req.getParameter("sexe"));
            user.setLocalisation(req.getParameter("localisation"));
            user.setBio(req.getParameter("bio"));
            
            if (utilisateurDAO.create(user)) {
                PreferencesRecherche pref = new PreferencesRecherche();
                pref.setUtilisateurId(user.getId());
                preferencesDAO.create(pref);

                Abonnement abonnement = new Abonnement();
                abonnement.setUtilisateurId(user.getId());
                abonnement.setType("GRATUIT");
                abonnement.setStatut("ACTIF");
                abonnement.setPrix(0);
                abonnementDAO.create(abonnement);

                resp.sendRedirect(req.getContextPath() + "/login.jsp?success=1");
            } else {
                resp.sendRedirect(req.getContextPath() + "/register.jsp?error=1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/register.jsp?error=1");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/logout".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/index.jsp?logout=1");
        } else if ("/forgot-password".equals(path)) {
            req.getRequestDispatcher("/forgot-password.jsp").forward(req, resp);
        } else if ("/reset-password".equals(path)) {
            req.setAttribute("token", req.getParameter("token"));
            req.getRequestDispatcher("/reset-password.jsp").forward(req, resp);
        }
    }

    private void handleForgotPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        Utilisateur user = utilisateurDAO.findByEmail(email);
        if (user != null) {
            String token = PasswordUtil.generateSecureToken();
            String sql = "INSERT INTO reset_tokens (utilisateur_id, token, expiration) VALUES (?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, user.getId());
                ps.setString(2, token);
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().plusHours(24)));
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // En production, envoyer un email avec le lien de réinitialisation
            // Pour l'instant, on redirige avec le token en paramètre (démonstration)
            resp.sendRedirect(req.getContextPath() + "/reset-password?token=" + token + "&sent=1");
        } else {
            resp.sendRedirect(req.getContextPath() + "/forgot-password?error=1");
        }
    }

    private void handleResetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getParameter("token");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm_password");

        if (password == null || !password.equals(confirmPassword)) {
            resp.sendRedirect(req.getContextPath() + "/reset-password?token=" + token + "&error=2");
            return;
        }
        if (!PasswordUtil.isPasswordValid(password)) {
            resp.sendRedirect(req.getContextPath() + "/reset-password?token=" + token + "&error=3");
            return;
        }

        String sql = "SELECT utilisateur_id FROM reset_tokens WHERE token = ? AND expiration > NOW() AND utilise = FALSE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("utilisateur_id");
                utilisateurDAO.updatePassword(userId, password);
                // Marquer le token comme utilisé
                try (PreparedStatement ps2 = conn.prepareStatement("UPDATE reset_tokens SET utilise = TRUE WHERE token = ?")) {
                    ps2.setString(1, token);
                    ps2.executeUpdate();
                }
                resp.sendRedirect(req.getContextPath() + "/login.jsp?reset=1");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.sendRedirect(req.getContextPath() + "/reset-password?token=" + token + "&error=1");
    }

    private void handleDeleteAccount(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user != null) {
            utilisateurDAO.deleteAccount(user.getId());
            session.invalidate();
        }
        resp.sendRedirect(req.getContextPath() + "/index.jsp?deleted=1");
    }
}
