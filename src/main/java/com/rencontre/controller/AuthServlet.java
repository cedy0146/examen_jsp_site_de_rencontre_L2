package com.rencontre.controller;

import com.rencontre.dao.UtilisateurDAO;
import com.rencontre.model.Utilisateur;
import com.rencontre.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet(urlPatterns = {"/login", "/logout", "/register"})
public class AuthServlet extends HttpServlet {
    
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        
        if ("/login".equals(path)) {
            handleLogin(req, resp);
        } else if ("/register".equals(path)) {
            handleRegister(req, resp);
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
        }
    }
}
