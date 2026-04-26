package com.rencontre.model;

import java.time.LocalDateTime;

/**
 * Modèle représentant un match entre deux utilisateurs.
 */
public class Match {
    private int id;
    private int utilisateur1Id;
    private int utilisateur2Id;
    private double scoreCompatibilite;
    private String statut;
    private LocalDateTime dateMatch;
    
    private Utilisateur utilisateur1;
    private Utilisateur utilisateur2;
    
    public Match() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUtilisateur1Id() { return utilisateur1Id; }
    public void setUtilisateur1Id(int utilisateur1Id) { this.utilisateur1Id = utilisateur1Id; }
    
    public int getUtilisateur2Id() { return utilisateur2Id; }
    public void setUtilisateur2Id(int utilisateur2Id) { this.utilisateur2Id = utilisateur2Id; }
    
    public double getScoreCompatibilite() { return scoreCompatibilite; }
    public void setScoreCompatibilite(double scoreCompatibilite) { this.scoreCompatibilite = scoreCompatibilite; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public LocalDateTime getDateMatch() { return dateMatch; }
    public void setDateMatch(LocalDateTime dateMatch) { this.dateMatch = dateMatch; }
    
    public Utilisateur getUtilisateur1() { return utilisateur1; }
    public void setUtilisateur1(Utilisateur utilisateur1) { this.utilisateur1 = utilisateur1; }
    
    public Utilisateur getUtilisateur2() { return utilisateur2; }
    public void setUtilisateur2(Utilisateur utilisateur2) { this.utilisateur2 = utilisateur2; }
    
    public boolean isDejaRencontre() {
        return "DEJA_RENCONTRE".equals(statut);
    }
    
    public boolean isAccepte() {
        return "ACCEPTE".equals(statut);
    }
}

