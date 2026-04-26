package com.rencontre.controller;

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
            matchingService.acceptMatch(matchId);
        } else if ("refuse".equals(action)) {
            matchingService.refuseMatch(matchId);
        } else if ("dejaRencontre".equals(action)) {
            matchingService.markAsDejaRencontre(matchId);
        }

        resp.sendRedirect(req.getContextPath() + "/app/match");
    }
}
