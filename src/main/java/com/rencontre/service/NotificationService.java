package com.rencontre.service;

import com.rencontre.dao.NotificationDAO;
import com.rencontre.model.Notification;

import java.util.List;

/**
 * Service pour la gestion des notifications.
 */
public class NotificationService {

    private NotificationDAO notificationDAO = new NotificationDAO();

    public List<Notification> getNotificationsForUser(int utilisateurId) {
        return notificationDAO.findByUtilisateurId(utilisateurId);
    }

    public List<Notification> getUnreadNotifications(int utilisateurId) {
        return notificationDAO.findUnreadByUtilisateurId(utilisateurId);
    }

    public int getUnreadCount(int utilisateurId) {
        return notificationDAO.countUnread(utilisateurId);
    }

    public boolean markAsRead(int notificationId) {
        return notificationDAO.markAsRead(notificationId);
    }

    public boolean markAllAsRead(int utilisateurId) {
        return notificationDAO.markAllAsRead(utilisateurId);
    }

    public boolean notifyNewMessage(int destinataireId, String expediteurNom) {
        return notificationDAO.create(new Notification(
            destinataireId,
            Notification.NOUVEAU_MESSAGE,
            "Nouveau message de " + expediteurNom
        ));
    }

    public boolean notifyNewLike(int destinataireId, String expediteurNom) {
        return notificationDAO.create(new Notification(
            destinataireId,
            Notification.LIKE_RECU,
            expediteurNom + " a aimé votre profil"
        ));
    }

    public boolean notifyProfileView(int destinataireId, String viewerNom) {
        return notificationDAO.create(new Notification(
            destinataireId,
            Notification.VISITE_PROFIL,
            viewerNom + " a visité votre profil"
        ));
    }

public boolean notifyNewMatch(int utilisateurId, String matchNom) {
        return notificationDAO.create(new Notification(
            utilisateurId,
            Notification.NOUVEAU_MATCH,
            "Nouveau match avec " + matchNom
        ));
    }

    public boolean notifyAbonnementExpire(int utilisateurId) {
        return notificationDAO.create(new Notification(
            utilisateurId,
            Notification.ABONNEMENT_EXPIRE,
            "Votre abonnement expire bientôt"
        ));
    }

    public boolean deleteNotification(int id) {
        return notificationDAO.delete(id);
    }
}

