# TODO - 6 Nouvelles fonctionnalités

## 1. Statut "En ligne" + Derniers visiteurs
- [x] `Utilisateur.java` : ajouter `derniereActivite` (déjà présent)
- [x] `database.sql` : ajouter colonne `derniere_activite TIMESTAMP` (déjà présent)
- [x] `UtilisateurDAO.java` : méthode `updateLastActivity()` (déjà présent)
- [x] `AuthFilter.java` : mettre à jour `derniere_activite` à chaque requête
- [x] `UtilisateurDAO.java` : méthode `isOnline()` (déjà présent)
- [x] Table `visites_profil` (visiteur_id, visite_id, date_visite) (déjà présent)
- [x] `ProfileServlet.java` : enregistrer la visite quand on voit un profil
- [x] `profile.jsp` : afficher "En ligne" / "Hors ligne"
- [x] `profile.jsp` : afficher les derniers visiteurs

## 2. Blocage d'utilisateur + Suppression de compte
- [x] `database.sql` : table `utilisateurs_bloques` (bloqueur_id, bloque_id)
- [x] `UtilisateurDAO.java` : méthodes `blockUser()`, `unblockUser()`, `isBlockedBy()` (déjà présents)
- [x] `InteractionServlet.java` : action blocage/déblocage
- [x] `profile-view.jsp` : bouton Bloquer/Débloquer
- [x] `AuthServlet.java` : action `delete` pour suppression compte
- [x] `profile.jsp` : bouton "Supprimer mon compte" avec confirmation

## 3. Distance géographique
- [x] `Utilisateur.java` : méthode `getDistanceFrom(Utilisateur other)`
- [x] `SearchServlet.java` : filtre par distance (utiliser latitude/longitude)
- [x] `search.jsp` : champ "Distance max (km)"
- [x] `profile-view.jsp` : afficher "À X km de vous"

## 4. Photos multiples (Galerie)
- [x] `database.sql` : table `photos` (id, utilisateur_id, url, ordre, date_ajout) (déjà présent)
- [x] `Photo.java` : modèle
- [x] `PhotoDAO.java` : CRUD
- [x] `ProfileServlet.java` : gérer upload/suppression photos
- [x] `profile-edit.jsp` : upload multiple photos
- [x] `profile-view.jsp` : galerie de photos
- [x] `profile.jsp` : galerie de photos

## 5. Signalement + Modération admin
- [x] `database.sql` : table `signalements` (id, signalant_id, signale_id, motif, description, statut, date) (déjà présent)
- [x] `Signalement.java` : modèle
- [x] `SignalementDAO.java` : CRUD
- [x] `ProfileServlet.java` : action `report`
- [x] `profile-view.jsp` : bouton "Signaler"
- [x] `AdminServlet.java` : page modération signalements
- [x] `admin/reports.jsp` : liste des signalements

## 6. Mot de passe oublié
- [x] `database.sql` : table `reset_tokens` (id, utilisateur_id, token, expiration) (déjà présent)
- [x] `PasswordUtil.java` : générer token sécurisé (déjà présent)
- [x] `AuthServlet.java` : actions `forgot-password`, `reset-password`
- [x] `forgot-password.jsp` : formulaire email
- [x] `reset-password.jsp` : formulaire nouveau mot de passe
- [x] `login.jsp` : lien "Mot de passe oublié ?"

