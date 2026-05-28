<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.utilisateur}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="pageTitle" value="Abonnement" />
</jsp:include>

<div class="page-header">
    <h1><i class="fa-solid fa-credit-card"></i> Mon abonnement</h1>
    <p>Gérez votre abonnement et découvrez nos offres.</p>
</div>

<c:if test="${param.success == '1'}">
    <div class="alert alert-success"><i class="fa-solid fa-circle-check"></i> Votre abonnement a été mis à jour avec succès !</div>
</c:if>

<!-- Abonnement actuel -->
<div class="card mb-3">
    <div class="card-header">
        <h3 class="card-title">&#128221; Abonnement actuel</h3>
    </div>
    <c:choose>
        <c:when test="${not empty abonnements}">
            <c:forEach var="ab" items="${abonnements}">
                <div class="d-flex align-center justify-between" style="padding:1rem;border-bottom:1px solid var(--border-color);">
                    <div>
                        <span class="badge badge-${ab.type == 'VIP' ? 'warning' : ab.type == 'PREMIUM' ? 'info' : 'secondary'}">${ab.type}</span>
                        <span class="badge badge-${ab.actif ? 'success' : 'danger'}">${ab.actif ? 'ACTIF' : ab.statut}</span>
                        <p class="mt-1"><strong>Du :</strong> <fmt:formatDate value="${ab.dateDebutDate}" pattern="dd/MM/yyyy"/></p>
                        <c:if test="${not empty ab.dateFin}">
                            <p><strong>Au :</strong> <fmt:formatDate value="${ab.dateFinDate}" pattern="dd/MM/yyyy"/></p>
                        </c:if>
                        <p><strong>Prix :</strong> ${ab.prix} €</p>
                    </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <p>Vous n'avez pas d'abonnement actif. Choisissez une offre ci-dessous.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Offres -->
<h2 class="mb-2"><i class="fa-solid fa-gift"></i> Nos offres</h2>
<div class="grid grid-3">
    <div class="card" style="text-align:center;">
        <div style="font-size:3rem;"><i class="fa-solid fa-star"></i></div>
        <h3>Gratuit</h3>
        <div style="font-size:2rem;font-weight:bold;color:var(--success-color);">0 €</div>
        <ul style="list-style:none;padding:0;text-align:left;margin:1.5rem 0;">
            <li><i class="fa-solid fa-check"></i> Création de profil</li>
            <li><i class="fa-solid fa-check"></i> Recherche basique</li>
            <li><i class="fa-solid fa-check"></i> 5 likes par jour</li>
            <li><i class="fa-solid fa-xmark"></i> Messagerie illimitée</li>
            <li><i class="fa-solid fa-xmark"></i> Voir qui vous a liké</li>
        </ul>
        <form action="${ctx}/app/subscription" method="post">
            <input type="hidden" name="type" value="GRATUIT">
            <button type="submit" class="btn btn-secondary btn-block">Choisir Gratuit</button>
        </form>
    </div>

    <div class="card" style="text-align:center;border:2px solid var(--info-color);">
        <div style="font-size:3rem;"><i class="fa-solid fa-gem"></i></div>
        <h3>Premium</h3>
        <div style="font-size:2rem;font-weight:bold;color:var(--info-color);">9.99 €<small>/mois</small></div>
        <ul style="list-style:none;padding:0;text-align:left;margin:1.5rem 0;">
            <li><i class="fa-solid fa-check"></i> Tout le pack Gratuit</li>
            <li><i class="fa-solid fa-check"></i> Likes illimités</li>
            <li><i class="fa-solid fa-check"></i> Messagerie illimitée</li>
            <li><i class="fa-solid fa-check"></i> Voir qui vous a liké</li>
            <li><i class="fa-solid fa-xmark"></i> Badge VIP</li>
        </ul>
        <form action="${ctx}/app/subscription" method="post">
            <input type="hidden" name="type" value="PREMIUM">
            <button type="submit" class="btn btn-info btn-block">Choisir Premium</button>
        </form>
    </div>

    <div class="card" style="text-align:center;border:2px solid var(--warning-color);">
        <div style="font-size:3rem;"><i class="fa-solid fa-crown"></i></div>
        <h3>VIP</h3>
        <div style="font-size:2rem;font-weight:bold;color:var(--warning-color);">29.99 €<small>/3 mois</small></div>
        <ul style="list-style:none;padding:0;text-align:left;margin:1.5rem 0;">
            <li><i class="fa-solid fa-check"></i> Tout le pack Premium</li>
            <li><i class="fa-solid fa-check"></i> Badge VIP exclusif</li>
            <li><i class="fa-solid fa-check"></i> Priorité dans les recherches</li>
            <li><i class="fa-solid fa-check"></i> Support prioritaire</li>
            <li><i class="fa-solid fa-check"></i> Statistiques avancées</li>
        </ul>
        <form action="${ctx}/app/subscription" method="post">
            <input type="hidden" name="type" value="VIP">
            <button type="submit" class="btn btn-warning btn-block">Choisir VIP</button>
        </form>
    </div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
