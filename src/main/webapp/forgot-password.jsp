<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mot de passe oublié - Coup de Foudre</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
</head>
<body class="auth-page">
    <div class="auth-container">
        <div class="text-center mb-3">
            <div style="font-size:3rem;">&#10084;</div>
            <h1>Mot de passe oublié</h1>
            <p class="text-muted">Entrez votre email pour réinitialiser votre mot de passe.</p>
        </div>

        <c:if test="${param.error == '1'}">
            <div class="alert alert-danger">
                &#9888; Aucun compte trouvé avec cet email.
            </div>
        </c:if>
        <c:if test="${param.sent == '1'}">
            <div class="alert alert-success">
                &#10004; Un lien de réinitialisation a été envoyé (vérifiez votre email).
            </div>
        </c:if>

        <form action="${ctx}/forgot-password" method="post">
            <div class="form-group">
                <label for="email">Adresse email</label>
                <input type="email" id="email" name="email" class="form-control" placeholder="votre@email.com" required autofocus>
            </div>
            <button type="submit" class="btn btn-primary btn-block btn-lg">Envoyer le lien</button>
        </form>

        <div class="auth-footer">
            <p><a href="${ctx}/login.jsp">&#8592; Retour à la connexion</a></p>
        </div>
    </div>
</body>
</html>

