package com.rencontre.controller;

import com.rencontre.dao.*;
import com.rencontre.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet pour la recherche avancée d'utilisateurs.
 */
@WebServlet("/app/search")
public class SearchServlet extends HttpServlet {

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private CentreInteretDAO centreInteretDAO = new CentreInteretDAO();
    private InteractionDAO interactionDAO = new InteractionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        req.setAttribute("allInterets", centreInteretDAO.findAll());
        req.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        String sexe = req.getParameter("sexe");
        Integer ageMin = parseIntOrNull(req.getParameter("ageMin"));
        Integer ageMax = parseIntOrNull(req.getParameter("ageMax"));
        String localisation = req.getParameter("localisation");
        Integer interetId = parseIntOrNull(req.getParameter("interetId"));

        List<Utilisateur> results = utilisateurDAO.search(sexe, ageMin, ageMax, localisation, interetId);
        // Filtrer les utilisateurs bloqués
        results.removeIf(u -> interactionDAO.isBlocked(user.getId(), u.getId()) || interactionDAO.isBlocked(u.getId(), user.getId()));

        req.setAttribute("results", results);
        req.setAttribute("allInterets", centreInteretDAO.findAll());
        req.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(req, resp);
    }

    private Integer parseIntOrNull(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

