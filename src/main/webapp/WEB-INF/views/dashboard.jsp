<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Tableau de Bord" />
</jsp:include>

<div class="page-header">
    <h1>Bonjour, ${user.prenom} ! &#128075;</h1>
    <p>Voici ce qui se passe sur votre compte aujourd'hui.</p>
</div>

<!-- Stats rapides -->
<div class="grid grid-4 mb-3">
    <div class="stat-card">
        <div class="stat-icon">&#128150;</div>
        <div class="stat-value">${stats != null ? stats.nombreMatchs : 0}</div>
        <div class="stat-label">Matchs</div>
    </div>
    <div class="stat-card">
        <div class="stat-icon">&#128172;</div>
        <div class="stat-value">${stats != null ? stats.nombreMessages : 0}</div>
        <div class="stat-label">Messages</div>
    </div>
    <div class="stat-card">
        <div class="stat-icon">&#128065;</div>
        <div class="stat-value">${stats != null ? stats.nombreVues : 0}</div>
        <div class="stat-label">Vues profil</div>
    </div>
    <div class="stat-card">
        <div class="stat-icon">&#128276;</div>
        <div class="stat-value">${stats != null ? stats.nombreLikes : 0}</div>
        <div class="stat-label">Likes reçus</div>
    </div>
</div>

<div class="grid grid-2">
    <!-- Suggestions de matchs -->
    <div class="card">
        <div class="card-header">
            <h2 class="card-title">&#128161; Suggestions pour vous</h2>
            <a href="${ctx}/app/match?action=suggestions" class="btn btn-sm btn-outline-primary">Voir tout</a>
        </div>
        <c:choose>
            <c:when test="${not empty suggestions}">
                <div class="grid grid-2">
                    <c:forEach var="match" items="${suggestions}" varStatus="status">
                        <c:if test="${status.index < 4}">
                            <div class="profile-card">
                                <c:set var="other" value="${match.utilisateur1Id == user.id ? match.utilisateur2 : match.utilisateur1}" />
                                <img src="${other.photoProfil != null ? other.photoProfil : ctx.concat('/assets/images/default-avatar.png')}" 
                                     alt="${other.prenom}" class="profile-photo" 
                                     onerror="this.src='${ctx}/assets/images/default-avatar.png'">
                                <div class="profile-name">${other.prenom}, ${other.age} ans</div>
                                <div class="profile-info">${other.localisation}</div>
                                <div class="match-score ${match.scoreCompatibilite >= 70 ? 'high' : match.scoreCompatibilite >= 40 ? 'medium' : 'low'}">
                                    ${match.scoreCompatibilite}%
                                </div>
                                <div class="profile-actions">
                                    <form action="${ctx}/app/match" method="post" style="display:inline;">
                                        <input type="hidden" name="matchId" value="${match.id}">
                                        <button type="submit" name="action" value="accept" class="btn btn-success btn-sm">&#128077; Like</button>
                                    </form>
                                    <form action="${ctx}/app/match" method="post" style="display:inline;">
                                        <input type="hidden" name="matchId" value="${match.id}">
                                        <button type="submit" name="action" value="refuse" class="btn btn-secondary btn-sm">&#10060;</button>
                                    </form>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon">&#128161;</div>
                    <p>Aucune suggestion pour le moment.</p>
                    <a href="${ctx}/app/match?action=suggestions" class="btn btn-primary btn-sm">Générer des suggestions</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Matchs acceptés -->
    <div class="card">
        <div class="card-header">
            <h2 class="card-title">&#128149; Vos matchs</h2>
            <a href="${ctx}/app/match?action=accepted" class="btn btn-sm btn-outline-primary">Voir tout</a>
        </div>
        <c:choose>
            <c:when test="${not empty matches}">
                <div class="grid grid-2">
                    <c:forEach var="match" items="${matches}" varStatus="status">
                        <c:if test="${status.index < 4}">
                            <div class="profile-card">
                                <c:set var="other" value="${match.utilisateur1Id == user.id ? match.utilisateur2 : match.utilisateur1}" />
                                <img src="${other.photoProfil != null ? other.photoProfil : ctx.concat('/assets/images/default-avatar.png')}" 
                                     alt="${other.prenom}" class="profile-photo"
                                     onerror="this.src='${ctx}/assets/images/default-avatar.png'">
                                <div class="profile-name">${other.prenom}</div>
                                <div class="profile-info">${other.localisation}</div>
                                <div class="profile-actions">
                                    <a href="${ctx}/app/message?action=conversation&partnerId=${other.id}" class="btn btn-primary btn-sm">&#128172; Message</a>
                                    <a href="${ctx}/app/profile?action=view&id=${other.id}" class="btn btn-outline-primary btn-sm">Profil</a>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon">&#128148;</div>
                    <p>Vous n'avez pas encore de matchs.</p>
                    <a href="${ctx}/app/search" class="btn btn-primary btn-sm">Rechercher</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Notifications récentes -->
<div class="card">
    <div class="card-header">
        <h2 class="card-title">&#128276; Notifications récentes</h2>
        <a href="${ctx}/app/notifications" class="btn btn-sm btn-outline-primary">Voir tout</a>
    </div>
    <c:choose>
        <c:when test="${not empty notifications}">
            <c:forEach var="notif" items="${notifications}" varStatus="status">
                <c:if test="${status.index < 5}">
                    <div class="notification-item ${notif.lu ? '' : 'unread'}">
                        <div class="notification-icon" style="background: ${notif.type == 'NOUVEAU_MESSAGE' ? '#d1ecf1' : notif.type == 'NOUVEAU_MATCH' ? '#d4edda' : notif.type == 'LIKE_RECU' ? '#f8d7da' : '#fff3cd'};">
                            ${notif.type == 'NOUVEAU_MESSAGE' ? '&#128172;' : notif.type == 'NOUVEAU_MATCH' ? '&#128150;' : notif.type == 'LIKE_RECU' ? '&#128077;' : '&#128065;'}
                        </div>
                        <div class="notification-content">
                            <p>${notif.contenu}</p>
                            <span class="notification-time"><fmt:formatDate value="${notif.dateCreation}" pattern="dd/MM/yyyy HH:mm"/></span>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <p>Aucune notification récente.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

