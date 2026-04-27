package com.rencontre.controller;

import com.rencontre.model.Notification;
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

        // Vérifier si on doit marquer une notification comme lue et rediriger
        String readId = req.getParameter("read");
        if (readId != null && !readId.isEmpty()) {
            try {
                int notificationId = Integer.parseInt(readId);
                Notification notif = notificationService.getNotificationsForUser(user.getId())
                    .stream()
                    .filter(n -> n.getId() == notificationId)
                    .findFirst()
                    .orElse(null);
                
                if (notif != null) {
                    notificationService.markAsRead(notificationId);
                    // Rediriger vers l'URL réelle de la notification
                    String redirectUrl = getRedirectUrl(notif);
                    resp.sendRedirect(req.getContextPath() + redirectUrl);
                    return;
                }
            } catch (NumberFormatException e) {
                // Ignorer et afficher la liste
            }
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

    /**
     * Détermine l'URL de redirection en fonction du type de notification.
     */
    private String getRedirectUrl(Notification notif) {
        switch (notif.getType()) {
            case Notification.NOUVEAU_MESSAGE:
                return "/app/message";
            case Notification.NOUVEAU_MATCH:
                return "/app/match";
            case Notification.LIKE_RECU:
                return "/app/match?action=suggestions";
            case Notification.VISITE_PROFIL:
                return "/app/profile";
            case Notification.ABONNEMENT_EXPIRE:
                return "/app/subscription";
            default:
                return "/app/dashboard";
        }
    }
}
