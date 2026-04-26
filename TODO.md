# TODO - Corrections appliquées

## 1. Correction erreur 500 après connexion
- [x] `DashboardServlet.java` : typer les List en `List<Match>`, `List<Notification>`
- [x] `DashboardServlet.java` : try-catch global dans doGet pour logger proprement

## 2. Messagerie directe (envoyer à n'importe qui)
- [x] `messages.jsp` : la zone de chat s'affiche dès qu'un `partner` est défini, même sans messages existants
- [x] `messages.jsp` : ajout d'un formulaire "Nouveau message direct" avec champ ID utilisateur + message
- [x] `messages.jsp` : affichage du vrai nom/prénom du partenaire dans l'en-tête de conversation
- [x] `profile-view.jsp` : le lien Message amène directement à la conversation fonctionnelle

## 3. Règles de complexité mot de passe
- [x] `PasswordUtil.java` : ajouter `isPasswordValid()` et `getPasswordRulesMessage()`
- [x] `AuthServlet.java` : valider la complexité lors de l'inscription (error=3)
- [x] `register.jsp` : afficher le message d'erreur pour error=3
- [x] `register.jsp` : changer minlength de 6 à 8
- [x] `database.sql` : mettre à jour les mots de passe de test avec des mots de passe conformes

---

**Mots de passe de test mis à jour :**
- admin@rencontre.com → `Admin@2026!`
- jean.dupont@email.com → `Jean@Dupont1`
- marie.martin@email.com → `Marie@Martin2`
- pierre.bernard@email.com → `Pierre@Bernard3`
- sophie.petit@email.com → `Sophie@Petit4`
- lucas.moreau@email.com → `Lucas@Moreau5`

