package com.rencontre.service;

import com.rencontre.dao.*;
import com.rencontre.model.*;

import java.util.*;

/**
 * Service pour le calcul de compatibilité entre utilisateurs.
 */
public class MatchingService {

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private MatchDAO matchDAO = new MatchDAO();
    private InteractionDAO interactionDAO = new InteractionDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();

    /**
     * Calcule le score de compatibilité entre deux utilisateurs.
     * +20 pts si même localisation
     * +10 pts par centre d'intérêt commun
     * +5 pts si dans la tranche d'âge
     * Déclaration automatique "déjà rencontré" si interactions existent.
     */
    public double calculateScore(Utilisateur u1, Utilisateur u2) {
        double score = 0;

        // Localisation
        if (u1.getLocalisation() != null && u1.getLocalisation().equalsIgnoreCase(u2.getLocalisation())) {
            score += 20;
        }

        // Centres d'intérêt communs
        List<CentreInteret> interets1 = u1.getInterets();
        List<CentreInteret> interets2 = u2.getInterets();
        if (interets1 != null && interets2 != null) {
            for (CentreInteret ci1 : interets1) {
                for (CentreInteret ci2 : interets2) {
                    if (ci1.getId() == ci2.getId()) {
                        score += 10;
                    }
                }
            }
        }

        // Tranche d'âge
        PreferencesRecherche pref1 = u1.getPreferences();
        if (pref1 != null) {
            int ageU2 = u2.getAge();
            if (ageU2 >= pref1.getAgeMin() && ageU2 <= pref1.getAgeMax()) {
                score += 5;
            }
        }

        // Vérification "déjà rencontré" via interactions
        if (interactionDAO.existsLike(u1.getId(), u2.getId()) || interactionDAO.existsLike(u2.getId(), u1.getId())) {
            score += 2; // léger bonus pour interaction passée
        }

        return Math.min(score, 100);
    }

    /**
     * Génère les suggestions de matchs pour un utilisateur.
     */
    public List<Match> generateSuggestions(int utilisateurId) {
        Utilisateur current = utilisateurDAO.findById(utilisateurId);
        if (current == null) return new ArrayList<>();
        utilisateurDAO.loadRelations(current);

        List<Utilisateur> candidates = utilisateurDAO.findAllExcept(utilisateurId);
        List<Match> suggestions = new ArrayList<>();

        for (Utilisateur candidate : candidates) {
            // Vérifier blocage
            if (interactionDAO.isBlocked(utilisateurId, candidate.getId()) ||
                interactionDAO.isBlocked(candidate.getId(), utilisateurId)) {
                continue;
            }

            // Vérifier préférences
            if (!matchesPreferences(current, candidate)) {
                continue;
            }

            // Vérifier si match existe déjà
            Match existing = matchDAO.findByUsers(utilisateurId, candidate.getId());
            if (existing != null) {
                continue;
            }

            utilisateurDAO.loadRelations(candidate);
            double score = calculateScore(current, candidate);

            // Ne suggérer que si score > 15
            if (score >= 15) {
                Match m = new Match();
                m.setUtilisateur1Id(utilisateurId);
                m.setUtilisateur2Id(candidate.getId());
                m.setScoreCompatibilite(score);
                m.setStatut("EN_ATTENTE");

                matchDAO.create(m);
                suggestions.add(m);

                // Notification
                notificationDAO.create(new Notification(
                    candidate.getId(),
                    Notification.NOUVEAU_MATCH,
                    "Nouvelle suggestion de compatibilité avec " + current.getNomComplet()
                ));
            }
        }
        return suggestions;
    }

    /**
     * Récupère les matchs acceptés pour un utilisateur.
     */
    public List<Match> getAcceptedMatches(int utilisateurId) {
        List<Match> matches = matchDAO.findAcceptedByUtilisateurId(utilisateurId);
        for (Match m : matches) {
            matchDAO.loadUsers(m);
        }
        return matches;
    }

    /**
     * Récupère les suggestions en attente pour un utilisateur.
     */
    public List<Match> getPendingSuggestions(int utilisateurId) {
        List<Match> matches = matchDAO.findSuggestionsForUser(utilisateurId);
        for (Match m : matches) {
            matchDAO.loadUsers(m);
        }
        return matches;
    }

    /**
     * Accepte un match (mutual like).
     */
    public boolean acceptMatch(int matchId) {
        Match m = matchDAO.findById(matchId);
        if (m == null) return false;

        boolean ok = matchDAO.acceptMatch(matchId);
        if (ok) {
            int otherId = (m.getUtilisateur1Id() == m.getUtilisateur2Id()) ? m.getUtilisateur2Id() : m.getUtilisateur1Id();
            // Notifier les deux
            notificationDAO.create(new Notification(
                m.getUtilisateur1Id(),
                Notification.NOUVEAU_MATCH,
                "Un match a été accepté !"
            ));
            notificationDAO.create(new Notification(
                m.getUtilisateur2Id(),
                Notification.NOUVEAU_MATCH,
                "Un match a été accepté !"
            ));
        }
        return ok;
    }

    public boolean refuseMatch(int matchId) {
        return matchDAO.refuseMatch(matchId);
    }

    public boolean markAsDejaRencontre(int matchId) {
        return matchDAO.markAsDejaRencontre(matchId);
    }

    private boolean matchesPreferences(Utilisateur u, Utilisateur candidate) {
        PreferencesRecherche pref = u.getPreferences();
        if (pref == null) return true;

        // Sexe
        if (!"TOUS".equals(pref.getSexeRecherche()) && !pref.getSexeRecherche().equals(candidate.getSexe())) {
            return false;
        }

        // Âge
        int age = candidate.getAge();
        if (age < pref.getAgeMin() || age > pref.getAgeMax()) {
            return false;
        }

        return true;
    }
}

