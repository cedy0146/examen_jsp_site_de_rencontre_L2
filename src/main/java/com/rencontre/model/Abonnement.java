package com.rencontre.model;

import java.time.LocalDateTime;

/**
 * Modèle représentant un abonnement utilisateur.
 */
public class Abonnement {
    private int id;
    private int utilisateurId;
    private String type;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String statut;
    private double prix;
    
    public Abonnement() {}
    
    public boolean isActif() {
        if (dateFin == null) return "ACTIF".equals(statut);
        return "ACTIF".equals(statut) && LocalDateTime.now().isBefore(dateFin);
    }
    
    public boolean isPremiumOrVip() {
        return "PREMIUM".equals(type) || "VIP".equals(type);
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public LocalDateTime getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }
    
    /**
     * Retourne la date de début sous forme de java.util.Date pour la compatibilité JSTL 1.2.
     */
    public java.util.Date getDateDebutDate() {
        if (dateDebut == null) return null;
        return java.sql.Timestamp.valueOf(dateDebut);
    }
    
    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }
    
    /**
     * Retourne la date de fin sous forme de java.util.Date pour la compatibilité JSTL 1.2.
     */
    public java.util.Date getDateFinDate() {
        if (dateFin == null) return null;
        return java.sql.Timestamp.valueOf(dateFin);
    }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
}

