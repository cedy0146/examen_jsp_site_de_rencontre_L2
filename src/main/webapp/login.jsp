<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - Coup de Foudre</title>
    <jsp:include page="/WEB-INF/views/includes/head-assets.jsp" />
</head>
<body class="auth-page">
    <div class="auth-container">
        <div class="text-center mb-3">
            <div class="auth-logo"><i class="fa-solid fa-heart"></i></div>
            <h1>Connexion</h1>
            <p class="text-muted">Heureux de vous revoir !</p>
        </div>

        <c:if test="${param.error == '1'}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-triangle-exclamation"></i> Email ou mot de passe incorrect.
            </div>
        </c:if>
        <c:if test="${param.success == '1'}">
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i> Inscription réussie ! Vous pouvez maintenant vous connecter.
            </div>
        </c:if>
        <c:if test="${param.logout == '1'}">
            <div class="alert alert-info">
                <i class="fa-solid fa-circle-info"></i> Vous avez été déconnecté avec succès.
            </div>
        </c:if>

        <form action="${ctx}/login" method="post">
            <div class="form-group">
                <label for="email"><i class="fa-solid fa-envelope"></i> Adresse email</label>
                <input type="email" id="email" name="email" class="form-control" placeholder="votre@email.com" required autofocus>
            </div>
            <div class="form-group">
                <label for="password"><i class="fa-solid fa-lock"></i> Mot de passe</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>
            <button type="submit" class="btn btn-primary btn-block btn-lg"><i class="fa-solid fa-right-to-bracket"></i> Se connecter</button>
        </form>

        <div class="auth-footer">
            <p>Pas encore membre ? <a href="${ctx}/register.jsp"><i class="fa-solid fa-user-plus"></i> Créer un compte</a></p>
            <p><a href="${ctx}/forgot-password"><i class="fa-solid fa-key"></i> Mot de passe oublié ?</a></p>
            <p><a href="${ctx}/index.html"><i class="fa-solid fa-arrow-left"></i> Retour à l'accueil</a></p>
        </div>
    </div>
</body>
</html>
