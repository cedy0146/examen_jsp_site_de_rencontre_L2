# TODO - Correction du bug d'inscription

## Problème
Lors de la création d'un nouveau compte, le message "Une erreur interne s'est produite. Veuillez réessayer plus tard." s'affiche.

## Causes identifiées
1. **Champs obligatoires manquants dans AuthServlet** : `dateNaissance`, `sexe`, `localisation`, `bio` ne sont pas lus depuis le formulaire, mais sont requis par le DAO et la BDD (`date_naissance` NOT NULL).
2. **Double hachage du mot de passe** : `AuthServlet` hache le mot de passe, puis `UtilisateurDAO.create()` le hache à nouveau.
3. **Absence de gestion d'erreurs** : Les exceptions remontent jusqu'à `error.jsp`.

## Plan de correction
- [x] Analyser les fichiers concernés
- [x] Modifier `AuthServlet.java` pour lire tous les champs du formulaire
- [x] Modifier `AuthServlet.java` pour parser `dateNaissance` en `LocalDate`
- [x] Modifier `AuthServlet.java` pour passer le mot de passe en clair au DAO
- [x] Modifier `AuthServlet.java` pour ajouter un try-catch avec log
- [x] Modifier `register.jsp` pour ajouter le message d'erreur mots de passe différents
- [x] Recompiler le projet
- [ ] Tester l'inscription

