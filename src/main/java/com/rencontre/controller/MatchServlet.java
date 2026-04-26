package com.rencontre.controller;

import com.rencontre.dao.MatchDAO;
import com.rencontre.model.Match;
import com.rencontre.model.Utilisateur;
import com.rencontre.service.MatchingService;
import com.rencontre.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet pour la gestion des matchs et suggestions.
 */
@WebServlet("/app/match")
public class MatchServlet extends HttpServlet {

    private MatchingService matchingService = new MatchingService();
    private NotificationService notificationService = new NotificationService();
    private MatchDAO matchDAO = new MatchDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        String action = req.getParameter("action");
        if ("suggestions".equals(action)) {
            req.setAttribute("suggestions", matchingService.generateSuggestions(user.getId()));
            req.getRequestDispatcher("/WEB-INF/views/matches.jsp").forward(req, resp);
        } else if ("accepted".equals(action)) {
            req.setAttribute("matches", matchingService.getAcceptedMatches(user.getId()));
            req.getRequestDispatcher("/WEB-INF/views/matches.jsp").forward(req, resp);
        } else {
            req.setAttribute("suggestions", matchingService.getPendingSuggestions(user.getId()));
            req.setAttribute("matches", matchingService.getAcceptedMatches(user.getId()));
            req.getRequestDispatcher("/WEB-INF/views/matches.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        String action = req.getParameter("action");
        int matchId = Integer.parseInt(req.getParameter("matchId"));

if ("accept".equals(action)) {
            Match match = matchDAO.findById(matchId);
            if (match != null) {
                matchingService.acceptMatch(matchId);
                // Déterminer l'autre utilisateur pour lui envoyer une notification
                int otherUserId = (match.getUtilisateur1Id() == user.getId()) ? match.getUtilisateur2Id() : match.getUtilisateur1Id();
                notificationService.notifyNewMatch(otherUserId, user.getNomComplet());
            }
        } else if ("refuse".equals(action)) {
            matchingService.refuseMatch(matchId);
        } else if ("dejaRencontre".equals(action)) {
            matchingService.markAsDejaRencontre(matchId);
        }

        resp.sendRedirect(req.getContextPath() + "/app/match");
    }
}
