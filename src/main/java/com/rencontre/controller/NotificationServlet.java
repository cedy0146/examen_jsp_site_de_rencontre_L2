package com.rencontre.controller;

import com.rencontre.model.Utilisateur;
import com.rencontre.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet pour la gestion des notifications utilisateur.
 */
@WebServlet("/app/notifications")
public class NotificationServlet extends HttpServlet {

    private NotificationService notificationService = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        req.setAttribute("notifications", notificationService.getNotificationsForUser(user.getId()));
        req.getRequestDispatcher("/WEB-INF/views/notifications.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");

        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = req.getParameter("action");
        if ("readAll".equals(action)) {
            notificationService.markAllAsRead(user.getId());
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            notificationService.deleteNotification(id);
        }

        resp.sendRedirect(req.getContextPath() + "/app/notifications");
    }
}