<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Mon Profil" />
</jsp:include>

<c:if test="${param.updated == '1'}">
    <div class="alert alert-success">
        &#10004; Votre profil a été mis à jour avec succès !
    </div>
</c:if>

<div class="grid grid-2">
    <!-- Carte profil -->
    <div class="card" style="text-align:center;">
        <img src="${user.photoProfil != null ? user.photoProfil : ctx.concat('/assets/images/default-avatar.png')}" 
             alt="${user.prenom}" class="profile-photo" style="width:150px;height:150px;"
             onerror="this.src='${ctx}/assets/images/default-avatar.png'">
        <h2 style="margin:1rem 0 0.5rem;">${user.prenom} ${user.nom}</h2>
        <p class="text-muted">${user.age} ans &bull; ${user.sexe} &bull; ${user.localisation}</p>
        <p>
            <span class="badge badge-${user.role == 'ADMIN' ? 'danger' : user.role == 'VIP' ? 'warning' : 'secondary'}">${user.role}</span>
            <span class="badge badge-${user.statut == 'ACTIF' ? 'success' : 'danger'}">${user.statut}</span>
            <span class="badge badge-${isOnline ? 'success' : 'secondary'}">${isOnline ? '&#128308; En ligne' : '&#9899; Hors ligne'}</span>
        </p>
        <div class="mt-2">
            <a href="${ctx}/app/profile?action=edit" class="btn btn-primary">&#9998; Modifier mon profil</a>
        </div>
        <div class="mt-2">
            <form action="${ctx}/delete-account" method="post" onsubmit="return confirm('Êtes-vous sûr de vouloir supprimer votre compte ? Cette action est irréversible.');">
                <button type="submit" class="btn btn-danger btn-sm">&#128465; Supprimer mon compte</button>
            </form>
        </div>
    </div>

    <!-- Détails -->
    <div>
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">&#128100; À propos de moi</h3>
            </div>
            <p>${not empty user.bio ? user.bio : '<em class="text-muted">Aucune bio renseignée.</em>'}</p>
        </div>

        <div class="card">
            <div class="card-header">
                <h3 class="card-title">&#10084; Centres d'intérêt</h3>
            </div>
            <c:choose>
                <c:when test="${not empty user.interets}">
                    <div class="d-flex gap-1" style="flex-wrap:wrap;">
                        <c:forEach var="interet" items="${user.interets}">
                            <span class="badge badge-primary">${interet.nom}</span>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">Aucun centre d'intérêt renseigné.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="card">
            <div class="card-header">
                <h3 class="card-title">&#128269; Préférences de recherche</h3>
            </div>
            <c:choose>
                <c:when test="${not empty user.preferences}">
                    <div class="grid grid-2">
                        <div><strong>Âge :</strong> ${user.preferences.ageMin} - ${user.preferences.ageMax} ans</div>
                        <div><strong>Sexe recherché :</strong> ${user.preferences.sexeRecherche}</div>
                        <div><strong>Distance max :</strong> ${user.preferences.localisationMaxKm} km</div>
                        <div><strong>Type de relation :</strong> ${user.preferences.typeRelation}</div>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">Aucune préférence renseignée.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="card">
            <div class="card-header">
                <h3 class="card-title">&#128179; Abonnement</h3>
            </div>
            <c:choose>
                <c:when test="${not empty user.abonnement}">
                    <p><strong>Type :</strong> <span class="badge badge-${user.abonnement.type == 'VIP' ? 'warning' : user.abonnement.type == 'PREMIUM' ? 'info' : 'secondary'}">${user.abonnement.type}</span></p>
                    <p><strong>Statut :</strong> <span class="badge badge-${user.abonnement.actif ? 'success' : 'danger'}">${user.abonnement.actif ? 'ACTIF' : user.abonnement.statut}</span></p>
                    <c:if test="${not empty user.abonnement.dateFin}">
                        <p><strong>Expire le :</strong> <fmt:formatDate value="${user.abonnement.dateFin}" pattern="dd/MM/yyyy"/></p>
                    </c:if>
                    <a href="${ctx}/app/subscription" class="btn btn-sm btn-outline-primary">Gérer mon abonnement</a>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">Vous n'avez pas d'abonnement actif.</p>
                    <a href="${ctx}/app/subscription" class="btn btn-sm btn-primary">Voir les offres</a>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="card">
            <div class="card-header">
                <h3 class="card-title">&#128247; Ma galerie</h3>
            </div>
            <c:choose>
                <c:when test="${not empty photos}">
                    <div class="grid grid-4">
                        <c:forEach var="photo" items="${photos}">
                            <img src="${photo.url}" style="width:100%;height:100px;object-fit:cover;border-radius:4px;" onerror="this.style.display='none'">
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">Aucune photo dans votre galerie.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="card">
            <div class="card-header">
                <h3 class="card-title">&#128064; Derniers visiteurs</h3>
            </div>
            <c:choose>
                <c:when test="${not empty recentVisitors}">
                    <div class="d-flex gap-1" style="flex-wrap:wrap;">
                        <c:forEach var="visitor" items="${recentVisitors}">
                            <a href="${ctx}/app/profile?action=view&id=${visitor.id}" title="${visitor.prenom} ${visitor.nom}">
                                <img src="${visitor.photoProfil != null ? visitor.photoProfil : ctx.concat('/assets/images/default-avatar.png')}" 
                                     style="width:50px;height:50px;border-radius:50%;object-fit:cover;"
                                     onerror="this.src='${ctx}/assets/images/default-avatar.png'">
                            </a>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">Aucun visiteur récent.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

