<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription - Coup de Foudre</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
</head>
<body class="auth-page">
    <div class="auth-container" style="max-width: 550px;">
        <div class="text-center mb-3">
            <div style="font-size:3rem;">&#10084;</div>
            <h1>Rejoignez-nous</h1>
            <p class="text-muted">Trouvez votre âme sœur dès maintenant</p>
        </div>

        <c:if test="${param.error == '1'}">
            <div class="alert alert-danger">
                &#9888; Une erreur est survenue lors de l'inscription. L'email est peut-être déjà utilisé.
            </div>
        </c:if>

        <form action="${ctx}/register" method="post">
            <div class="form-row">
                <div class="form-group">
                    <label for="prenom">Prénom *</label>
                    <input type="text" id="prenom" name="prenom" class="form-control" placeholder="Jean" required>
                </div>
                <div class="form-group">
                    <label for="nom">Nom *</label>
                    <input type="text" id="nom" name="nom" class="form-control" placeholder="Dupont" required>
                </div>
            </div>

            <div class="form-group">
                <label for="email">Adresse email *</label>
                <input type="email" id="email" name="email" class="form-control" placeholder="jean.dupont@email.com" required>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="password">Mot de passe *</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required minlength="6">
                </div>
                <div class="form-group">
                    <label for="confirm_password">Confirmer *</label>
                    <input type="password" id="confirm_password" name="confirm_password" class="form-control" placeholder="••••••••" required>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="dateNaissance">Date de naissance *</label>
                    <input type="date" id="dateNaissance" name="dateNaissance" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="sexe">Sexe *</label>
                    <select id="sexe" name="sexe" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <option value="HOMME">Homme</option>
                        <option value="FEMME">Femme</option>
                        <option value="AUTRE">Autre</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label for="localisation">Localisation (ville)</label>
                <input type="text" id="localisation" name="localisation" class="form-control" placeholder="Paris, Lyon, Marseille...">
            </div>

            <div class="form-group">
                <label for="bio">Présentation</label>
                <textarea id="bio" name="bio" class="form-control" rows="3" placeholder="Dites-nous en plus sur vous..."></textarea>
            </div>

            <button type="submit" class="btn btn-primary btn-block btn-lg">Créer mon compte</button>
        </form>

        <div class="auth-footer">
            <p>Déjà inscrit ? <a href="${ctx}/login.jsp">Se connecter ici</a></p>
            <p><a href="${ctx}/index.html">&#8592; Retour à l'accueil</a></p>
        </div>
    </div>
</body>
</html>

