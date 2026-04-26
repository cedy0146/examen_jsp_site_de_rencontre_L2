


# Plan de création des interfaces JSP pour Coup de Foudre

## Composants partagés
- [x] `header.jsp` - Navigation conditionnelle
- [x] `footer.jsp` - Pied de page
- [x] `style.css` - Design system complet

## Pages publiques
- [x] `index.jsp` - Page d'accueil moderne
- [x] `login.jsp` - Connexion
- [x] `register.jsp` - Inscription

## Pages utilisateur connecté
- [x] `dashboard.jsp` - Tableau de bord avec suggestions
- [x] `profile.jsp` - Affichage du profil
- [x] `profile-edit.jsp` - Édition du profil + préférences (CRUD)
- [x] `profile-view.jsp` - Visualisation d'un profil tiers
- [x] `search.jsp` - Recherche avancée avec filtres
- [x] `matches.jsp` - Liste des matchs et suggestions
- [x] `messages.jsp` - Messagerie
- [x] `notifications.jsp` - Centre de notifications
- [x] `subscription.jsp` - Gestion des abonnements

## Pages admin
- [x] `admin/dashboard.jsp` - Dashboard admin
- [x] `admin/users.jsp` - Gestion des utilisateurs (CRUD)
- [x] `admin/stats.jsp` - Statistiques

## Pages d'erreur
- [x] `error.jsp` - Page d'erreur 404/500

## Modifications backend
- [x] `AuthServlet.java` - Redirection vers les nouvelles pages JSP
- [x] `web.xml` - Welcome-file vers index.jsp

## Redirections
- [x] Anciens JSP (dashboard.jsp, profile.jsp, search.jsp) redirigent vers les nouvelles pages
