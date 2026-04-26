-- ============================================
-- SITE DE RENCONTRE - SCRIPT SQL COMPLET
-- Base de données : MySQL
-- ============================================

CREATE DATABASE IF NOT EXISTS site_rencontre CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE site_rencontre;

-- ============================================
-- TABLE : utilisateurs
-- ============================================
CREATE TABLE IF NOT EXISTS utilisateurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE NOT NULL,
    sexe ENUM('HOMME', 'FEMME', 'AUTRE') NOT NULL,
    localisation VARCHAR(255),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    photo_profil VARCHAR(255),
    bio TEXT,
    role ENUM('ADMIN', 'UTILISATEUR', 'VIP') DEFAULT 'UTILISATEUR',
    statut ENUM('ACTIF', 'BLOQUE', 'SUPPRIME') DEFAULT 'ACTIF',
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    derniere_connexion TIMESTAMP NULL,
    visibilite ENUM('PUBLIC', 'AMIS', 'PRIVE') DEFAULT 'PUBLIC',
    INDEX idx_email (email),
    INDEX idx_localisation (localisation),
    INDEX idx_role (role)
);

-- ============================================
-- TABLE : centres_interet
-- ============================================
CREATE TABLE IF NOT EXISTS centres_interet (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE,
    categorie VARCHAR(100),
    description TEXT
);

