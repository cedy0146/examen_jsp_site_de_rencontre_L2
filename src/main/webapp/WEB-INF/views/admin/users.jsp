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
            <li><a href="${ctx}/app/admin"><i class="fa-solid fa-chart-pie"></i> Dashboard</a></li>
            <li><a href="${ctx}/app/admin?action=users" class="active"><i class="fa-solid fa-user"></i> Utilisateurs</a></li>
            <li><a href="${ctx}/app/admin?action=stats"><i class="fa-solid fa-chart-line"></i> Statistiques</a></li>
        </ul>
    </aside>

    <!-- Content -->
    <div>
        <div class="page-header">
            <h1><i class="fa-solid fa-user"></i> Gestion des utilisateurs</h1>
            <p>Administrez les comptes utilisateurs du site.</p>
        </div>

        <c:if test="${param.success == '1'}">
            <div class="alert alert-success"><i class="fa-solid fa-circle-check"></i> Action effectuée avec succès.</div>
        </c:if>

        <div class="card">
            <div class="card-header" style="justify-content: flex-end;">
                <span class="text-muted">${users != null ? users.size() : 0} utilisateur(s)</span>
            </div>
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Photo</th>
                            <th>Nom</th>
                            <th>Email</th>
                            <th>Sexe</th>
                            <th>Localisation</th>
                            <th>Rôle</th>
                            <th>Statut</th>
                            <th>Inscription</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td>${u.id}</td>
                                <td>
                                    <img src="${u.photoProfil != null ? u.photoProfil : ctx.concat('/assets/images/default-avatar.png')}" 
                                         class="profile-photo-sm" alt="" onerror="this.src='${ctx}/assets/images/default-avatar.png'">
                                </td>
                                <td>${u.prenom} ${u.nom}</td>
                                <td>${u.email}</td>
                                <td>${u.sexe}</td>
                                <td>${u.localisation}</td>
                                <td><span class="badge badge-${u.role == 'ADMIN' ? 'danger' : u.role == 'VIP' ? 'warning' : 'secondary'}">${u.role}</span></td>
                                <td><span class="badge badge-${u.statut == 'ACTIF' ? 'success' : u.statut == 'BLOQUE' ? 'danger' : 'secondary'}">${u.statut}</span></td>
                                <td>${u.dateInscription}</td>
                                <td>
                                    <div class="d-flex gap-1">
                                        <c:choose>
                                            <c:when test="${u.statut == 'ACTIF'}">
                                                <form action="${ctx}/app/admin" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="block">
                                                    <input type="hidden" name="userId" value="${u.id}">
                                                    <button type="submit" class="btn btn-sm btn-warning" onclick="return confirm('Bloquer cet utilisateur ?');">Bloquer</button>
                                                </form>
                                            </c:when>
                                            <c:when test="${u.statut == 'BLOQUE'}">
                                                <form action="${ctx}/app/admin" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="unblock">
                                                    <input type="hidden" name="userId" value="${u.id}">
                                                    <button type="submit" class="btn btn-sm btn-success">Débloquer</button>
                                                </form>
                                            </c:when>
                                        </c:choose>
                                        <form action="${ctx}/app/admin" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="userId" value="${u.id}">
                                            <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('SUPPRIMER cet utilisateur ? Cette action est irréversible.');">Supprimer</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
    </div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
