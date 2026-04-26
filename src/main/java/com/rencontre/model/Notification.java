package com.rencontre.model;

import java.time.LocalDateTime;

/**
 * Modèle représentant une notification pour un utilisateur.
 */
public class Notification {
    private int id;
    private int utilisateurId;
    private String type;
    private String contenu;
    private boolean lu;
    private LocalDateTime dateCreation;
    
    public static final String NOUVEAU_MESSAGE = "NOUVEAU_MESSAGE";
    public static final String NOUVEAU_MATCH = "NOUVEAU_MATCH";
    public static final String LIKE_RECU = "LIKE_RECU";
    public static final String VISITE_PROFIL = "VISITE_PROFIL";
    public static final String ABONNEMENT_EXPIRE = "ABONNEMENT_EXPIRE";
    
    public Notification() {}
    
    public Notification(int utilisateurId, String type, String contenu) {
        this.utilisateurId = utilisateurId;
        this.type = type;
        this.contenu = contenu;
        this.lu = false;
        this.dateCreation = LocalDateTime.now();
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    
    public boolean isLu() { return lu; }
    public void setLu(boolean lu) { this.lu = lu; }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}

