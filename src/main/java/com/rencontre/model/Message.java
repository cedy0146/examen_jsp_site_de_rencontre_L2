package com.rencontre.model;

import java.time.LocalDateTime;

/**
 * Modèle représentant un message entre utilisateurs.
 */
public class Message {
    private int id;
    private int expediteurId;
    private int destinataireId;
    private String contenu;
    private LocalDateTime dateEnvoi;
    private boolean lu;
    
    private Utilisateur expediteur;
    private Utilisateur destinataire;
    
    public Message() {}
    
    public Message(int expediteurId, int destinataireId, String contenu) {
        this.expediteurId = expediteurId;
        this.destinataireId = destinataireId;
        this.contenu = contenu;
        this.dateEnvoi = LocalDateTime.now();
        this.lu = false;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getExpediteurId() { return expediteurId; }
    public void setExpediteurId(int expediteurId) { this.expediteurId = expediteurId; }
    
    public int getDestinataireId() { return destinataireId; }
    public void setDestinataireId(int destinataireId) { this.destinataireId = destinataireId; }
    
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    
    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }
    
    /**
     * Retourne la date d'envoi sous forme de java.util.Date pour la compatibilité JSTL 1.2.
     */
    public java.util.Date getDateEnvoiDate() {
        if (dateEnvoi == null) return null;
        return java.sql.Timestamp.valueOf(dateEnvoi);
    }
    
    public boolean isLu() { return lu; }
    public void setLu(boolean lu) { this.lu = lu; }
    
    public Utilisateur getExpediteur() { return expediteur; }
    public void setExpediteur(Utilisateur expediteur) { this.expediteur = expediteur; }
    
    public Utilisateur getDestinataire() { return destinataire; }
    public void setDestinataire(Utilisateur destinataire) { this.destinataire = destinataire; }
}

