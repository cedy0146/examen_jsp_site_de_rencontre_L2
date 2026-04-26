package com.rencontre.model;

import java.time.LocalDateTime;

/**
 * Modèle représentant un signalement utilisateur.
 */
public class Signalement {
    private int id;
    private int signalantId;
    private int signaleId;
    private String motif;
    private String description;
    private String statut;
    private LocalDateTime dateSignalement;
    
    private Utilisateur signalant;
    private Utilisateur signale;
    
    public Signalement() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getSignalantId() { return signalantId; }
    public void setSignalantId(int signalantId) { this.signalantId = signalantId; }
    
    public int getSignaleId() { return signaleId; }
    public void setSignaleId(int signaleId) { this.signaleId = signaleId; }
    
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public LocalDateTime getDateSignalement() { return dateSignalement; }
    public void setDateSignalement(LocalDateTime dateSignalement) { this.dateSignalement = dateSignalement; }
    
    public Utilisateur getSignalant() { return signalant; }
    public void setSignalant(Utilisateur signalant) { this.signalant = signalant; }
    
    public Utilisateur getSignale() { return signale; }
    public void setSignale(Utilisateur signale) { this.signale = signale; }
}

