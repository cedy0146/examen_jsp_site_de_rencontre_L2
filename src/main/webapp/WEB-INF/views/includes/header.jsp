<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.pageTitle} - Coup de Foudre</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
</head>
<body>
    <nav class="main-nav">
        <div class="nav-container">
            <a href="${ctx}/index.jsp" class="nav-brand">&#10084; Coup de Foudre</a>
            <div class="nav-links">
                <c:choose>
                    <c:when test="${not empty user}">
                        <a href="${ctx}/app/dashboard" class="nav-link ${param.pageTitle == 'Tableau de Bord' ? 'active' : ''}">&#127968; Accueil</a>
                        <a href="${ctx}/app/search" class="nav-link ${param.pageTitle == 'Recherche' ? 'active' : ''}">&#128269; Recherche</a>
                        <a href="${ctx}/app/match" class="nav-link ${param.pageTitle == 'Matchs' ? 'active' : ''}">&#128150; Matchs</a>
                        <a href="${ctx}/app/message" class="nav-link ${param.pageTitle == 'Messages' ? 'active' : ''}">&#128172; Messages</a>
                        <a href="${ctx}/app/notifications" class="nav-link ${param.pageTitle == 'Notifications' ? 'active' : ''}">&#128276; Notifications</a>
                        <a href="${ctx}/app/profile" class="nav-link ${param.pageTitle == 'Mon Profil' ? 'active' : ''}">&#128100; Profil</a>
                        <c:if test="${user.admin}">
                            <a href="${ctx}/app/admin" class="nav-link ${param.pageTitle == 'Administration' ? 'active' : ''}">&#9881; Admin</a>
                        </c:if>
                        <div class="nav-user">
                            <span class="nav-user-name">${user.prenom}</span>
                            <a href="${ctx}/logout" class="btn btn-sm btn-outline" style="color:white;border-color:rgba(255,255,255,0.5);">Déconnexion</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/login.jsp" class="nav-link">Connexion</a>
                        <a href="${ctx}/register.jsp" class="nav-link">Inscription</a>
                    </c:otherwise>
                </c:choose>
            </div>
    </nav>
    <main class="main-content">
