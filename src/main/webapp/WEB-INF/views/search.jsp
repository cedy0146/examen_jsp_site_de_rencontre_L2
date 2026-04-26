<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Recherche" />
</jsp:include>

<div class="page-header">
    <h1>&#128269; Recherche avancée</h1>
    <p>Trouvez des membres qui correspondent à vos critères.</p>
</div>

<!-- Filtres -->
<div class="search-filters">
    <form action="${ctx}/app/search" method="post">
        <div class="form-group">
            <label for="sexe">Sexe</label>
            <select id="sexe" name="sexe" class="form-control">
                <option value="">Tous</option>
                <option value="HOMME">Homme</option>
                <option value="FEMME">Femme</option>
                <option value="AUTRE">Autre</option>
            </select>
        </div>
        <div class="form-group">
            <label for="ageMin">Âge min</label>
            <input type="number" id="ageMin" name="ageMin" class="form-control" value="18" min="18" max="99">
        </div>
        <div class="form-group">
            <label for="ageMax">Âge max</label>
            <input type="number" id="ageMax" name="ageMax" class="form-control" value="99" min="18" max="99">
        </div>
        <div class="form-group">
            <label for="localisation">Ville</label>
            <input type="text" id="localisation" name="localisation" class="form-control" placeholder="Paris, Lyon...">
        </div>
        <div class="form-group">
            <label for="interetId">Centre d'intérêt</label>
            <select id="interetId" name="interetId" class="form-control">
                <option value="">Tous</option>
                <c:forEach var="interet" items="${allInterets}">
                    <option value="${interet.id}">${interet.nom}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary">&#128269; Rechercher</button>
            <a href="${ctx}/app/search" class="btn btn-secondary">Réinitialiser</a>
        </div>
    </form>
</div>

<!-- Résultats -->
<c:choose>
    <c:when test="${not empty results}">
        <div class="page-header">
            <h2>${results.size()} résultat(s) trouvé(s)</h2>
        </div>
        <div class="grid grid-4">
            <c:forEach var="u" items="${results}">
                <div class="profile-card">
                    <img src="${u.photoProfil != null ? u.photoProfil : ctx.concat('/assets/images/default-avatar.png')}" 
                         alt="${u.prenom}" class="profile-photo"
                         onerror="this.src='${ctx}/assets/images/default-avatar.png'">
                    <div class="profile-name">${u.prenom}, ${u.age} ans</div>
                    <div class="profile-info">${u.sexe} &bull; ${u.localisation}</div>
                    <p class="profile-bio">${not empty u.bio ? u.bio.substring(0, u.bio.length() > 60 ? 60 : u.bio.length()).concat('...') : '<em>Pas de bio</em>'}</p>
                    <div class="profile-actions">
                        <a href="${ctx}/app/profile?action=view&id=${u.id}" class="btn btn-primary btn-sm">Voir profil</a>
                        <form action="${ctx}/app/interaction" method="post" style="display:inline;">
                            <input type="hidden" name="destinataireId" value="${u.id}">
                            <input type="hidden" name="type" value="LIKE">
                            <button type="submit" class="btn btn-success btn-sm">&#128077;</button>
                        </form>
                    </div>
            </c:forEach>
        </div>
    </c:when>
    <c:when test="${results != null && empty results}">
        <div class="empty-state">
            <div class="empty-state-icon">&#128269;</div>
            <h3>Aucun résultat</h3>
            <p>Aucun profil ne correspond à vos critères. Essayez d'élargir votre recherche.</p>
        </div>
    </c:when>
</c:choose>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
