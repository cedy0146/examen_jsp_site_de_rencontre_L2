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

        try {
            // Charger les statistiques
            Statistiques stats = statistiquesDAO.findByUtilisateurId(user.getId());
            req.setAttribute("stats", stats);

            // Charger les suggestions de matchs
            List<Match> suggestions = matchDAO.findSuggestionsForUser(user.getId());
            for (Match m : suggestions) {
                matchDAO.loadUsers(m);
            }
            req.setAttribute("suggestions", suggestions);

            // Charger les matchs acceptés
            List<Match> matches = matchDAO.findAcceptedByUtilisateurId(user.getId());
            for (Match m : matches) {
                matchDAO.loadUsers(m);
            }
            req.setAttribute("matches", matches);

            // Charger les notifications récentes
            List<Notification> notifications = notificationDAO.findByUtilisateurId(user.getId());
            req.setAttribute("notifications", notifications);

            req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Erreur lors du chargement du tableau de bord : " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }
}

