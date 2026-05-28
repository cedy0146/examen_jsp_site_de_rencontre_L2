<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.rencontre.service.NotificationService" %>
<%@ page import="com.rencontre.model.Utilisateur" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />


<%
    int unreadNotifCount = 0;
    Utilisateur currentUser = (Utilisateur) session.getAttribute("utilisateur");
    if (currentUser != null) {
        try {
            unreadNotifCount = new NotificationService().getUnreadCount(currentUser.getId());
        } catch (Exception e) {
            // Si erreur BDD, on affiche 0 sans planter la page
        }
    }
    request.setAttribute("unreadNotifCount", unreadNotifCount);
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.pageTitle} - Coup de Foudre</title>
    <jsp:include page="/WEB-INF/views/includes/head-assets.jsp" />
</head>
<body>
    <nav class="main-nav">
        <div class="nav-container">
            <a href="${ctx}/index.jsp" class="nav-brand">
                <span class="icon-wrap icon-wrap-brand"><i class="fa-solid fa-heart"></i></span>
                Coup de Foudre
            </a>
            <div class="nav-links">
                <c:choose>
                    <c:when test="${not empty user}">
                        <a href="${ctx}/app/dashboard" class="nav-link ${param.pageTitle == 'Tableau de Bord' ? 'active' : ''}">
                            <i class="fa-solid fa-house"></i><span>Accueil</span>
                        </a>
                        <a href="${ctx}/app/search" class="nav-link ${param.pageTitle == 'Recherche' ? 'active' : ''}">
                            <i class="fa-solid fa-magnifying-glass"></i><span>Recherche</span>
                        </a>
                        <a href="${ctx}/app/match" class="nav-link ${param.pageTitle == 'Matchs' ? 'active' : ''}">
                            <i class="fa-solid fa-heart-circle-bolt"></i><span>Matchs</span>
                        </a>
                        <a href="${ctx}/app/message" class="nav-link ${param.pageTitle == 'Messages' ? 'active' : ''}">
                            <i class="fa-solid fa-comment-dots"></i><span>Messages</span>
                        </a>
                        <a href="${ctx}/app/notifications" class="nav-link ${param.pageTitle == 'Notifications' ? 'active' : ''}">
                            <i class="fa-solid fa-bell"></i><span>Notifications</span>
                            <c:if test="${unreadNotifCount > 0}">
                                <span class="badge-notification">${unreadNotifCount}</span>
                            </c:if>
                        </a>
                        <a href="${ctx}/app/profile" class="nav-link ${param.pageTitle == 'Mon Profil' ? 'active' : ''}">
                            <i class="fa-solid fa-user"></i><span>Profil</span>
                        </a>
                        <c:if test="${user.admin}">
                            <a href="${ctx}/app/admin" class="nav-link ${param.pageTitle == 'Administration' ? 'active' : ''}">
                                <i class="fa-solid fa-gear"></i><span>Admin</span>
                            </a>
                        </c:if>
                        <div class="nav-user">
                            <span class="nav-user-name"><i class="fa-solid fa-circle-user"></i> ${user.prenom}</span>
                            <a href="${ctx}/logout" class="btn btn-sm btn-outline nav-logout">
                                <i class="fa-solid fa-right-from-bracket"></i> Déconnexion
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/login.jsp" class="nav-link"><i class="fa-solid fa-right-to-bracket"></i> Connexion</a>
                        <a href="${ctx}/register.jsp" class="nav-link"><i class="fa-solid fa-user-plus"></i> Inscription</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>
    <main class="main-content">