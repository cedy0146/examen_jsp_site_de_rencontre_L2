<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<c:set var="vu" value="${viewedUser}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Profil de ${vu.prenom}" />
</jsp:include>

<div class="page-header">
    <h1>&#128100; Profil de ${vu.prenom} ${vu.nom}</h1>
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
        </p>
        <div class="profile-actions mt-2">
            <form action="${ctx}/app/interaction" method="post" style="display:inline;">
                <input type="hidden" name="destinataireId" value="${vu.id}">
                <input type="hidden" name="type" value="LIKE">
                <button type="submit" class="btn btn-success">&#128077; J'aime</button>
            </form>
            <a href="${ctx}/app/message?action=conversation&partnerId=${vu.id}" class="btn btn-primary">&#128172; Message</a>
            <form action="${ctx}/app/interaction" method="post" style="display:inline;" 
                  onsubmit="return confirm('Voulez-vous vraiment bloquer cet utilisateur ?');">
                <input type="hidden" name="destinataireId" value="${vu.id}">
                <input type="hidden" name="type" value="BLOCAGE">
                <button type="submit" class="btn btn-danger">&#128683; Bloquer</button>
            </form>
        </div>
    </div>

    <div>
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">&#128172; Bio</h3>
            </div>
            <p>${not empty vu.bio ? vu.bio : '<em class="text-muted">Aucune bio renseignée.</em>'}</p>
        </div>

        <div class="card">
            <div class="card-header">
                <h3 class="card-title">&#10084; Centres d'intérêt</h3>
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
                <h3 class="card-title">&#128197; Informations</h3>
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
    <a href="${ctx}/app/search" class="btn btn-secondary">&#8592; Retour à la recherche</a>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

