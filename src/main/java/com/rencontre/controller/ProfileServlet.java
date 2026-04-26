package com.rencontre.controller;

import com.rencontre.dao.*;
import com.rencontre.model.*;
import com.rencontre.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servlet pour la gestion du profil utilisateur.
 */
@WebServlet("/app/profile")
public class ProfileServlet extends HttpServlet {

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private CentreInteretDAO centreInteretDAO = new CentreInteretDAO();
    private PreferencesDAO preferencesDAO = new PreferencesDAO();
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
        if ("edit".equals(action)) {
            utilisateurDAO.loadRelations(user);
            req.setAttribute("allInterets", centreInteretDAO.findAll());
            req.getRequestDispatcher("/WEB-INF/views/profile-edit.jsp").forward(req, resp);
        } else if ("view".equals(action)) {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/app/profile");
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/app/profile");
                return;
            }
            Utilisateur viewed = utilisateurDAO.findById(id);
            if (viewed != null) {
                utilisateurDAO.loadRelations(viewed);
                req.setAttribute("viewedUser", viewed);
                // Envoyer une notification de visite de profil (sauf si on visite son propre profil)
                if (user != null && user.getId() != viewed.getId()) {
                    notificationService.notifyProfileView(viewed.getId(), user.getNomComplet());
                }
                req.getRequestDispatcher("/WEB-INF/views/profile-view.jsp").forward(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/app/profile");
            }
        } else {
            utilisateurDAO.loadRelations(user);
            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
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
        if ("update".equals(action)) {
            user.setNom(req.getParameter("nom"));
            user.setPrenom(req.getParameter("prenom"));
            user.setEmail(req.getParameter("email"));
            user.setDateNaissance(LocalDate.parse(req.getParameter("dateNaissance")));
            user.setSexe(req.getParameter("sexe"));
            user.setLocalisation(req.getParameter("localisation"));
            user.setBio(req.getParameter("bio"));
            user.setVisibilite(req.getParameter("visibilite"));

            String[] interets = req.getParameterValues("interets");
            if (interets != null) {
                List<Integer> interetIds = Arrays.stream(interets).map(Integer::parseInt).collect(Collectors.toList());
                centreInteretDAO.setUserInterets(user.getId(), interetIds);
            }

            utilisateurDAO.update(user);

            // Mise à jour préférences
            PreferencesRecherche pref = preferencesDAO.findByUtilisateurId(user.getId());
            if (pref == null) {
                pref = new PreferencesRecherche();
                pref.setUtilisateurId(user.getId());
            }
            pref.setAgeMin(Integer.parseInt(req.getParameter("ageMin")));
            pref.setAgeMax(Integer.parseInt(req.getParameter("ageMax")));
            pref.setSexeRecherche(req.getParameter("sexeRecherche"));
            pref.setLocalisationMaxKm(Integer.parseInt(req.getParameter("localisationMaxKm")));
            pref.setTypeRelation(req.getParameter("typeRelation"));

            if (pref.getId() > 0) {
                preferencesDAO.update(pref);
            } else {
                preferencesDAO.create(pref);
            }

            session.setAttribute("utilisateur", utilisateurDAO.findById(user.getId()));
            resp.sendRedirect(req.getContextPath() + "/app/profile?updated=1");
        } else {
            resp.sendRedirect(req.getContextPath() + "/app/profile");
        }
    }
}
