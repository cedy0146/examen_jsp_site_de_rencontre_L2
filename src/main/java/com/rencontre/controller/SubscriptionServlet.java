package com.rencontre.controller;

import com.rencontre.dao.AbonnementDAO;
import com.rencontre.model.Abonnement;
import com.rencontre.model.Utilisateur;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Servlet pour la gestion des abonnements.
 */
@WebServlet("/app/subscription")
public class SubscriptionServlet extends HttpServlet {

    private AbonnementDAO abonnementDAO = new AbonnementDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        req.setAttribute("abonnements", abonnementDAO.findByUtilisateurId(user.getId()));
        req.getRequestDispatcher("/WEB-INF/views/subscription.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        String type = req.getParameter("type");
        Abonnement a = new Abonnement();
        a.setUtilisateurId(user.getId());
        a.setType(type);

        if ("PREMIUM".equals(type)) {
            a.setDateFin(LocalDateTime.now().plusMonths(1));
            a.setPrix(9.99);
        } else if ("VIP".equals(type)) {
            a.setDateFin(LocalDateTime.now().plusMonths(3));
            a.setPrix(29.99);
        } else {
            a.setDateFin(null);
            a.setPrix(0.0);
        }

        abonnementDAO.create(a);
        resp.sendRedirect(req.getContextPath() + "/app/subscription?success=1");
    }
}
