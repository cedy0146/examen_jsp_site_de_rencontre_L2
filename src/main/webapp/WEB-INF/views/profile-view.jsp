<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<c:set var="vu" value="${viewedUser}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Profil de ${vu.prenom}" />
</jsp:include>

<div class="page-header">
    <h1><i class="fa-solid fa-user"></i> Profil de ${vu.prenom} ${vu.nom}</h1>
    <p>Découvrez ce membre et interagissez avec lui.</p>
</div>

<div class="grid grid-2">
    <div class="card" style="text-align:center;">
        <img src="${vu.photoProfil != null ? vu.photoProfil : ctx.concat('/assets/images/default-avatar.png')}" 
             alt="${vu.prenom}" class="profile-photo" style="width:180px;height:180px;"
             onerror="this.src='${ctx}/assets/images/default-avatar.png'">
        <h2 style="margin:1rem 0 0.5rem;">${vu.prenom} ${vu.nom}</h2>
        <p class="text-muted">${vu.age} ans &bull; ${vu.sexe} &bull; ${vu.localisation}</p>
        <p>
            <span class="badge badge-${vu.role == 'ADMIN' ? 'danger' : vu.role == 'VIP' ? 'warning' : 'secondary'}">${vu.role}</span>
            <c:if test="${vu.visibilite == 'PUBLIC'}">
                <span class="badge badge-success">Profil public</span>
            </c:if>
            <span class="badge badge-${isOnline ? 'success' : 'secondary'}">${isOnline ? '<i class="fa-solid fa-circle" style="color:#4caf50;font-size:0.6em;vertical-align:middle;"></i> En ligne' : '<i class="fa-solid fa-circle" style="color:#9e9e9e;font-size:0.6em;vertical-align:middle;"></i> Hors ligne'}</span>
        </p>
        <c:if test="${not empty distance}">
            <p class="text-muted"><i class="fa-solid fa-location-dot"></i> À ${distance} km de vous</p>
        </c:if>
        <div class="profile-actions mt-2">
            <form action="${ctx}/app/interaction" method="post" style="display:inline;">
                <input type="hidden" name="action" value="like">
                <input type="hidden" name="destinataireId" value="${vu.id}">
                <button type="submit" class="btn btn-success"><i class="fa-solid fa-thumbs-up"></i> J'aime</button>
            </form>
            <a href="${ctx}/app/message?action=conversation&partnerId=${vu.id}" class="btn btn-primary"><i class="fa-solid fa-comment-dots"></i> Message</a>
            <c:choose>
                <c:when test="${isBlocked}">
                    <form action="${ctx}/app/interaction" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="unblock">
                        <input type="hidden" name="destinataireId" value="${vu.id}">
                        <button type="submit" class="btn btn-warning"><i class="fa-solid fa-lock-open"></i> Débloquer</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <form action="${ctx}/app/interaction" method="post" style="display:inline;" 
                          onsubmit="return confirm('Voulez-vous vraiment bloquer cet utilisateur ?');">
                        <input type="hidden" name="action" value="block">
                        <input type="hidden" name="destinataireId" value="${vu.id}">
                        <button type="submit" class="btn btn-danger"><i class="fa-solid fa-ban"></i> Bloquer</button>
                    </form>
                </c:otherwise>
            </c:choose>
            <button type="button" class="btn btn-outline-danger btn-sm" onclick="document.getElementById('reportForm').style.display='block'"><i class="fa-solid fa-flag"></i> Signaler</button>
        </div>
        <!-- Formulaire de signalement -->
        <div id="reportForm" style="display:none;margin-top:1rem;" class="card">
            <div class="card-header"><h4>Signaler ${vu.prenom}</h4></div>
            <form action="${ctx}/app/profile" method="post">
                <input type="hidden" name="action" value="report">
                <input type="hidden" name="signaleId" value="${vu.id}">
                <div class="form-group">
                    <label>Motif</label>
                    <select name="motif" class="form-control" required>
                        <option value="FAUX_PROFIL">Faux profil</option>
                        <option value="HARCELEMENT">Harcèlement</option>
                        <option value="PHOTO_INAPPROPRIEE">Photo inappropriée</option>
                        <option value="COMPORTEMENT">Comportement inapproprié</option>
                        <option value="SPAM">Spam</option>
                        <option value="AUTRE">Autre</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Description</label>
                    <textarea name="description" class="form-control" rows="3" placeholder="Décrivez le problème..."></textarea>
                </div>
                <button type="submit" class="btn btn-danger btn-sm">Envoyer le signalement</button>
                <button type="button" class="btn btn-secondary btn-sm" onclick="document.getElementById('reportForm').style.display='none'">Annuler</button>
            </form>
        </div>
        <c:if test="${param.reported == '1'}">
            <div class="alert alert-success mt-2"><i class="fa-solid fa-circle-check"></i> Signalement envoyé.</div>
        </c:if>
    </div>

    <div>
        <div class="card">
            <div class="card-header">
                <h3 class="card-title"><i class="fa-solid fa-comment-dots"></i> Bio</h3>
            </div>
            <p>${not empty vu.bio ? vu.bio : '<em class="text-muted">Aucune bio renseignée.</em>'}</p>
        </div>

        <div class="card">
            <div class="card-header">
                <h3 class="card-title"><i class="fa-solid fa-heart"></i> Centres d'intérêt</h3>
            </div>
            <c:choose>
                <c:when test="${not empty vu.interets}">
                    <div class="d-flex gap-1" style="flex-wrap:wrap;">
                        <c:forEach var="interet" items="${vu.interets}">
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
                <h3 class="card-title"><i class="fa-solid fa-images"></i> Galerie photos</h3>
            </div>
            <c:choose>
                <c:when test="${not empty photos}">
                    <div class="grid grid-3">
                        <c:forEach var="photo" items="${photos}">
                            <img src="${photo.url}" style="width:100%;height:120px;object-fit:cover;border-radius:4px;" onerror="this.style.display='none'">
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">Aucune photo dans la galerie.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="card">
            <div class="card-header">
                <h3 class="card-title"><i class="fa-solid fa-calendar"></i> Informations</h3>
            </div>
            <div class="grid grid-2">
                <div><strong>Membre depuis :</strong> ${vu.dateInscription}</div>
                <div><strong>Dernière connexion :</strong> ${not empty vu.derniereConnexion ? vu.derniereConnexion : 'Jamais'}</div>
                <div><strong>Localisation :</strong> ${vu.localisation}</div>
                <div><strong>Visibilité :</strong> ${vu.visibilite}</div>
            </div>
        </div>
    </div>
</div>

<div class="mt-3">
    <a href="${ctx}/app/search" class="btn btn-secondary"><i class="fa-solid fa-arrow-left"></i> Retour à la recherche</a>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

