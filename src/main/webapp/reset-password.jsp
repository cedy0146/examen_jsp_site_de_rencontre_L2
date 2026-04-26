<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Réinitialisation du mot de passe - Coup de Foudre</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
</head>
<body class="auth-page">
    <div class="auth-container">
        <div class="text-center mb-3">
            <div style="font-size:3rem;">&#10084;</div>
            <h1>Nouveau mot de passe</h1>
            <p class="text-muted">Choisissez un nouveau mot de passe sécurisé.</p>
        </div>

        <c:if test="${param.error == '1'}">
            <div class="alert alert-danger">
                &#9888; Lien invalide ou expiré.
            </div>
        </c:if>
        <c:if test="${param.error == '2'}">
            <div class="alert alert-danger">
                &#9888; Les mots de passe ne correspondent pas.
            </div>
        </c:if>
        <c:if test="${param.error == '3'}">
            <div class="alert alert-danger">
                &#9888; Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.
            </div>
        </c:if>

        <form action="${ctx}/reset-password" method="post">
            <input type="hidden" name="token" value="${param.token}">
            <div class="form-group">
                <label for="password">Nouveau mot de passe</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>
            <div class="form-group">
                <label for="confirm_password">Confirmer le mot de passe</label>
                <input type="password" id="confirm_password" name="confirm_password" class="form-control" placeholder="••••••••" required>
            </div>
            <button type="submit" class="btn btn-primary btn-block btn-lg">Réinitialiser</button>
        </form>

        <div class="auth-footer">
            <p><a href="${ctx}/login.jsp">&#8592; Retour à la connexion</a></p>
        </div>
    </div>
</body>
</html>

