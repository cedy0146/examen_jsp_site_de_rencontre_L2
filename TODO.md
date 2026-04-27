# TODO - Correction Messagerie

## Problème
La discussion entre utilisateurs ne fonctionne pas. L'affichage des messages plante.

## Cause principale
JSTL 1.2 ne supporte pas `java.time.LocalDateTime`. Le tag `<fmt:formatDate>` échoue sur `msg.dateEnvoi`.

## Plan de correction

- [x] 1. `Message.java` : Ajouter getter `getDateEnvoiDate()` pour compatibilité JSTL
- [x] 2. `messages.jsp` : Utiliser `${msg.dateEnvoiDate}` dans `<fmt:formatDate>`
- [x] 3. `MessageDAO.java` : Fermer proprement le ResultSet de getGeneratedKeys()
- [x] 4. `MessageServlet.java` : Charger objets Utilisateur complets pour la liste des conversations
- [x] 5. `messages.jsp` : Afficher prénom/nom au lieu de "Utilisateur #ID"
- [x] 6. Recompiler et tester

