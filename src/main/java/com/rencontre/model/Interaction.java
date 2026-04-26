package com.rencontre.model;

import java.time.LocalDateTime;

/**
 * Modèle représentant une interaction entre utilisateurs (like, clin d'oeil, etc.).
 */
public class Interaction {
    private int id;
    private int expediteurId;
    private int destinataireId;
    private String type;
    private String contenu;
    private LocalDateTime dateInteraction;
    private boolean lu;
    
    private Utilisateur expediteur;
    private Utilisateur destinataire;
    
    public static final String LIKE = "LIKE";
    public static final String CLIN_DOEIL = "CLIN_DOEIL";
    public static final String MESSAGE = "MESSAGE";
    public static final String VUE = "VUE";
    public static final String BLOCAGE = "BLOCAGE";
    
    public Interaction() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getExpediteurId() { return expediteurId; }
    public void setExpediteurId(int expediteurId) { this.expediteurId = expediteurId; }
    
    public int getDestinataireId() { return destinataireId; }
    public void setDestinataireId(int destinataireId) { this.destinataireId = destinataireId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    
    public LocalDateTime getDateInteraction() { return dateInteraction; }
    public void setDateInteraction(LocalDateTime dateInteraction) { this.dateInteraction = dateInteraction; }
    
    public boolean isLu() { return lu; }
    public void setLu(boolean lu) { this.lu = lu; }
    
    public Utilisateur getExpediteur() { return expediteur; }
    public void setExpediteur(Utilisateur expediteur) { this.expediteur = expediteur; }
    
    public Utilisateur getDestinataire() { return destinataire; }
    public void setDestinataire(Utilisateur destinataire) { this.destinataire = destinataire; }
}