-- ============================================
-- TABLE : utilisateur_interets
-- ============================================
CREATE TABLE IF NOT EXISTS utilisateur_interets (
    utilisateur_id INT NOT NULL,
    interet_id INT NOT NULL,
    PRIMARY KEY (utilisateur_id, interet_id),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    FOREIGN KEY (interet_id) REFERENCES centres_interet(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE : preferences_recherche
-- ============================================
CREATE TABLE IF NOT EXISTS preferences_recherche (
    id INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id INT NOT NULL UNIQUE,
    age_min INT DEFAULT 18,
    age_max INT DEFAULT 99,
    sexe_recherche ENUM('HOMME', 'FEMME', 'AUTRE', 'TOUS') DEFAULT 'TOUS',
    localisation_max_km INT DEFAULT 50,
    type_relation ENUM('AMITIE', 'AMOUREUSE', 'PROFESSIONNELLE', 'TOUS') DEFAULT 'TOUS',
    importance_interets INT DEFAULT 5,
    importance_localisation INT DEFAULT 5,
    importance_age INT DEFAULT 5,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE : abonnements
-- ============================================
CREATE TABLE IF NOT EXISTS abonnements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    type ENUM('GRATUIT', 'PREMIUM', 'VIP') DEFAULT 'GRATUIT',
    date_debut TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_fin TIMESTAMP NULL,
    statut ENUM('ACTIF', 'EXPIRE', 'ANNULE') DEFAULT 'ACTIF',
    prix DECIMAL(10, 2) DEFAULT 0.00,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_utilisateur (utilisateur_id)
);

-- ============================================
-- TABLE : interactions
-- ============================================
CREATE TABLE IF NOT EXISTS interactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    expediteur_id INT NOT NULL,
    destinataire_id INT NOT NULL,
    type ENUM('LIKE', 'CLIN_DOEIL', 'MESSAGE', 'VUE', 'BLOCAGE') NOT NULL,
    contenu TEXT,
    date_interaction TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    lu BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (expediteur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    FOREIGN KEY (destinataire_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_expediteur (expediteur_id),
    INDEX idx_destinataire (destinataire_id),
    INDEX idx_type (type)
);

-- ============================================
-- TABLE : messages
-- ============================================
CREATE TABLE IF NOT EXISTS messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    expediteur_id INT NOT NULL,
    destinataire_id INT NOT NULL,
    contenu TEXT NOT NULL,
    date_envoi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    lu BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (expediteur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    FOREIGN KEY (destinataire_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_conversation (expediteur_id, destinataire_id),
    INDEX idx_date (date_envoi)
);

-- ============================================
-- TABLE : matchs
-- ============================================
CREATE TABLE IF NOT EXISTS matchs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur1_id INT NOT NULL,
    utilisateur2_id INT NOT NULL,
    score_compatibilite DECIMAL(5, 2) DEFAULT 0.00,
    statut ENUM('EN_ATTENTE', 'ACCEPTE', 'REFUSE', 'DEJA_RENCONTRE') DEFAULT 'EN_ATTENTE',
    date_match TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_match (utilisateur1_id, utilisateur2_id),
    FOREIGN KEY (utilisateur1_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    FOREIGN KEY (utilisateur2_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    CHECK (utilisateur1_id < utilisateur2_id)
);

-- ============================================
-- TABLE : notifications
-- ============================================
CREATE TABLE IF NOT EXISTS notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    type ENUM('NOUVEAU_MESSAGE', 'NOUVEAU_MATCH', 'LIKE_RECU', 'VISITE_PROFIL', 'ABONNEMENT_EXPIRE') NOT NULL,
    contenu TEXT NOT NULL,
    lu BOOLEAN DEFAULT FALSE,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_utilisateur_lu (utilisateur_id, lu)
);

-- ============================================
-- TABLE : historique_connexions
-- ============================================
CREATE TABLE IF NOT EXISTS historique_connexions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    ip_adresse VARCHAR(45),
    date_connexion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE : paiements
-- ============================================
CREATE TABLE IF NOT EXISTS paiements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    abonnement_id INT,
    montant DECIMAL(10, 2) NOT NULL,
    methode ENUM('CARTE', 'PAYPAL', 'VIREMENT') DEFAULT 'CARTE',
    statut ENUM('EN_ATTENTE', 'PAYE', 'ECHOUE', 'REMBOURSE') DEFAULT 'EN_ATTENTE',
    date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    FOREIGN KEY (abonnement_id) REFERENCES abonnements(id) ON DELETE SET NULL
);

-- ============================================
-- DONNEES DE TEST
-- ============================================

INSERT INTO centres_interet (nom, categorie, description) VALUES
('Sport', 'Loisirs', 'Activités sportives diverses'),
('Musique', 'Culture', 'Écoute et pratique musicale'),
('Voyages', 'Loisirs', 'Découverte de nouveaux horizons'),
('Cinéma', 'Culture', 'Passion pour le 7ème art'),
('Cuisine', 'Art de vivre', 'Gastronomie et cuisine maison'),
('Lecture', 'Culture', 'Livres et littérature'),
('Photographie', 'Art', 'Prise de photos et retouche'),
('Technologie', 'Science', 'Informatique et nouvelles technologies'),
('Nature', 'Environnement', 'Randonnée et écologie'),
('Jeux vidéo', 'Loisirs', 'Gaming et e-sports');

-- Utilisateur admin
INSERT INTO utilisateurs (email, mot_de_passe, nom, prenom, date_naissance, sexe, localisation, role, bio)
VALUES ('admin@rencontre.com', 'admin123', 'Admin', 'System', '1990-01-01', 'HOMME', 'Paris', 'ADMIN', 'Administrateur du site');

-- Utilisateurs de test
INSERT INTO utilisateurs (email, mot_de_passe, nom, prenom, date_naissance, sexe, localisation, bio) VALUES
('jean.dupont@email.com', 'jean123', 'Dupont', 'Jean', '1992-05-15', 'HOMME', 'Paris', 'Passionné de sport et de voyages'),
('marie.martin@email.com', 'marie123', 'Martin', 'Marie', '1995-08-22', 'FEMME', 'Lyon', 'Amoureuse de la musique et de la nature'),
('pierre.bernard@email.com', 'pierre123', 'Bernard', 'Pierre', '1988-12-03', 'HOMME', 'Marseille', 'Développeur et gamer dans l''âme'),
('sophie.petit@email.com', 'sophie123', 'Petit', 'Sophie', '1993-04-10', 'FEMME', 'Paris', 'Photographe amateur et cinéphile'),
('lucas.moreau@email.com', 'lucas123', 'Moreau', 'Lucas', '1990-11-28', 'HOMME', 'Lyon', 'Chef cuisinier et amateur de lecture');

-- Centres d'intérêt des utilisateurs
INSERT INTO utilisateur_interets (utilisateur_id, interet_id) VALUES
(2, 1), (2, 3), -- Jean : Sport, Voyages
(3, 2), (3, 9), -- Marie : Musique, Nature
(4, 8), (4, 10), -- Pierre : Technologie, Jeux vidéo
(5, 4), (5, 7), -- Sophie : Cinéma, Photographie
(6, 5), (6, 6); -- Lucas : Cuisine, Lecture

-- Préférences de recherche
INSERT INTO preferences_recherche (utilisateur_id, age_min, age_max, sexe_recherche, localisation_max_km, type_relation) VALUES
(2, 20, 35, 'FEMME', 50, 'AMOUREUSE'),
(3, 25, 40, 'HOMME', 100, 'AMOUREUSE'),
(4, 22, 35, 'FEMME', 75, 'TOUS'),
(5, 25, 38, 'HOMME', 50, 'AMOUREUSE'),
(6, 20, 35, 'FEMME', 30, 'AMOUREUSE');

-- Abonnements
INSERT INTO abonnements (utilisateur_id, type, date_fin, prix) VALUES
(2, 'PREMIUM', DATE_ADD(NOW(), INTERVAL 1 MONTH), 9.99),
(3, 'GRATUIT', NULL, 0.00),
(4, 'VIP', DATE_ADD(NOW(), INTERVAL 3 MONTH), 29.99),
(5, 'PREMIUM', DATE_ADD(NOW(), INTERVAL 2 MONTH), 9.99),
(6, 'GRATUIT', NULL, 0.00);

-- Interactions (likes)
INSERT INTO interactions (expediteur_id, destinataire_id, type) VALUES
(2, 3, 'LIKE'),
(3, 2, 'LIKE'),
(4, 5, 'LIKE'),
(5, 4, 'VUE');

-- Matchs
INSERT INTO matchs (utilisateur1_id, utilisateur2_id, score_compatibilite, statut) VALUES
(2, 3, 85.50, 'ACCEPTE'),
(4, 5, 72.00, 'EN_ATTENTE');

-- Messages
INSERT INTO messages (expediteur_id, destinataire_id, contenu, lu) VALUES
(2, 3, 'Bonjour Marie ! J''ai vu que tu aimais la musique, moi aussi je suis passionné par les voyages.', TRUE),
(3, 2, 'Salut Jean ! Oui, j''adore la musique. Tu as déjà voyagé où ?', TRUE),
(2, 3, 'J''ai visité le Japon et l''Italie, c''était incroyable !', FALSE);

