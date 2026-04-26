<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Matchs" />
</jsp:include>

<div class="page-header">
    <h1>&#128150; Vos matchs</h1>
    <p>Gérez vos suggestions et vos connexions.</p>
</div>

<div class="tabs">
    <a href="${ctx}/app/match" class="tab ${empty param.action || param.action == '' ? 'active' : ''}">Tout</a>
    <a href="${ctx}/app/match?action=suggestions" class="tab ${param.action == 'suggestions' ? 'active' : ''}">Suggestions</a>
    <a href="${ctx}/app/match?action=accepted" class="tab ${param.action == 'accepted' ? 'active' : ''}">Matchs acceptés</a>
</div>

<!-- Suggestions en attente -->
<c:if test="${empty param.action || param.action == 'suggestions' || param.action == ''}">
    <div class="card">
        <div class="card-header">
            <h3 class="card-title">&#128161; Suggestions en attente</h3>
            <a href="${ctx}/app/match?action=suggestions" class="btn btn-sm btn-primary">&#128260; Rafraîchir</a>
        </div>
        <c:choose>
            <c:when test="${not empty suggestions}">
                <div class="grid grid-3">
                    <c:forEach var="match" items="${suggestions}">
                        <div class="match-card">
                            <c:set var="other" value="${match.utilisateur1Id == user.id ? match.utilisateur2 : match.utilisateur1}" />
                            <img src="${other.photoProfil != null ? other.photoProfil : ctx.concat('/assets/images/default-avatar.png')}" 
                                 alt="${other.prenom}" class="profile-photo" style="width:100px;height:100px;"
                                 onerror="this.src='${ctx}/assets/images/default-avatar.png'">
                            <div class="match-score ${match.scoreCompatibilite >= 70 ? 'high' : match.scoreCompatibilite >= 40 ? 'medium' : 'low'}">
                                ${match.scoreCompatibilite}%
                            </div>
                            <div class="profile-name">${other.prenom}, ${other.age} ans</div>
                            <div class="profile-info">${other.localisation}</div>
                            <p class="profile-bio">${not empty other.bio ? other.bio.substring(0, other.bio.length() > 50 ? 50 : other.bio.length()).concat('...') : ''}</p>
                            <div class="profile-actions">
                                <form action="${ctx}/app/match" method="post" style="display:inline;">
                                    <input type="hidden" name="matchId" value="${match.id}">
                                    <button type="submit" name="action" value="accept" class="btn btn-success btn-sm">&#128077; Accepter</button>
                                </form>
                                <form action="${ctx}/app/match" method="post" style="display:inline;">
                                    <input type="hidden" name="matchId" value="${match.id}">
                                    <button type="submit" name="action" value="refuse" class="btn btn-danger btn-sm">&#10060;</button>
                                </form>
                                <a href="${ctx}/app/profile?action=view&id=${other.id}" class="btn btn-outline-primary btn-sm">Profil</a>
                            </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon">&#128161;</div>
                    <p>Aucune suggestion en attente.</p>
                    <a href="${ctx}/app/match?action=suggestions" class="btn btn-primary btn-sm">Générer des suggestions</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<!-- Matchs acceptés -->
<c:if test="${empty param.action || param.action == 'accepted' || param.action == ''}">
    <div class="card">
        <div class="card-header">
            <h3 class="card-title">&#128149; Matchs acceptés</h3>
        </div>
        <c:choose>
            <c:when test="${not empty matches}">
                <div class="grid grid-3">
                    <c:forEach var="match" items="${matches}">
                        <div class="match-card">
                            <c:set var="other" value="${match.utilisateur1Id == user.id ? match.utilisateur2 : match.utilisateur1}" />
                            <img src="${other.photoProfil != null ? other.photoProfil : ctx.concat('/assets/images/default-avatar.png')}" 
                                 alt="${other.prenom}" class="profile-photo" style="width:100px;height:100px;"
                                 onerror="this.src='${ctx}/assets/images/default-avatar.png'">
                            <div class="match-score high">${match.scoreCompatibilite}%</div>
                            <div class="profile-name">${other.prenom}, ${other.age} ans</div>
                            <div class="profile-info">${other.localisation}</div>
                            <div class="profile-actions">
                                <a href="${ctx}/app/message?action=conversation&partnerId=${other.id}" class="btn btn-primary btn-sm">&#128172; Message</a>
                                <a href="${ctx}/app/profile?action=view&id=${other.id}" class="btn btn-outline-primary btn-sm">Profil</a>
                                <form action="${ctx}/app/match" method="post" style="display:inline;" 
                                      onsubmit="return confirm('Marquer comme déjà rencontré ?');">
                                    <input type="hidden" name="matchId" value="${match.id}">
                                    <button type="submit" name="action" value="dejaRencontre" class="btn btn-warning btn-sm">&#128107;</button>
                                </form>
                            </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon">&#128148;</div>
                    <p>Vous n'avez pas encore de matchs acceptés.</p>
                    <a href="${ctx}/app/search" class="btn btn-primary btn-sm">Rechercher des membres</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
