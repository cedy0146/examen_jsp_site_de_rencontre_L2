<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Tableau de Bord" />
</jsp:include>

<style>
.notif-icon {
    width: 44px; height: 44px;
    border-radius: 13px;
    display: flex; align-items: center; justify-content: center;
    font-size: 1.1rem; flex-shrink: 0;
    box-shadow: 0 2px 8px rgba(0,0,0,0.15);
}
.notif-icon.type-message    { background: linear-gradient(135deg,#0ea5e9,#06b6d4); color:#fff; }
.notif-icon.type-match      { background: linear-gradient(135deg,#ec4899,#f43f5e); color:#fff; }
.notif-icon.type-like       { background: linear-gradient(135deg,#f97316,#ef4444); color:#fff; }
.notif-icon.type-visite     { background: linear-gradient(135deg,#a855f7,#7c3aed); color:#fff; }
.notif-icon.type-abonnement { background: linear-gradient(135deg,#f59e0b,#d97706); color:#fff; }
.notif-icon.type-default    { background: linear-gradient(135deg,#64748b,#475569); color:#fff; }
.notif-unread-dot {
    width: 8px; height: 8px; border-radius: 50%;
    background: var(--indigo); flex-shrink: 0;
    box-shadow: 0 0 6px rgba(79,70,229,0.6);
    margin-left: auto;
}
</style>

<div class="page-header">
    <h1>Bonjour, ${user.prenom} ! <i class="fa-solid fa-hand"></i></h1>
    <p>Voici ce qui se passe sur votre compte aujourd'hui.</p>
</div>

<!-- Stats rapides -->
<div class="grid grid-4 mb-3">
    <div class="stat-card">
        <div class="stat-icon"><i class="fa-solid fa-heart-circle-bolt"></i></div>
        <div class="stat-value">${stats != null ? stats.nombreMatchs : 0}</div>
        <div class="stat-label">Matchs</div>
    </div>
    <div class="stat-card">
        <div class="stat-icon"><i class="fa-solid fa-comment-dots"></i></div>
        <div class="stat-value">${stats != null ? stats.nombreMessages : 0}</div>
        <div class="stat-label">Messages</div>
    </div>
    <div class="stat-card">
        <div class="stat-icon"><i class="fa-solid fa-eye"></i></div>
        <div class="stat-value">${stats != null ? stats.nombreVues : 0}</div>
        <div class="stat-label">Vues profil</div>
    </div>
    <div class="stat-card">
        <div class="stat-icon"><i class="fa-solid fa-bell"></i></div>
        <div class="stat-value">${stats != null ? stats.nombreLikes : 0}</div>
        <div class="stat-label">Likes reçus</div>
    </div>
</div>

<div class="grid grid-2">
    <!-- Suggestions de matchs -->
    <div class="card">
        <div class="card-header">
            <h2 class="card-title"><i class="fa-solid fa-lightbulb"></i> Suggestions pour vous</h2>
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
                                        <button type="submit" name="action" value="accept" class="btn btn-success btn-sm"><i class="fa-solid fa-thumbs-up"></i> Like</button>
                                    </form>
                                    <form action="${ctx}/app/match" method="post" style="display:inline;">
                                        <input type="hidden" name="matchId" value="${match.id}">
                                        <button type="submit" name="action" value="refuse" class="btn btn-secondary btn-sm"><i class="fa-solid fa-xmark"></i></button>
                                    </form>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon"><i class="fa-solid fa-lightbulb"></i></div>
                    <p>Aucune suggestion pour le moment.</p>
                    <a href="${ctx}/app/match?action=suggestions" class="btn btn-primary btn-sm">Générer des suggestions</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Matchs acceptés -->
    <div class="card">
        <div class="card-header">
            <h2 class="card-title"><i class="fa-solid fa-heart"></i> Vos matchs</h2>
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
                                    <a href="${ctx}/app/message?action=conversation&partnerId=${other.id}" class="btn btn-primary btn-sm"><i class="fa-solid fa-comment-dots"></i> Message</a>
                                    <a href="${ctx}/app/profile?action=view&id=${other.id}" class="btn btn-outline-primary btn-sm">Profil</a>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon"><i class="fa-solid fa-heart-crack"></i></div>
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
        <h2 class="card-title"><i class="fa-solid fa-bell"></i> Notifications récentes</h2>
        <a href="${ctx}/app/notifications" class="btn btn-sm btn-outline-primary">Voir tout</a>
    </div>
    <c:choose>
        <c:when test="${not empty notifications}">
            <c:forEach var="notif" items="${notifications}" varStatus="status">
                <c:if test="${status.index < 5}">

                    <%-- Icône et titre selon le type --%>
                    <c:choose>
                        <c:when test="${notif.type == 'NOUVEAU_MESSAGE'}">
                            <c:set var="iconClass"  value="type-message" />
                            <c:set var="iconFA"     value="fa-comment-dots" />
                            <c:set var="notifTitle" value="Nouveau message" />
                        </c:when>
                        <c:when test="${notif.type == 'NOUVEAU_MATCH'}">
                            <c:set var="iconClass"  value="type-match" />
                            <c:set var="iconFA"     value="fa-heart" />
                            <c:set var="notifTitle" value="Nouveau match" />
                        </c:when>
                        <c:when test="${notif.type == 'LIKE_RECU'}">
                            <c:set var="iconClass"  value="type-like" />
                            <c:set var="iconFA"     value="fa-thumbs-up" />
                            <c:set var="notifTitle" value="Like reçu" />
                        </c:when>
                        <c:when test="${notif.type == 'VISITE_PROFIL'}">
                            <c:set var="iconClass"  value="type-visite" />
                            <c:set var="iconFA"     value="fa-eye" />
                            <c:set var="notifTitle" value="Visite de profil" />
                        </c:when>
                        <c:when test="${notif.type == 'ABONNEMENT_EXPIRE'}">
                            <c:set var="iconClass"  value="type-abonnement" />
                            <c:set var="iconFA"     value="fa-triangle-exclamation" />
                            <c:set var="notifTitle" value="Abonnement expiré" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="iconClass"  value="type-default" />
                            <c:set var="iconFA"     value="fa-circle-info" />
                            <c:set var="notifTitle" value="Notification" />
                        </c:otherwise>
                    </c:choose>

                    <div class="notification-item ${notif.lu ? '' : 'unread'}">
                        <div class="notif-icon ${iconClass}">
                            <i class="fa-solid ${iconFA}"></i>
                        </div>
                        <div class="notification-content">
                            <p style="margin:0;font-weight:600;font-size:0.88rem;">${notifTitle}</p>
                            <p style="margin:2px 0 0;color:var(--text-muted);font-size:0.82rem;">${notif.contenu}</p>
                            <span class="notification-time">
                                <i class="fa-regular fa-clock" style="font-size:0.7rem;"></i>
                                <fmt:formatDate value="${notif.dateCreationDate}" pattern="dd/MM/yyyy HH:mm"/>
                            </span>
                        </div>
                        <c:if test="${!notif.lu}">
                            <div class="notif-unread-dot"></div>
                        </c:if>
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

