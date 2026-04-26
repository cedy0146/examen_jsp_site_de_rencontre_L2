package com.rencontre.service;

import com.rencontre.dao.StatistiquesDAO;
import com.rencontre.model.Statistiques;

/**
 * Service pour les statistiques.
 */
public class StatistiquesService {

    private StatistiquesDAO statistiquesDAO = new StatistiquesDAO();

    public Statistiques getUserStats(int utilisateurId) {
        return statistiquesDAO.findByUtilisateurId(utilisateurId);
    }

    public Statistiques getGlobalStats() {
        return statistiquesDAO.findGlobalStats();
    }
}

