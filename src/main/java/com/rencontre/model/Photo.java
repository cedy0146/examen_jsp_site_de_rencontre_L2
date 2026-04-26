package com.rencontre.model;

import java.time.LocalDateTime;

/**
 * Modèle représentant une photo de la galerie d'un utilisateur.
 */
public class Photo {
    private int id;
    private int utilisateurId;
    private String url;
    private int ordre;
    private LocalDateTime dateAjout;
    
    public Photo() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public int getOrdre() { return ordre; }
    public void setOrdre(int ordre) { this.ordre = ordre; }
    
    public LocalDateTime getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDateTime dateAjout) { this.dateAjout = dateAjout; }
}

