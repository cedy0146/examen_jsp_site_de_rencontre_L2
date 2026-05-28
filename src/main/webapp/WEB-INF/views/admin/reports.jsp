<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Modération - Signalements" />
</jsp:include>

<div class="page-header">
    <h1><i class="fa-solid fa-flag"></i> Signalements utilisateurs</h1>
    <p>Gérez les signalements et modérez la communauté.</p>
</div>

<div class="card">
    <div class="card-header">
        <h3 class="card-title">Liste des signalements</h3>
    </div>
    <c:choose>
        <c:when test="${not empty signalements}">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Signalant</th>
                        <th>Signalé</th>
                        <th>Motif</th>
                        <th>Description</th>
                        <th>Statut</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="s" items="${signalements}">
                        <tr>
                            <td>${s.id}</td>
                            <td>${s.signalantId}</td>
                            <td>${s.signaleId}</td>
                            <td><span class="badge badge-secondary">${s.motif}</span></td>
                            <td>${not empty s.description ? s.description : '<em class="text-muted">-</em>'}</td>
                            <td>
                                <span class="badge badge-${s.statut == 'EN_ATTENTE' ? 'warning' : s.statut == 'TRAITE' ? 'success' : 'danger'}">${s.statut}</span>
                            </td>
<td><fmt:formatDate value="${s.dateSignalementDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td>
                                <c:if test="${s.statut == 'EN_ATTENTE'}">
                                    <form action="${ctx}/app/admin" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="resolveReport">
                                        <input type="hidden" name="reportId" value="${s.id}">
                                        <button type="submit" class="btn btn-success btn-sm"><i class="fa-solid fa-circle-check"></i> Traiter</button>
                                    </form>
                                    <form action="${ctx}/app/admin" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="rejectReport">
                                        <input type="hidden" name="reportId" value="${s.id}">
                                        <button type="submit" class="btn btn-danger btn-sm"><i class="fa-solid fa-xmark"></i> Rejeter</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p class="text-muted">Aucun signalement à afficher.</p>
        </c:otherwise>
    </c:choose>
</div>

<div class="mt-3">
    <a href="${ctx}/app/admin" class="btn btn-secondary"><i class="fa-solid fa-arrow-left"></i> Retour au tableau de bord</a>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

