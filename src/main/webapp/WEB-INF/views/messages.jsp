<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="currentUser" value="${sessionScope.utilisateur}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Messages" />
</jsp:include>

<div class="page-header">
    <h1>&#128172; Messagerie</h1>
    <p>Discutez avec vos matchs en temps réel.</p>
</div>

<div class="chat-container">
    <!-- Liste des conversations -->
    <div class="conversations-list">
        <div class="card-header" style="border-bottom:1px solid var(--border-color);padding:1rem;">
            <h3 class="card-title" style="font-size:1rem;">Conversations</h3>
        </div>

        <!-- Formulaire nouveau message direct -->
        <div style="padding:1rem;border-bottom:1px solid var(--border-color);">
            <form action="${ctx}/app/message" method="post" style="display:flex;gap:0.5rem;">
                <input type="number" name="destinataireId" placeholder="ID utilisateur" required style="width:80px;padding:0.4rem;border:1px solid var(--border-color);border-radius:4px;">
                <input type="text" name="contenu" placeholder="Votre message..." required style="flex:1;padding:0.4rem;border:1px solid var(--border-color);border-radius:4px;">
                <button type="submit" class="btn btn-primary btn-sm">&#10148;</button>
            </form>
        </div>

        <c:choose>
            <c:when test="${not empty partners}">
                <c:forEach var="partnerId" items="${partners}">
                    <a href="${ctx}/app/message?action=conversation&partnerId=${partnerId}"
                       class="conversation-item ${param.partnerId == partnerId ? 'active' : ''}">
                        <img src="${ctx}/assets/images/default-avatar.png" class="profile-photo-sm" alt="">
                        <div class="conversation-info">
                            <h4>Utilisateur #${partnerId}</h4>
                            <p class="text-muted">Cliquez pour voir</p>
                        </div>
                    </a>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div style="padding:2rem;text-align:center;color:var(--text-muted);">
                    <p>Aucune conversation.</p>
                    <a href="${ctx}/app/match" class="btn btn-sm btn-primary">Voir mes matchs</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Zone de chat -->
    <div class="chat-area">
        <c:choose>
            <c:when test="${not empty partner}">
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">${errorMessage}</div>
                </c:if>
                <div class="chat-header">
                    <img src="${ctx}/assets/images/default-avatar.png" class="profile-photo-sm" alt="">
                    <div>
                        <strong>${partner.prenom} ${partner.nom}</strong>
                        <p class="text-muted" style="margin:0;font-size:0.85rem;">
                            ${partner.localisation}
                        </p>
                    </div>
                    <a href="${ctx}/app/profile?action=view&id=${partner.id}" class="btn btn-sm btn-outline-primary">Profil</a>
                </div>
                <div class="chat-messages">
                    <c:choose>
                        <c:when test="${not empty messages}">
                            <c:forEach var="msg" items="${messages}">
                                <div class="message ${msg.expediteurId == currentUser.id ? 'sent' : 'received'}">
                                    <div>${msg.contenu}</div>
                                    <div class="message-time">
                                        <fmt:formatDate value="${msg.dateEnvoi}" pattern="dd/MM HH:mm"/>
                                        <c:if test="${msg.expediteurId == currentUser.id}">
                                            ${msg.lu ? '&#10003;&#10003;' : '&#10003;'}
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div style="text-align:center;color:var(--text-muted);padding:2rem;">
                                <p><em>Démarrez la conversation avec ${partner.prenom}...</em></p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="chat-input">
                    <form action="${ctx}/app/message" method="post" style="display:flex;gap:1rem;width:100%;">
                        <input type="hidden" name="destinataireId" value="${partner.id}">
                        <input type="text" name="contenu" placeholder="Écrivez votre message..." required autocomplete="off">
                        <button type="submit" class="btn btn-primary">&#10148;</button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div style="display:flex;align-items:center;justify-content:center;height:100%;color:var(--text-muted);">
                    <div style="text-align:center;">
                        <div style="font-size:3rem;margin-bottom:1rem;">&#128172;</div>
                        <h3>Sélectionnez une conversation</h3>
                        <p>Choisissez un contact dans la liste ou envoyez un message direct.</p>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

