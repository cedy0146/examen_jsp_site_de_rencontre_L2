package com.rencontre.model;

/**
 * Modèle représentant les préférences de recherche d'un utilisateur.
 */
public class PreferencesRecherche {
    private int id;
    private int utilisateurId;
    private int ageMin = 18;
    private int ageMax = 99;
    private String sexeRecherche = "TOUS";
    private int localisationMaxKm = 50;
    private String typeRelation = "TOUS";
    private int importanceInterets = 5;
    private int importanceLocalisation = 5;
    private int importanceAge = 5;
    
    public PreferencesRecherche() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    
    public int getAgeMin() { return ageMin; }
    public void setAgeMin(int ageMin) { this.ageMin = ageMin; }
    
    public int getAgeMax() { return ageMax; }
    public void setAgeMax(int ageMax) { this.ageMax = ageMax; }
    
    public String getSexeRecherche() { return sexeRecherche; }
    public void setSexeRecherche(String sexeRecherche) { this.sexeRecherche = sexeRecherche; }
    
    public int getLocalisationMaxKm() { return localisationMaxKm; }
    public void setLocalisationMaxKm(int localisationMaxKm) { this.localisationMaxKm = localisationMaxKm; }
    
    public String getTypeRelation() { return typeRelation; }
    public void setTypeRelation(String typeRelation) { this.typeRelation = typeRelation; }
    
    public int getImportanceInterets() { return importanceInterets; }
    public void setImportanceInterets(int importanceInterets) { this.importanceInterets = importanceInterets; }
    
    public int getImportanceLocalisation() { return importanceLocalisation; }
    public void setImportanceLocalisation(int importanceLocalisation) { this.importanceLocalisation = importanceLocalisation; }
    
    public int getImportanceAge() { return importanceAge; }
    public void setImportanceAge(int importanceAge) { this.importanceAge = importanceAge; }
}

