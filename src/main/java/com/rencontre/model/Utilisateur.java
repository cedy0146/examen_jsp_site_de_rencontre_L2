package com.rencontre.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Modèle représentant un utilisateur du site de rencontre.
 */
public class Utilisateur {
    private int id;
    private String email;
    private String motDePasse;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String sexe;
    private String localisation;
    private Double latitude;
    private Double longitude;
    private String photoProfil;
    private String bio;
    private String role;
    private String statut;
    private LocalDateTime dateInscription;
    private LocalDateTime derniereConnexion;
    private LocalDateTime derniereActivite;
    private String visibilite;
    
    private List<CentreInteret> interets = new ArrayList<>();
    private PreferencesRecherche preferences;
    private Abonnement abonnement;
    
    public Utilisateur() {}
    
    public Utilisateur(int id, String email, String nom, String prenom, LocalDate dateNaissance, 
                       String sexe, String localisation, String role) {
        this.id = id;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.localisation = localisation;
        this.role = role;
    }
    
    public int getAge() {
        if (dateNaissance == null) return 0;
        return LocalDate.now().getYear() - dateNaissance.getYear();
    }
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    
    public boolean isVip() {
        return "VIP".equals(role);
    }
    
    public boolean isPremium() {
        return abonnement != null && "PREMIUM".equals(abonnement.getType());
    }
    
    public boolean isActif() {
        return "ACTIF".equals(statut);
    }
    
    public double getDistanceFrom(Utilisateur other) {
        if (this.latitude == null || this.longitude == null || other.latitude == null || other.longitude == null) {
            return -1;
        }
        return com.rencontre.dao.UtilisateurDAO.calculateDistance(this.latitude, this.longitude, other.latitude, other.longitude);
    }
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    
    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public String getPhotoProfil() { return photoProfil; }
    public void setPhotoProfil(String photoProfil) { this.photoProfil = photoProfil; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public LocalDateTime getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }
    
    /**
     * Retourne la date d'inscription sous forme de java.util.Date pour la compatibilité JSTL 1.2.
     */
    public java.util.Date getDateInscriptionDate() {
        if (dateInscription == null) return null;
        return java.sql.Timestamp.valueOf(dateInscription);
    }
    
    public LocalDateTime getDerniereConnexion() { return derniereConnexion; }
    public void setDerniereConnexion(LocalDateTime derniereConnexion) { this.derniereConnexion = derniereConnexion; }
    
    /**
     * Retourne la dernière connexion sous forme de java.util.Date pour la compatibilité JSTL 1.2.
     */
    public java.util.Date getDerniereConnexionDate() {
        if (derniereConnexion == null) return null;
        return java.sql.Timestamp.valueOf(derniereConnexion);
    }
    
    public LocalDateTime getDerniereActivite() { return derniereActivite; }
    public void setDerniereActivite(LocalDateTime derniereActivite) { this.derniereActivite = derniereActivite; }
    
    /**
     * Retourne la dernière activité sous forme de java.util.Date pour la compatibilité JSTL 1.2.
     */
    public java.util.Date getDerniereActiviteDate() {
        if (derniereActivite == null) return null;
        return java.sql.Timestamp.valueOf(derniereActivite);
    }
    
    public String getVisibilite() { return visibilite; }
    public void setVisibilite(String visibilite) { this.visibilite = visibilite; }
    
    public List<CentreInteret> getInterets() { return interets; }
    public void setInterets(List<CentreInteret> interets) { this.interets = interets; }
    public void addInteret(CentreInteret interet) { this.interets.add(interet); }
    
    public PreferencesRecherche getPreferences() { return preferences; }
    public void setPreferences(PreferencesRecherche preferences) { this.preferences = preferences; }
    
    public Abonnement getAbonnement() { return abonnement; }
    public void setAbonnement(Abonnement abonnement) { this.abonnement = abonnement; }
}
