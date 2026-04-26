<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur - Coup de Foudre</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
</head>
<body class="auth-page">
    <div class="auth-container" style="text-align:center;">
        <div style="font-size:5rem;">&#128533;</div>
        <h1>Oups !</h1>
        <p class="text-muted">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 404}">
                    La page que vous recherchez n'existe pas.
                </c:when>
                <c:when test="${pageContext.errorData.statusCode == 403}">
                    Vous n'avez pas l'autorisation d'accéder à cette page.
                </c:when>
                <c:when test="${pageContext.errorData.statusCode == 500}">
                    Une erreur interne s'est produite. Veuillez réessayer plus tard.
                </c:when>
                <c:otherwise>
                    Une erreur s'est produite. Veuillez réessayer.
                </c:otherwise>
            </c:choose>
        </p>
        <div class="mt-3">
            <a href="${ctx}/index.jsp" class="btn btn-primary">&#127968; Retour à l'accueil</a>
            <a href="${ctx}/app/dashboard" class="btn btn-secondary">Tableau de bord</a>
        </div>
</body>
</html>
