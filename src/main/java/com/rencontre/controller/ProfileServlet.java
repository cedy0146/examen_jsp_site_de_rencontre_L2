package com.rencontre.controller;

import com.rencontre.dao.*;
import com.rencontre.model.*;
import com.rencontre.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servlet pour la gestion du profil utilisateur.
 */
@WebServlet("/app/profile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 25 * 1024 * 1024)
public class ProfileServlet extends HttpServlet {

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private CentreInteretDAO centreInteretDAO = new CentreInteretDAO();
    private PreferencesDAO preferencesDAO = new PreferencesDAO();
    private PhotoDAO photoDAO = new PhotoDAO();
    private SignalementDAO signalementDAO = new SignalementDAO();
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
                // Enregistrer la visite de profil (sauf si on visite son propre profil)
                if (user != null && user.getId() != viewed.getId()) {
                    utilisateurDAO.recordVisit(user.getId(), viewed.getId());
                    notificationService.notifyProfileView(viewed.getId(), user.getNomComplet());
                }
                // Charger les photos et le statut en ligne
                req.setAttribute("photos", photoDAO.findByUtilisateurId(viewed.getId()));
                req.setAttribute("isOnline", utilisateurDAO.isOnline(viewed.getId()));
                req.setAttribute("isBlocked", utilisateurDAO.isBlockedBy(user.getId(), viewed.getId()));
                if (user.getLatitude() != null && user.getLongitude() != null) {
                    double distance = user.getDistanceFrom(viewed);
                    req.setAttribute("distance", distance >= 0 ? String.format("%.1f", distance) : null);
                }
                req.getRequestDispatcher("/WEB-INF/views/profile-view.jsp").forward(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/app/profile");
            }
        } else {
            utilisateurDAO.loadRelations(user);
            req.setAttribute("user", user);
            req.setAttribute("photos", photoDAO.findByUtilisateurId(user.getId()));
            req.setAttribute("recentVisitors", utilisateurDAO.findRecentVisitors(user.getId()));
            req.setAttribute("isOnline", true);
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

            // Gestion upload photo de profil
            Part photoPart = req.getPart("photo");
            if (photoPart != null && photoPart.getSize() > 0) {
                String fileName = Paths.get(photoPart.getSubmittedFileName()).getFileName().toString();
                String uniqueName = UUID.randomUUID().toString() + "_" + fileName;
                String uploadPath = getServletContext().getRealPath("/assets/uploads");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                photoPart.write(uploadPath + File.separator + uniqueName);
                user.setPhotoProfil(req.getContextPath() + "/assets/uploads/" + uniqueName);
            }

            utilisateurDAO.update(user);

            // Gestion upload photos multiples pour la galerie
            List<Part> photoParts = req.getParts().stream()
                .filter(part -> "photos".equals(part.getName()) && part.getSize() > 0)
                .collect(Collectors.toList());
            int ordre = photoDAO.findByUtilisateurId(user.getId()).size();
            for (Part part : photoParts) {
                String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                String uniqueName = UUID.randomUUID().toString() + "_" + fileName;
                String uploadPath = getServletContext().getRealPath("/assets/uploads");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                part.write(uploadPath + File.separator + uniqueName);
                Photo photo = new Photo();
                photo.setUtilisateurId(user.getId());
                photo.setUrl(req.getContextPath() + "/assets/uploads/" + uniqueName);
                photo.setOrdre(ordre++);
                photoDAO.create(photo);
            }

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
        } else if ("report".equals(action)) {
            int signaleId = Integer.parseInt(req.getParameter("signaleId"));
            Signalement s = new Signalement();
            s.setSignalantId(user.getId());
            s.setSignaleId(signaleId);
            s.setMotif(req.getParameter("motif"));
            s.setDescription(req.getParameter("description"));
            signalementDAO.create(s);
            resp.sendRedirect(req.getContextPath() + "/app/profile?action=view&id=" + signaleId + "&reported=1");
        } else if ("deletePhoto".equals(action)) {
            int photoId = Integer.parseInt(req.getParameter("photoId"));
            photoDAO.delete(photoId);
            resp.sendRedirect(req.getContextPath() + "/app/profile?action=edit");
        } else {
            resp.sendRedirect(req.getContextPath() + "/app/profile");
        }
    }
}
