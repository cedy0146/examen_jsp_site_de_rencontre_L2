<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="currentUser" value="${sessionScope.utilisateur}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Messages" />
</jsp:include>

<style>
/* ===== MESSAGERIE AMÉLIORÉE ===== */

/* Supprime le formulaire "ID utilisateur" moche - remplacé dans la liste */
.conv-new-msg { display: none; }

/* Conversation item amélioré */
.conversation-item {
    padding: 0.85rem 1rem !important;
    gap: 0.75rem !important;
}
.conv-item-body {
    flex: 1;
    min-width: 0;
}
.conv-item-top {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 0.5rem;
}
.conv-item-name {
    font-weight: 600;
    font-size: 0.9rem;
    color: var(--text);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}
.conv-item-time {
    font-size: 0.72rem;
    color: var(--text-muted);
    flex-shrink: 0;
}
.conv-item-preview {
    font-size: 0.8rem;
    color: var(--text-muted);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    margin-top: 2px;
}
.conv-unread-badge {
    background: var(--indigo);
    color: white;
    font-size: 0.68rem;
    font-weight: 700;
    padding: 2px 6px;
    border-radius: 10px;
    flex-shrink: 0;
}
.conv-item-preview.unread {
    color: var(--text);
    font-weight: 600;
}

/* Avatar avec indicateur online (déco) */
.conv-avatar-wrap {
    position: relative;
    flex-shrink: 0;
}
.conv-avatar-wrap .profile-photo-sm {
    width: 46px;
    height: 46px;
}

/* En-tête du chat amélioré */
.chat-header {
    padding: 0.85rem 1.25rem !important;
}
.chat-header-info { flex: 1; }
.chat-header-name {
    font-size: 1rem;
    font-weight: 700;
    margin: 0;
}
.chat-header-sub {
    font-size: 0.78rem;
    color: var(--text-muted);
    margin: 0;
}

/* Bulles plus rondes */
.message {
    max-width: 65% !important;
    border-radius: 20px !important;
}
.message.sent   { border-bottom-right-radius: 5px !important; }
.message.received { border-bottom-left-radius: 5px !important; }

/* Message time inline */
.message-time {
    text-align: right;
    font-size: 0.68rem !important;
    opacity: 0.7;
    margin-top: 4px;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    gap: 3px;
}

/* Liste conv header */
.conv-list-header {
    padding: 1rem;
    border-bottom: 1px solid var(--border);
    display: flex;
    align-items: center;
    justify-content: space-between;
}
.conv-list-header h3 {
    font-size: 1rem;
    margin: 0;
    font-weight: 700;
}
</style>

<div class="page-header">
    <h1><i class="fa-solid fa-comment-dots"></i> Messagerie</h1>
    <p>Discutez avec vos matchs en temps réel.</p>
</div>

<c:if test="${param.error == 'match'}">
    <div class="alert alert-danger">Vous devez avoir un match accepté pour envoyer des messages.</div>
</c:if>
<c:if test="${not empty errorMessage && empty partner}">
    <div class="alert alert-danger">${errorMessage}</div>
</c:if>

