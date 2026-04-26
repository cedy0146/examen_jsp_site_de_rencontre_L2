package com.rencontre.controller;

import com.rencontre.dao.SignalementDAO;
import com.rencontre.dao.UtilisateurDAO;
import com.rencontre.model.Utilisateur;
import com.rencontre.service.StatistiquesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet pour l'administration.
 */
@WebServlet("/app/admin")
public class AdminServlet extends HttpServlet {

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private SignalementDAO signalementDAO = new SignalementDAO();
    private StatistiquesService statistiquesService = new StatistiquesService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null || !user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        String action = req.getParameter("action");
        if ("users".equals(action)) {
            req.setAttribute("users", utilisateurDAO.findAll());
            req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
        } else if ("stats".equals(action)) {
            req.setAttribute("stats", statistiquesService.getGlobalStats());
            req.getRequestDispatcher("/WEB-INF/views/admin/stats.jsp").forward(req, resp);
        } else if ("reports".equals(action)) {
            req.setAttribute("signalements", signalementDAO.findAll());
            req.getRequestDispatcher("/WEB-INF/views/admin/reports.jsp").forward(req, resp);
        } else {
            req.setAttribute("stats", statistiquesService.getGlobalStats());
            req.setAttribute("users", utilisateurDAO.findAll());
            req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null || !user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        String action = req.getParameter("action");
        int userId = Integer.parseInt(req.getParameter("userId"));

        if ("block".equals(action)) {
            utilisateurDAO.blockUser(userId);
        } else if ("unblock".equals(action)) {
            utilisateurDAO.unblockUser(userId);
        } else if ("delete".equals(action)) {
            utilisateurDAO.delete(userId);
        } else if ("resolveReport".equals(action)) {
            int reportId = Integer.parseInt(req.getParameter("reportId"));
            signalementDAO.updateStatut(reportId, "TRAITE");
            resp.sendRedirect(req.getContextPath() + "/app/admin?action=reports");
            return;
        } else if ("rejectReport".equals(action)) {
            int reportId = Integer.parseInt(req.getParameter("reportId"));
            signalementDAO.updateStatut(reportId, "REJETE");
            resp.sendRedirect(req.getContextPath() + "/app/admin?action=reports");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/app/admin?action=users");
    }
}
