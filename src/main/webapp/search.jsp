<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Recherche" />
</jsp:include>

<style>
/* ===== SEARCH PAGE OVERRIDES ===== */
.search-results-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 1.25rem;
    margin-top: 1.5rem;
}
@media (max-width: 1200px) { .search-results-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 900px)  { .search-results-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 550px)  { .search-results-grid { grid-template-columns: 1fr; } }

.result-card {
    background: var(--card-bg);
    border-radius: 16px;
    border: 1px solid var(--border);
    overflow: hidden;
    transition: transform 0.22s ease, box-shadow 0.22s ease;
    animation: fadeInUp 0.4s ease both;
    position: relative;
    display: flex;
    flex-direction: column;
}
.result-card:hover {
    transform: translateY(-6px);
    box-shadow: 0 16px 40px rgba(0,0,0,0.25);
}

/* Banner dégradé en haut */
.result-card-banner {
    height: 80px;
    background: linear-gradient(135deg, var(--indigo) 0%, var(--teal) 60%, var(--rose-gold, #e88fa0) 100%);
    flex-shrink: 0;
    position: relative;
}

/* Badge abonnement */
.result-badge {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 0.65rem;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    padding: 3px 8px;
    border-radius: 20px;
    background: rgba(255,255,255,0.18);
    color: #fff;
    backdrop-filter: blur(4px);
    border: 1px solid rgba(255,255,255,0.3);
}

/* Photo centrée chevauchant la bannière */
.result-avatar-wrap {
    display: flex;
    justify-content: center;
    margin-top: -40px;
    position: relative;
    z-index: 2;
}
.result-avatar {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    object-fit: cover;
    border: 3px solid var(--card-bg);
    box-shadow: 0 4px 14px rgba(0,0,0,0.3);
    transition: transform 0.2s ease;
}
.result-card:hover .result-avatar {
    transform: scale(1.07);
}

/* Corps de la carte */
.result-body {
    padding: 0.75rem 1rem 1rem;
    text-align: center;
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 0.3rem;
}
.result-name {
    font-size: 1rem;
    font-weight: 700;
    color: var(--text);
    margin: 0;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}
.result-meta {
    font-size: 0.78rem;
    color: var(--text-muted);
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 6px;
    flex-wrap: wrap;
}
.result-meta .dot {
    width: 3px;
    height: 3px;
    border-radius: 50%;
    background: var(--text-muted);
    display: inline-block;
}
.result-bio {
    font-size: 0.82rem;
    color: var(--text-muted);
    font-style: italic;
    line-height: 1.45;
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    margin: 0.25rem 0 0.5rem;
    flex: 1;
}

/* Boutons action */
.result-actions {
    display: flex;
    gap: 0.5rem;
    justify-content: center;
    margin-top: auto;
    padding-top: 0.5rem;
}
.btn-view {
    flex: 1;
    padding: 0.45rem 0.75rem;
    font-size: 0.82rem;
    border-radius: 10px;
    background: var(--indigo);
    color: #fff;
    border: none;
    cursor: pointer;
    text-decoration: none;
    text-align: center;
    transition: background 0.18s;
    font-weight: 600;
}
.btn-view:hover { background: var(--indigo-dark, #3730a3); color: #fff; }
.btn-like {
    width: 38px;
    height: 38px;
    border-radius: 10px;
    background: rgba(236,72,153,0.13);
    border: 1px solid rgba(236,72,153,0.3);
    color: #ec4899;
    cursor: pointer;
    font-size: 1rem;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: background 0.18s, transform 0.15s;
    flex-shrink: 0;
}
.btn-like:hover { background: #ec4899; color: #fff; transform: scale(1.1); }

/* Compteur résultats */
.results-count {
    display: flex;
    align-items: center;
    gap: 0.6rem;
    font-size: 0.95rem;
    color: var(--text-muted);
    margin-bottom: 0.25rem;
}
.results-count strong {
    font-size: 1.4rem;
    font-weight: 800;
    color: var(--text);
}
</style>

<div class="page-header">
    <h1><i class="fa-solid fa-magnifying-glass"></i> Recherche avancée</h1>
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
            <label for="distanceMax">Distance max (km)</label>
            <input type="number" id="distanceMax" name="distanceMax" class="form-control" value="50" min="1" max="500">
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
            <button type="submit" class="btn btn-primary"><i class="fa-solid fa-magnifying-glass"></i> Rechercher</button>
            <a href="${ctx}/app/search" class="btn btn-secondary">Réinitialiser</a>
        </div>
    </form>
</div>

<!-- Résultats -->
<c:choose>
    <c:when test="${not empty results}">
        <div class="results-count">
            <strong>${results.size()}</strong> profil<c:if test="${results.size() > 1}">s</c:if> trouvé<c:if test="${results.size() > 1}">s</c:if>
        </div>

        <div class="search-results-grid">
            <c:forEach var="u" items="${results}" varStatus="loop">
                <div class="result-card" style="animation-delay: ${loop.index * 60}ms">

                    <!-- Bannière colorée -->
                    <div class="result-card-banner">
                        <span class="result-badge">
                            <c:choose>
                                <c:when test="${u.role == 'VIP'}">⭐ VIP</c:when>
                                <c:otherwise>Membre</c:otherwise>
                            </c:choose>
                        </span>
                    </div>

                    <!-- Photo -->
                    <div class="result-avatar-wrap">
                        <img class="result-avatar"
                             src="${not empty u.photoProfil ? u.photoProfil : ctx.concat('/assets/images/default-avatar.png')}"
                             alt="${u.prenom}"
                             onerror="this.src='${ctx}/assets/images/default-avatar.png'">
                    </div>

                    <!-- Infos -->
                    <div class="result-body">
                        <p class="result-name">${u.prenom}, ${u.age} ans</p>
                        <div class="result-meta">
                            <span>${u.sexe}</span>
                            <span class="dot"></span>
                            <span><i class="fa-solid fa-location-dot" style="font-size:0.7rem;"></i> ${u.localisation}</span>
                        </div>
                        <p class="result-bio">
                            <c:choose>
                                <c:when test="${not empty u.bio}">${u.bio}</c:when>
                                <c:otherwise><em>Pas de bio</em></c:otherwise>
                            </c:choose>
                        </p>

                        <!-- Actions -->
                        <div class="result-actions">
                            <a href="${ctx}/app/profile?action=view&id=${u.id}" class="btn-view">
                                <i class="fa-solid fa-user"></i> Voir profil
                            </a>
                            <form action="${ctx}/app/interaction" method="post" style="margin:0;">
                                <input type="hidden" name="destinataireId" value="${u.id}">
                                <input type="hidden" name="action" value="like">
                                <button type="submit" class="btn-like" title="J'aime">
                                    <i class="fa-solid fa-heart"></i>
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:when>

    <c:when test="${results != null && empty results}">
        <div class="empty-state">
            <div class="empty-state-icon"><i class="fa-solid fa-magnifying-glass"></i></div>
            <h3>Aucun résultat</h3>
            <p>Aucun profil ne correspond à vos critères. Essayez d'élargir votre recherche.</p>
        </div>
    </c:when>
</c:choose>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
