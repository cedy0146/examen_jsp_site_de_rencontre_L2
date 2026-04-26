package com.rencontre.controller;

import com.rencontre.dao.MatchDAO;
import com.rencontre.dao.NotificationDAO;
import com.rencontre.dao.StatistiquesDAO;
import com.rencontre.model.Match;
import com.rencontre.model.Notification;
import com.rencontre.model.Statistiques;
import com.rencontre.model.Utilisateur;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet pour le tableau de bord utilisateur.
 */
@WebServlet("/app/dashboard")
public class DashboardServlet extends HttpServlet {

    private MatchDAO matchDAO = new MatchDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();
    private StatistiquesDAO statistiquesDAO = new StatistiquesDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        // Charger les statistiques
        Statistiques stats = statistiquesDAO.findByUtilisateurId(user.getId());
        req.setAttribute("stats", stats);

        // Charger les suggestions de matchs
        List suggestions = matchDAO.findSuggestionsForUser(user.getId());
        for (Object m : suggestions) {
            matchDAO.loadUsers((Match) m);
        }
        req.setAttribute("suggestions", suggestions);

        // Charger les matchs acceptés
        List matches = matchDAO.findAcceptedByUtilisateurId(user.getId());
        for (Object m : matches) {
            matchDAO.loadUsers((Match) m);
        }
        req.setAttribute("matches", matches);

        // Charger les notifications récentes
        List notifications = notificationDAO.findByUtilisateurId(user.getId());
        req.setAttribute("notifications", notifications);

        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}

