<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Notifications" />
</jsp:include>

<style>
/* ===== NOTIFICATIONS AMÉLIORÉES ===== */
.notif-list { display: flex; flex-direction: column; gap: 0; }

.notif-row {
    display: flex;
    align-items: center;
    gap: 1rem;
    padding: 1rem 1.25rem;
    border-bottom: 1px solid var(--border);
    text-decoration: none;
    color: inherit;
    transition: background 0.18s;
    position: relative;
}
.notif-row:hover { background: rgba(79,70,229,0.06); }
.notif-row.unread { background: rgba(79,70,229,0.04); }
.notif-row.unread::before {
    content: '';
    position: absolute;
    left: 0; top: 0; bottom: 0;
    width: 3px;
    border-radius: 0 3px 3px 0;
    background: var(--indigo);
}

/* Icône avec fond coloré selon le type */
.notif-icon {
    width: 46px;
    height: 46px;
    border-radius: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.2rem;
    flex-shrink: 0;
    box-shadow: 0 2px 8px rgba(0,0,0,0.15);
}
.notif-icon.type-message  { background: linear-gradient(135deg, #0ea5e9, #06b6d4); color: #fff; }
.notif-icon.type-match    { background: linear-gradient(135deg, #ec4899, #f43f5e); color: #fff; }
.notif-icon.type-like     { background: linear-gradient(135deg, #f97316, #ef4444); color: #fff; }
.notif-icon.type-visite   { background: linear-gradient(135deg, #a855f7, #7c3aed); color: #fff; }
.notif-icon.type-abonnement { background: linear-gradient(135deg, #f59e0b, #d97706); color: #fff; }
.notif-icon.type-default  { background: linear-gradient(135deg, #64748b, #475569); color: #fff; }

.notif-body { flex: 1; min-width: 0; }
.notif-title {
    font-weight: 700;
    font-size: 0.92rem;
    margin: 0 0 2px;
    color: var(--text);
}
.notif-content {
    font-size: 0.84rem;
    color: var(--text-muted);
    margin: 0 0 3px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}
.notif-time {
    font-size: 0.75rem;
    color: var(--text-muted);
    opacity: 0.7;
}

/* Bouton supprimer */
.notif-delete {
    opacity: 0;
    transition: opacity 0.18s;
    flex-shrink: 0;
}
.notif-row:hover .notif-delete { opacity: 1; }

/* Badge non-lu */
.notif-unread-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: var(--indigo);
    flex-shrink: 0;
    box-shadow: 0 0 6px rgba(79,70,229,0.6);
}
</style>

<div class="page-header">
    <h1><i class="fa-solid fa-bell"></i> Notifications</h1>
    <p>Restez informé de toute l'activité sur votre compte.</p>
</div>

<c:if test="${param.deleted == '1'}">
    <div class="alert alert-success"><i class="fa-solid fa-circle-check"></i> Notification supprimée.</div>
</c:if>

<div class="card">
    <div class="card-header" style="justify-content: flex-end;">
        <form action="${ctx}/app/notifications" method="post" style="display:inline;">
            <input type="hidden" name="action" value="readAll">
            <button type="submit" class="btn btn-sm btn-secondary">
                <i class="fa-solid fa-check-double"></i> Tout marquer comme lu
            </button>
        </form>
    </div>

    <c:choose>
        <c:when test="${not empty notifications}">
            <div class="notif-list">
            <c:forEach var="notif" items="${notifications}">

                <%-- Détermine la classe d'icône et le titre selon le type --%>
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

                <a href="${ctx}/app/notifications?read=${notif.id}"
                   class="notif-row ${notif.lu ? '' : 'unread'}">

                    <%-- Icône colorée --%>
                    <div class="notif-icon ${iconClass}">
                        <i class="fa-solid ${iconFA}"></i>
                    </div>

                    <%-- Contenu --%>
                    <div class="notif-body">
                        <p class="notif-title">${notifTitle}</p>
                        <p class="notif-content">${notif.contenu}</p>
                        <span class="notif-time">
                            <i class="fa-regular fa-clock" style="font-size:0.7rem;"></i>
                            <fmt:formatDate value="${notif.dateCreationDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </span>
                    </div>

                    <%-- Point non-lu --%>
                    <c:if test="${!notif.lu}">
                        <div class="notif-unread-dot"></div>
                    </c:if>

                    <%-- Bouton supprimer (apparaît au hover) --%>
                    <form action="${ctx}/app/notifications" method="post"
                          class="notif-delete" onclick="event.stopPropagation();">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${notif.id}">
                        <button type="submit" class="btn btn-sm btn-danger"
                                onclick="return confirm('Supprimer cette notification ?');">
                            <i class="fa-solid fa-trash"></i>
                        </button>
                    </form>
                </a>

            </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <div class="empty-state-icon"><i class="fa-solid fa-bell-slash"></i></div>
                <h3>Aucune notification</h3>
                <p>Vous n'avez pas encore de notifications.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
