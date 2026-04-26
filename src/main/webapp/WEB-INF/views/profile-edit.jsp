<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Modifier mon Profil" />
</jsp:include>

<div class="page-header">
    <h1>&#9998; Modifier mon profil</h1>
    <p>Mettez à jour vos informations et préférences.</p>
</div>

<form action="${ctx}/app/profile" method="post" enctype="multipart/form-data">
    <input type="hidden" name="action" value="update">

    <div class="grid grid-2">
        <!-- Informations personnelles -->
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">&#128100; Informations personnelles</h3>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label for="prenom">Prénom</label>
                    <input type="text" id="prenom" name="prenom" class="form-control" value="${user.prenom}" required>
                </div>
                <div class="form-group">
                    <label for="nom">Nom</label>
                    <input type="text" id="nom" name="nom" class="form-control" value="${user.nom}" required>
                </div>
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" class="form-control" value="${user.email}" required>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label for="dateNaissance">Date de naissance</label>
                    <input type="date" id="dateNaissance" name="dateNaissance" class="form-control" value="${user.dateNaissance}" required>
                </div>
                <div class="form-group">
                    <label for="sexe">Sexe</label>
                    <select id="sexe" name="sexe" class="form-control" required>
                        <option value="HOMME" ${user.sexe == 'HOMME' ? 'selected' : ''}>Homme</option>
                        <option value="FEMME" ${user.sexe == 'FEMME' ? 'selected' : ''}>Femme</option>
                        <option value="AUTRE" ${user.sexe == 'AUTRE' ? 'selected' : ''}>Autre</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="localisation">Localisation</label>
                <input type="text" id="localisation" name="localisation" class="form-control" value="${user.localisation}">
            </div>
            <div class="form-group">
                <label for="bio">Bio / Présentation</label>
                <textarea id="bio" name="bio" class="form-control" rows="4">${user.bio}</textarea>
            </div>
            <div class="form-group">
                <label for="visibilite">Visibilité du profil</label>
                <select id="visibilite" name="visibilite" class="form-control">
                    <option value="PUBLIC" ${user.visibilite == 'PUBLIC' ? 'selected' : ''}>Public - Tout le monde peut voir</option>
                    <option value="AMIS" ${user.visibilite == 'AMIS' ? 'selected' : ''}>Amis - Seuls mes matchs</option>
                    <option value="PRIVE" ${user.visibilite == 'PRIVE' ? 'selected' : ''}>Privé - Seulement moi</option>
                </select>
            </div>
            <div class="form-group">
                <label for="photo">Photo de profil</label>
                <input type="file" id="photo" name="photo" class="form-control" accept="image/*">
                <c:if test="${not empty user.photoProfil}">
                    <p class="text-muted mt-1"><small>Photo actuelle : ${user.photoProfil}</small></p>
                </c:if>
            </div>
            <div class="form-group">
                <label for="photos">Photos de la galerie (plusieurs fichiers possibles)</label>
                <input type="file" id="photos" name="photos" class="form-control" accept="image/*" multiple>
            </div>
            <c:if test="${not empty photos}">
                <div class="form-group">
                    <label>Photos existantes</label>
                    <div class="grid grid-4">
                        <c:forEach var="photo" items="${photos}">
                            <div style="position:relative;">
                                <img src="${photo.url}" style="width:100%;height:80px;object-fit:cover;border-radius:4px;" onerror="this.style.display='none'">
                                <a href="${ctx}/app/profile?action=deletePhoto&photoId=${photo.id}" class="btn btn-danger btn-xs" style="position:absolute;top:2px;right:2px;padding:2px 6px;font-size:10px;" onclick="return confirm('Supprimer cette photo ?')">&#10005;</a>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </div>

        <!-- Préférences et intérêts -->
        <div>
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">&#10084; Centres d'intérêt</h3>
                </div>
                <div class="checkbox-group">
                    <c:forEach var="interet" items="${allInterets}">
                        <label class="checkbox-item">
                            <input type="checkbox" name="interets" value="${interet.id}" 
                                <c:forEach var="ui" items="${user.interets}">
                                    <c:if test="${ui.id == interet.id}">checked</c:if>
                                </c:forEach>
                            >
                            ${interet.nom}
                        </label>
                    </c:forEach>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">&#128269; Préférences de recherche</h3>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="ageMin">Âge minimum</label>
                        <input type="number" id="ageMin" name="ageMin" class="form-control" value="${user.preferences != null ? user.preferences.ageMin : 18}" min="18" max="99">
                    </div>
                    <div class="form-group">
                        <label for="ageMax">Âge maximum</label>
                        <input type="number" id="ageMax" name="ageMax" class="form-control" value="${user.preferences != null ? user.preferences.ageMax : 99}" min="18" max="99">
                    </div>
                </div>
                <div class="form-group">
                    <label for="sexeRecherche">Sexe recherché</label>
                    <select id="sexeRecherche" name="sexeRecherche" class="form-control">
                        <option value="TOUS" ${user.preferences != null && user.preferences.sexeRecherche == 'TOUS' ? 'selected' : ''}>Tous</option>
                        <option value="HOMME" ${user.preferences != null && user.preferences.sexeRecherche == 'HOMME' ? 'selected' : ''}>Homme</option>
                        <option value="FEMME" ${user.preferences != null && user.preferences.sexeRecherche == 'FEMME' ? 'selected' : ''}>Femme</option>
                        <option value="AUTRE" ${user.preferences != null && user.preferences.sexeRecherche == 'AUTRE' ? 'selected' : ''}>Autre</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="localisationMaxKm">Distance maximum (km)</label>
                    <input type="number" id="localisationMaxKm" name="localisationMaxKm" class="form-control" value="${user.preferences != null ? user.preferences.localisationMaxKm : 50}" min="1">
                </div>
                <div class="form-group">
                    <label for="typeRelation">Type de relation</label>
                    <select id="typeRelation" name="typeRelation" class="form-control">
                        <option value="TOUS" ${user.preferences != null && user.preferences.typeRelation == 'TOUS' ? 'selected' : ''}>Tous</option>
                        <option value="AMITIE" ${user.preferences != null && user.preferences.typeRelation == 'AMITIE' ? 'selected' : ''}>Amitié</option>
                        <option value="AMOUREUSE" ${user.preferences != null && user.preferences.typeRelation == 'AMOUREUSE' ? 'selected' : ''}>Amoureuse</option>
                        <option value="PROFESSIONNELLE" ${user.preferences != null && user.preferences.typeRelation == 'PROFESSIONNELLE' ? 'selected' : ''}>Professionnelle</option>
                    </select>
                </div>
            </div>
        </div>
    </div>

    <div class="d-flex gap-2 mt-3">
        <button type="submit" class="btn btn-primary btn-lg">&#128190; Enregistrer les modifications</button>
        <a href="${ctx}/app/profile" class="btn btn-secondary btn-lg">&#10060; Annuler</a>
    </div>
</form>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

