package com.rencontre.model;

/**
 * Modèle représentant les statistiques d'un utilisateur ou du site.
 */
public class Statistiques {
    private int nombreVues;
    private int nombreLikes;
    private int nombreMessages;
    private int nombreMatchs;
    private double tauxCompatibiliteMoyen;
    private int nombreConnexions;
    private int nombreInteractions;
    
    // Statistiques admin
    private int totalUtilisateurs;
    private int utilisateursActifs;
    private int totalAbonnementsPremium;
    private int totalAbonnementsVip;
    private double revenusTotaux;
    private int nouveauxUtilisateursMois;
    
    public Statistiques() {}
    
    public int getNombreVues() { return nombreVues; }
    public void setNombreVues(int nombreVues) { this.nombreVues = nombreVues; }
    
    public int getNombreLikes() { return nombreLikes; }
    public void setNombreLikes(int nombreLikes) { this.nombreLikes = nombreLikes; }
    
    public int getNombreMessages() { return nombreMessages; }
    public void setNombreMessages(int nombreMessages) { this.nombreMessages = nombreMessages; }
    
    public int getNombreMatchs() { return nombreMatchs; }
    public void setNombreMatchs(int nombreMatchs) { this.nombreMatchs = nombreMatchs; }
    
    public double getTauxCompatibiliteMoyen() { return tauxCompatibiliteMoyen; }
    public void setTauxCompatibiliteMoyen(double tauxCompatibiliteMoyen) { this.tauxCompatibiliteMoyen = tauxCompatibiliteMoyen; }
    
    public int getNombreConnexions() { return nombreConnexions; }
    public void setNombreConnexions(int nombreConnexions) { this.nombreConnexions = nombreConnexions; }
    
    public int getNombreInteractions() { return nombreInteractions; }
    public void setNombreInteractions(int nombreInteractions) { this.nombreInteractions = nombreInteractions; }
    
    public int getTotalUtilisateurs() { return totalUtilisateurs; }
    public void setTotalUtilisateurs(int totalUtilisateurs) { this.totalUtilisateurs = totalUtilisateurs; }
    
    public int getUtilisateursActifs() { return utilisateursActifs; }
    public void setUtilisateursActifs(int utilisateursActifs) { this.utilisateursActifs = utilisateursActifs; }
    
    public int getTotalAbonnementsPremium() { return totalAbonnementsPremium; }
    public void setTotalAbonnementsPremium(int totalAbonnementsPremium) { this.totalAbonnementsPremium = totalAbonnementsPremium; }
    
    public int getTotalAbonnementsVip() { return totalAbonnementsVip; }
    public void setTotalAbonnementsVip(int totalAbonnementsVip) { this.totalAbonnementsVip = totalAbonnementsVip; }
    
    public double getRevenusTotaux() { return revenusTotaux; }
    public void setRevenusTotaux(double revenusTotaux) { this.revenusTotaux = revenusTotaux; }
    
    public int getNouveauxUtilisateursMois() { return nouveauxUtilisateursMois; }
    public void setNouveauxUtilisateursMois(int nouveauxUtilisateursMois) { this.nouveauxUtilisateursMois = nouveauxUtilisateursMois; }
}

