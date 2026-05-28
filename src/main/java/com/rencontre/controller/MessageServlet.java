package com.rencontre.controller;

import com.rencontre.dao.MessageDAO;
import com.rencontre.dao.UtilisateurDAO;
import com.rencontre.dao.MatchDAO;
import com.rencontre.model.Message;
import com.rencontre.model.Utilisateur;
import com.rencontre.model.Match;
import com.rencontre.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/app/message")
public class MessageServlet extends HttpServlet {

    private MessageDAO messageDAO = new MessageDAO();
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private MatchDAO matchDAO = new MatchDAO();
    private NotificationService notificationService = new NotificationService();

    /**
     * Charge la liste des conversations + dernier message + non-lus.
     * Appelé dans TOUS les cas pour que la liste gauche soit toujours visible.
     */
    private void loadPartners(HttpServletRequest req, int userId) {
        List<Integer> partnerIds = messageDAO.findConversationPartners(userId);
        List<Utilisateur> partners = new ArrayList<>();
        Map<Integer, Message>  lastMessages = new HashMap<>();
        Map<Integer, Integer>  unreadCounts  = new HashMap<>();

        for (Integer pid : partnerIds) {
            Utilisateur u = utilisateurDAO.findById(pid);
            if (u != null) {
                partners.add(u);
                Message last = messageDAO.findLastMessage(userId, pid);
                if (last != null) lastMessages.put(pid, last);
                unreadCounts.put(pid, messageDAO.countUnreadFrom(userId, pid));
            }
        }
        req.setAttribute("partners",      partners);
        req.setAttribute("lastMessages",  lastMessages);
        req.setAttribute("unreadCounts",  unreadCounts);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }

        // Toujours charger la liste (corrige le bug liste vide)
        loadPartners(req, user.getId());

        String action = req.getParameter("action");
        if ("conversation".equals(action)) {
            String partnerIdStr = req.getParameter("partnerId");
            if (partnerIdStr == null) { req.getRequestDispatcher("/WEB-INF/views/messages.jsp").forward(req, resp); return; }
            int partnerId;
            try { partnerId = Integer.parseInt(partnerIdStr); }
            catch (NumberFormatException e) { req.getRequestDispatcher("/WEB-INF/views/messages.jsp").forward(req, resp); return; }

            Utilisateur partner = utilisateurDAO.findById(partnerId);
            if (partner == null) {
                req.setAttribute("errorMessage", "Utilisateur introuvable.");
                req.getRequestDispatcher("/WEB-INF/views/messages.jsp").forward(req, resp); return;
            }
            if (!hasAcceptedMatch(user.getId(), partnerId)) {
                req.setAttribute("errorMessage", "Vous devez avoir un match accepté pour envoyer des messages.");
                req.getRequestDispatcher("/WEB-INF/views/messages.jsp").forward(req, resp); return;
            }

            List<Message> messages = messageDAO.findConversation(user.getId(), partnerId);
            messageDAO.markConversationAsRead(user.getId(), partnerId);
            req.setAttribute("messages", messages);
            req.setAttribute("partner", partner);
        }

        req.getRequestDispatcher("/WEB-INF/views/messages.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }

        String destinataireIdStr = req.getParameter("destinataireId");
        String contenu = req.getParameter("contenu");
        if (destinataireIdStr == null || contenu == null || contenu.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/app/message"); return;
        }
        int destinataireId;
        try { destinataireId = Integer.parseInt(destinataireIdStr); }
        catch (NumberFormatException e) { resp.sendRedirect(req.getContextPath() + "/app/message"); return; }

        if (!hasAcceptedMatch(user.getId(), destinataireId)) {
            resp.sendRedirect(req.getContextPath() + "/app/message?error=match"); return;
        }

        Message m = new Message();
        m.setExpediteurId(user.getId());
        m.setDestinataireId(destinataireId);
        m.setContenu(contenu);
        messageDAO.create(m);
        notificationService.notifyNewMessage(destinataireId, user.getNomComplet());

        resp.sendRedirect(req.getContextPath() + "/app/message?action=conversation&partnerId=" + destinataireId);
    }

    private boolean hasAcceptedMatch(int userId, int partnerId) {
        Match match = matchDAO.findByUsers(userId, partnerId);
        return match != null && "ACCEPTE".equals(match.getStatut());
    }
}
