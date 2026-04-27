package com.rencontre.controller;

import com.rencontre.dao.MessageDAO;
import com.rencontre.dao.UtilisateurDAO;
import com.rencontre.model.Message;
import com.rencontre.model.Utilisateur;
import com.rencontre.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet pour la messagerie instantanee.
 */
@WebServlet("/app/message")
public class MessageServlet extends HttpServlet {

    private MessageDAO messageDAO = new MessageDAO();
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
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
        if ("conversation".equals(action)) {
            String partnerIdStr = req.getParameter("partnerId");
            if (partnerIdStr == null || partnerIdStr.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/app/message");
                return;
            }
            int partnerId;
            try {
                partnerId = Integer.parseInt(partnerIdStr);
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/app/message");
                return;
            }
            Utilisateur partner = utilisateurDAO.findById(partnerId);
            if (partner == null) {
                req.setAttribute("errorMessage", "Utilisateur introuvable.");
                req.getRequestDispatcher("/WEB-INF/views/messages.jsp").forward(req, resp);
                return;
            }
            List<Message> messages = messageDAO.findConversation(user.getId(), partnerId);
            messageDAO.markConversationAsRead(user.getId(), partnerId);
            req.setAttribute("messages", messages);
            req.setAttribute("partner", partner);
            req.getRequestDispatcher("/WEB-INF/views/messages.jsp").forward(req, resp);
        } else {
            List<Integer> partnerIds = messageDAO.findConversationPartners(user.getId());
            List<Utilisateur> partners = new ArrayList<>();
            for (Integer pid : partnerIds) {
                Utilisateur u = utilisateurDAO.findById(pid);
                if (u != null) {
                    utilisateurDAO.loadRelations(u); // Charger les relations de l'utilisateur
                    partners.add(u);
                }
            }
            req.setAttribute("partners", partners);
            req.getRequestDispatcher("/WEB-INF/views/messages.jsp").forward(req, resp);
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

        String destinataireIdStr = req.getParameter("destinataireId");
        String contenu = req.getParameter("contenu");
        if (destinataireIdStr == null || destinataireIdStr.isEmpty() || contenu == null || contenu.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/app/message");
            return;
        }
        int destinataireId;
        try {
            destinataireId = Integer.parseInt(destinataireIdStr);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/app/message");
            return;
        }

        Message m = new Message();
        m.setExpediteurId(user.getId());
        m.setDestinataireId(destinataireId);
        m.setContenu(contenu);

        messageDAO.create(m);
        
        Utilisateur dest = utilisateurDAO.findById(destinataireId);
        if (dest != null) {
            notificationService.notifyNewMessage(destinataireId, user.getNomComplet());
        }

        resp.sendRedirect(req.getContextPath() + "/app/message?action=conversation&partnerId=" + destinataireId);
    }
}
