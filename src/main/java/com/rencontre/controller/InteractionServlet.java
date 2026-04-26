package com.rencontre.controller;

import com.rencontre.dao.InteractionDAO;
import com.rencontre.model.Interaction;
import com.rencontre.model.Utilisateur;
import com.rencontre.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet pour la gestion des interactions (likes, blocages, etc.).
 */
@WebServlet("/app/interaction")
public class InteractionServlet extends HttpServlet {

    private InteractionDAO interactionDAO = new InteractionDAO();
    private NotificationService notificationService = new NotificationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        String action = req.getParameter("action");
        String destinataireIdStr = req.getParameter("destinataireId");
        if (destinataireIdStr == null || destinataireIdStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            return;
        }
        int destinataireId;
        try {
            destinataireId = Integer.parseInt(destinataireIdStr);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            return;
        }

        if ("like".equals(action)) {
            // Vérifier si le like existe déjà
            if (!interactionDAO.existsLike(user.getId(), destinataireId)) {
                Interaction interaction = new Interaction();
                interaction.setExpediteurId(user.getId());
                interaction.setDestinataireId(destinataireId);
                interaction.setType("LIKE");
                interaction.setContenu("A aimé votre profil");
                interactionDAO.create(interaction);
                
                // Envoyer une notification au destinataire
                notificationService.notifyNewLike(destinataireId, user.getNomComplet());
            }
        } else if ("unlike".equals(action)) {
            interactionDAO.deleteLike(user.getId(), destinataireId);
        } else if ("block".equals(action)) {
            Interaction interaction = new Interaction();
            interaction.setExpediteurId(user.getId());
            interaction.setDestinataireId(destinataireId);
            interaction.setType("BLOCAGE");
            interaction.setContenu("A bloqué cet utilisateur");
            interactionDAO.create(interaction);
        }

        // Rediriger vers la page précédente ou le profil
        String redirect = req.getParameter("redirect");
        if (redirect != null && !redirect.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + redirect);
        } else {
            resp.sendRedirect(req.getContextPath() + "/app/profile?action=view&id=" + destinataireId);
        }
    }
}
