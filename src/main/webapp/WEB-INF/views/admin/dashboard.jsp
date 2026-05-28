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
        <h3><i class="fa-solid fa-gear"></i> Administration</h3>
        <ul class="admin-menu">
            <li><a href="${ctx}/app/admin" class="active"><i class="fa-solid fa-chart-pie"></i> Dashboard</a></li>
            <li><a href="${ctx}/app/admin?action=users"><i class="fa-solid fa-user"></i> Utilisateurs</a></li>
            <li><a href="${ctx}/app/admin?action=stats"><i class="fa-solid fa-chart-line"></i> Statistiques</a></li>
        </ul>
    </aside>

    <!-- Content -->
    <div>
        <div class="page-header">
            <h1><i class="fa-solid fa-chart-pie"></i> Tableau de bord Admin</h1>
            <p>Vue d'ensemble de l'activité du site.</p>
        </div>

        <!-- KPI -->
        <div class="grid grid-4 mb-3">
            <div class="stat-card">
                <div class="stat-icon"><i class="fa-solid fa-user"></i></div>
                <div class="stat-value">${stats != null ? stats.totalUtilisateurs : 0}</div>
                <div class="stat-label">Utilisateurs</div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fa-solid fa-gem"></i></div>
                <div class="stat-value">${stats != null ? stats.totalAbonnementsPremium : 0}</div>
                <div class="stat-label">Premium</div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fa-solid fa-crown"></i></div>
                <div class="stat-value">${stats != null ? stats.totalAbonnementsVip : 0}</div>
                <div class="stat-label">VIP</div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fa-solid fa-coins"></i></div>
                <div class="stat-value">${stats != null ? stats.revenusTotaux : 0} €</div>
                <div class="stat-label">Revenus</div>
        </div>

        <div class="grid grid-2">
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title"><i class="fa-solid fa-chart-line"></i> Activité récente</h3>
                </div>
                <div class="grid grid-2">
                    <div class="stat-card" style="box-shadow:none;">
                        <div class="stat-value" style="font-size:1.5rem;">${stats != null ? stats.utilisateursActifs : 0}</div>
                        <div class="stat-label">Actifs aujourd'hui</div>
                    <div class="stat-card" style="box-shadow:none;">
                        <div class="stat-value" style="font-size:1.5rem;">${stats != null ? stats.nouveauxUtilisateursMois : 0}</div>
                        <div class="stat-label">Nouveaux ce mois</div>
                </div>

            <div class="card">
                <div class="card-header">
                    <h3 class="card-title"><i class="fa-solid fa-gear"></i> Actions rapides</h3>
                </div>
                <div class="d-flex gap-2" style="flex-wrap:wrap;">
                    <a href="${ctx}/app/admin?action=users" class="btn btn-primary">Gérer les utilisateurs</a>
                    <a href="${ctx}/app/admin?action=stats" class="btn btn-secondary">Voir les statistiques</a>
                </div>
        </div>

        <!-- Derniers utilisateurs -->
        <div class="card">
            <div class="card-header">
                <h3 class="card-title"><i class="fa-solid fa-user"></i> Derniers utilisateurs inscrits</h3>
                <a href="${ctx}/app/admin?action=users" class="btn btn-sm btn-outline-primary">Voir tout</a>
            </div>
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nom</th>
                            <th>Email</th>
                            <th>Rôle</th>
                            <th>Statut</th>
                            <th>Inscription</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${users}" varStatus="status">
                            <c:if test="${status.index < 10}">
                                <tr>
                                    <td>${u.id}</td>
                                    <td>${u.prenom} ${u.nom}</td>
                                    <td>${u.email}</td>
                                    <td><span class="badge badge-${u.role == 'ADMIN' ? 'danger' : u.role == 'VIP' ? 'warning' : 'secondary'}">${u.role}</span></td>
                                    <td><span class="badge badge-${u.statut == 'ACTIF' ? 'success' : u.statut == 'BLOQUE' ? 'danger' : 'secondary'}">${u.statut}</span></td>
                                    <td>${u.dateInscription}</td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
    </div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