<div class="chat-container">

    <!-- ===== LISTE CONVERSATIONS ===== -->
    <div class="conversations-list">
        <div class="conv-list-header">
            <h3><i class="fa-solid fa-inbox"></i> Conversations</h3>
            <a href="${ctx}/app/match" class="btn btn-sm btn-primary" title="Voir mes matchs">
                <i class="fa-solid fa-heart"></i>
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty partners}">
                <c:forEach var="p" items="${partners}">
                    <c:set var="unread" value="${unreadCounts[p.id]}" />
                    <c:set var="lastMsg" value="${lastMessages[p.id]}" />
                    <a href="${ctx}/app/message?action=conversation&partnerId=${p.id}"
                       class="conversation-item ${param.partnerId == p.id ? 'active' : ''}">

                        <div class="conv-avatar-wrap">
                            <img src="${not empty p.photoProfil ? p.photoProfil : ctx.concat('/assets/images/default-avatar.png')}"
                                 class="profile-photo-sm" alt="${p.prenom}"
                                 onerror="this.src='${ctx}/assets/images/default-avatar.png'">
                        </div>

                        <div class="conv-item-body">
                            <div class="conv-item-top">
                                <span class="conv-item-name">${p.prenom} ${p.nom}</span>
                                <c:if test="${not empty lastMsg}">
                                    <span class="conv-item-time">
                                        <fmt:formatDate value="${lastMsg.dateEnvoiDate}" pattern="HH:mm"/>
                                    </span>
                                </c:if>
                            </div>
                            <div class="d-flex align-center" style="gap:0.4rem;">
                                <span class="conv-item-preview ${unread > 0 ? 'unread' : ''}">
                                    <c:choose>
                                        <c:when test="${not empty lastMsg}">
                                            <c:if test="${lastMsg.expediteurId == currentUser.id}">
                                                <i class="fa-solid fa-check" style="font-size:0.7rem;color:var(--teal);"></i>
                                            </c:if>
                                            ${lastMsg.contenu.length() > 35 ? lastMsg.contenu.substring(0,35).concat('...') : lastMsg.contenu}
                                        </c:when>
                                        <c:otherwise><em>Commencer la conversation</em></c:otherwise>
                                    </c:choose>
                                </span>
                                <c:if test="${unread > 0}">
                                    <span class="conv-unread-badge">${unread}</span>
                                </c:if>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div style="padding:2rem;text-align:center;color:var(--text-muted);">
                    <i class="fa-solid fa-comment-slash" style="font-size:2rem;margin-bottom:0.75rem;display:block;"></i>
                    <p>Aucune conversation.</p>
                    <a href="${ctx}/app/match" class="btn btn-sm btn-primary">Voir mes matchs</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- ===== ZONE DE CHAT ===== -->
    <div class="chat-area">
        <c:choose>
            <c:when test="${not empty partner}">
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">${errorMessage}</div>
                </c:if>

                <!-- En-tête -->
                <div class="chat-header">
                    <img src="${not empty partner.photoProfil ? partner.photoProfil : ctx.concat('/assets/images/default-avatar.png')}"
                         class="profile-photo-sm" alt="${partner.prenom}"
                         onerror="this.src='${ctx}/assets/images/default-avatar.png'">
                    <div class="chat-header-info">
                        <p class="chat-header-name">${partner.prenom} ${partner.nom}</p>
                        <p class="chat-header-sub"><i class="fa-solid fa-location-dot"></i> ${partner.localisation}</p>
                    </div>
                    <a href="${ctx}/app/profile?action=view&id=${partner.id}" class="btn btn-sm btn-primary">
                        <i class="fa-solid fa-user"></i> Profil
                    </a>
                </div>

                <!-- Messages -->
                <div class="chat-messages" id="chatMessages">
                    <c:choose>
                        <c:when test="${not empty messages}">
                            <c:forEach var="msg" items="${messages}">
                                <div class="message ${msg.expediteurId == currentUser.id ? 'sent' : 'received'}">
                                    <div>${msg.contenu}</div>
                                    <div class="message-time">
                                        <fmt:formatDate value="${msg.dateEnvoiDate}" pattern="dd/MM HH:mm"/>
                                        <c:if test="${msg.expediteurId == currentUser.id}">
                                            <c:choose>
                                                <c:when test="${msg.lu}">
                                                    <i class="fa-solid fa-check-double" style="color:var(--teal-light);"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fa-solid fa-check"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div style="text-align:center;color:var(--text-muted);padding:3rem;">
                                <i class="fa-solid fa-hand-wave" style="font-size:2.5rem;margin-bottom:1rem;display:block;"></i>
                                <p>Démarrez la conversation avec <strong>${partner.prenom}</strong> !</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Input -->
                <div class="chat-input">
                    <form action="${ctx}/app/message" method="post" style="display:flex;gap:0.75rem;width:100%;">
                        <input type="hidden" name="destinataireId" value="${partner.id}">
                        <input type="text" name="contenu" placeholder="Écrivez votre message..." required autocomplete="off">
                        <button type="submit" class="btn btn-primary">
                            <i class="fa-solid fa-paper-plane"></i>
                        </button>
                    </form>
                </div>
            </c:when>

            <c:otherwise>
                <div style="display:flex;align-items:center;justify-content:center;height:100%;color:var(--text-muted);">
                    <div style="text-align:center;">
                        <i class="fa-solid fa-comment-dots" style="font-size:3.5rem;margin-bottom:1rem;display:block;opacity:0.3;"></i>
                        <h3>Sélectionnez une conversation</h3>
                        <p>Choisissez un contact dans la liste à gauche.</p>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
// Auto-scroll vers le bas des messages
const chatMessages = document.getElementById('chatMessages');
if (chatMessages) chatMessages.scrollTop = chatMessages.scrollHeight;
</script>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
