<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Notifications" />
</jsp:include>

<div class="page-header">
    <h1>&#128276; Notifications</h1>
    <p>Restez informé de toute l'activité sur votre compte.</p>
</div>

<c:if test="${param.deleted == '1'}">
    <div class="alert alert-success">&#10004; Notification supprimée.</div>
</c:if>

<div class="card">
    <div class="card-header" style="justify-content: flex-end;">
        <form action="${ctx}/app/notifications" method="post" style="display:inline;">
            <input type="hidden" name="action" value="readAll">
            <button type="submit" class="btn btn-sm btn-secondary">&#10003; Tout marquer comme lu</button>
        </form>
    </div>

    <c:choose>
        <c:when test="${not empty notifications}">
            <c:forEach var="notif" items="${notifications}">
                <div class="notification-item ${notif.lu ? '' : 'unread'}">
                    <div class="notification-icon" style="background: ${notif.type == 'NOUVEAU_MESSAGE' ? '#d1ecf1' : notif.type == 'NOUVEAU_MATCH' ? '#d4edda' : notif.type == 'LIKE_RECU' ? '#f8d7da' : notif.type == 'VISITE_PROFIL' ? '#fff3cd' : '#e2e3e5'};">
                        ${notif.type == 'NOUVEAU_MESSAGE' ? '&#128172;' : notif.type == 'NOUVEAU_MATCH' ? '&#128150;' : notif.type == 'LIKE_RECU' ? '&#128077;' : notif.type == 'VISITE_PROFIL' ? '&#128065;' : notif.type == 'ABONNEMENT_EXPIRE' ? '&#9888;' : '&#8505;'}
                    </div>
                    <div class="notification-content">
                        <p><strong>${notif.type == 'NOUVEAU_MESSAGE' ? 'Nouveau message' : notif.type == 'NOUVEAU_MATCH' ? 'Nouveau match' : notif.type == 'LIKE_RECU' ? 'Like reçu' : notif.type == 'VISITE_PROFIL' ? 'Visite de profil' : notif.type == 'ABONNEMENT_EXPIRE' ? 'Abonnement' : 'Notification'}</strong></p>
                        <p>${notif.contenu}</p>
                        <span class="notification-time"><fmt:formatDate value="${notif.dateCreation}" pattern="dd/MM/yyyy HH:mm"/></span>
                    </div>
                    <form action="${ctx}/app/notifications" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${notif.id}">
                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer cette notification ?');">&#128465;</button>
                    </form>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <div class="empty-state-icon">&#128276;</div>
                <h3>Aucune notification</h3>
                <p>Vous n'avez pas encore de notifications.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
