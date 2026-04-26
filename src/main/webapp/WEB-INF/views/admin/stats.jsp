<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Administration" />
</jsp:include>

<div class="admin-layout">
    <!-- Sidebar -->
    <aside class="admin-sidebar">
        <h3>&#9881; Administration</h3>
        <ul class="admin-menu">
            <li><a href="${ctx}/app/admin">&#128202; Dashboard</a></li>
            <li><a href="${ctx}/app/admin?action=users">&#128100; Utilisateurs</a></li>
            <li><a href="${ctx}/app/admin?action=stats" class="active">&#128200; Statistiques</a></li>
        </ul>
    </aside>

    <!-- Content -->
    <div>
        <div class="page-header">
            <h1>&#128200; Statistiques globales</h1>
            <p>Analysez les performances du site en temps réel.</p>
        </div>

        <!-- KPI -->
        <div class="grid grid-4 mb-3">
            <div class="stat-card">
                <div class="stat-icon">&#128100;</div>
                <div class="stat-value">${stats != null ? stats.totalUtilisateurs : 0}</div>
                <div class="stat-label">Total utilisateurs</div>
            <div class="stat-card">
                <div class="stat-icon">&#128241;</div>
                <div class="stat-value">${stats != null ? stats.utilisateursActifs : 0}</div>
                <div class="stat-label">Actifs aujourd'hui</div>
            <div class="stat-card">
                <div class="stat-icon">&#128142;</div>
                <div class="stat-value">${stats != null ? stats.totalAbonnementsPremium : 0}</div>
                <div class="stat-label">Abonnés Premium</div>
            <div class="stat-card">
                <div class="stat-icon">&#128081;</div>
                <div class="stat-value">${stats != null ? stats.totalAbonnementsVip : 0}</div>
                <div class="stat-label">Abonnés VIP</div>
        </div>

        <div class="grid grid-2">
            <!-- Revenus -->
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">&#128176; Revenus</h3>
                </div>
                <div class="stat-card" style="box-shadow:none;">
                    <div class="stat-value" style="font-size:2rem;">${stats != null ? stats.revenusTotaux : 0} €</div>
                    <div class="stat-label">Revenus totaux</div>
                <div class="mt-2">
                    <p class="text-muted">Nouveaux utilisateurs ce mois : <strong>${stats != null ? stats.nouveauxUtilisateursMois : 0}</strong></p>
                </div>

            <!-- Interactions -->
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">&#128200; Interactions</h3>
                </div>
                <div style="margin-bottom:1rem;">
                    <div class="d-flex justify-between align-center mb-1">
                        <span>Likes</span>
                        <span class="text-muted">${stats != null ? stats.nombreLikes : 0}</span>
                    </div>
                    <div class="progress-bar">
                        <div class="progress-bar-fill" style="width: ${stats != null && stats.nombreLikes > 0 ? Math.min(stats.nombreLikes * 2, 100) : 0}%;"></div>
                </div>
                <div style="margin-bottom:1rem;">
                    <div class="d-flex justify-between align-center mb-1">
                        <span>Messages</span>
                        <span class="text-muted">${stats != null ? stats.nombreMessages : 0}</span>
                    </div>
                    <div class="progress-bar">
                        <div class="progress-bar-fill" style="width: ${stats != null && stats.nombreMessages > 0 ? Math.min(stats.nombreMessages * 2, 100) : 0}%;"></div>
                </div>
                <div style="margin-bottom:1rem;">
                    <div class="d-flex justify-between align-center mb-1">
                        <span>Matchs</span>
                        <span class="text-muted">${stats != null ? stats.nombreMatchs : 0}</span>
                    </div>
                    <div class="progress-bar">
                        <div class="progress-bar-fill" style="width: ${stats != null && stats.nombreMatchs > 0 ? Math.min(stats.nombreMatchs * 5, 100) : 0}%;"></div>
                </div>
                <div>
                    <div class="d-flex justify-between align-center mb-1">
                        <span>Vues de profil</span>
                        <span class="text-muted">${stats != null ? stats.nombreVues : 0}</span>
                    </div>
                    <div class="progress-bar">
                        <div class="progress-bar-fill" style="width: ${stats != null && stats.nombreVues > 0 ? Math.min(stats.nombreVues, 100) : 0}%;"></div>
                </div>
        </div>

        <!-- Taux de compatibilité -->
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">&#128202; Taux de compatibilité moyen</h3>
            </div>
            <div style="display:flex;align-items:center;gap:2rem;">
                <div style="width:120px;height:120px;border-radius:50%;border:8px solid var(--primary-color);display:flex;align-items:center;justify-content:center;font-size:1.5rem;font-weight:bold;color:var(--primary-color);">
                    ${stats != null ? stats.tauxCompatibiliteMoyen : 0}%
                </div>
                <div>
                    <p>Score moyen de compatibilité entre les utilisateurs matchés.</p>
                    <p class="text-muted">Basé sur les centres d'intérêt communs, la localisation et les préférences.</p>
                </div>
        </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
