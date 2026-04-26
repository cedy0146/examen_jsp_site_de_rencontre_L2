<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - Coup de Foudre</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
</head>
<body class="auth-page">
    <div class="auth-container">
        <div class="text-center mb-3">
            <div style="font-size:3rem;">&#10084;</div>
            <h1>Connexion</h1>
            <p class="text-muted">Heureux de vous revoir !</p>
        </div>

        <c:if test="${param.error == '1'}">
            <div class="alert alert-danger">
                &#9888; Email ou mot de passe incorrect.
            </div>
        </c:if>
        <c:if test="${param.success == '1'}">
            <div class="alert alert-success">
                &#10004; Inscription réussie ! Vous pouvez maintenant vous connecter.
            </div>
        </c:if>
        <c:if test="${param.logout == '1'}">
            <div class="alert alert-info">
                &#8505; Vous avez été déconnecté avec succès.
            </div>
        </c:if>

        <form action="${ctx}/login" method="post">
            <div class="form-group">
                <label for="email">Adresse email</label>
                <input type="email" id="email" name="email" class="form-control" placeholder="votre@email.com" required autofocus>
            </div>
            <div class="form-group">
                <label for="password">Mot de passe</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>
            <button type="submit" class="btn btn-primary btn-block btn-lg">Se connecter</button>
        </form>

        <div class="auth-footer">
            <p>Pas encore membre ? <a href="${ctx}/register.jsp">Créer un compte</a></p>
            <p><a href="${ctx}/forgot-password">Mot de passe oublié ?</a></p>
            <p><a href="${ctx}/index.html">&#8592; Retour à l'accueil</a></p>
        </div>
    </div>
</body>
</html>

