
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Coup de Foudre - Site de Rencontre</title>
    <jsp:include page="/WEB-INF/views/includes/head-assets.jsp" />
</head>
<body class="landing-page">
    <nav class="main-nav landing-nav">
        <div class="nav-container">
            <a href="${ctx}/index.jsp" class="nav-brand">
                <span class="icon-wrap icon-wrap-brand"><i class="fa-solid fa-heart"></i></span>
                Coup de Foudre
            </a>
            <div class="nav-links">
                <a href="${ctx}/login.jsp" class="nav-link"><i class="fa-solid fa-right-to-bracket"></i> Connexion</a>
                <a href="${ctx}/register.jsp" class="btn btn-primary btn-sm"><i class="fa-solid fa-user-plus"></i> S'inscrire</a>
            </div>
        </div>
    </nav>

    <section class="landing-hero">
        <div class="landing-floating-icons">
            <i class="fa-solid fa-heart"></i>
            <i class="fa-solid fa-heart"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-heart"></i>
            <i class="fa-solid fa-sparkles"></i>
            <i class="fa-solid fa-heart"></i>
        </div>
        <h1><i class="fa-solid fa-heart-pulse"></i> Trouvez l'amour</h1>
        <p class="subtitle">Des milliers de célibataires vous attendent. Inscrivez-vous gratuitement et commencez votre histoire.</p>
        <div class="landing-actions">
            <a href="${ctx}/register.jsp" class="btn btn-lg btn-primary"><i class="fa-solid fa-wand-magic-sparkles"></i> Créer un compte gratuit</a>
            <a href="${ctx}/login.jsp" class="btn btn-lg btn-outline landing-btn-outline"><i class="fa-solid fa-right-to-bracket"></i> J'ai déjà un compte</a>
        </div>
    </section>

    <section class="landing-features">
        <h2><i class="fa-solid fa-gem"></i> Pourquoi choisir Coup de Foudre ?</h2>
        <div class="features-grid">
            <div class="feature-item">
                <div class="feature-icon"><i class="fa-solid fa-magnifying-glass"></i></div>
                <h3>Recherche avancée</h3>
                <p>Filtrer par âge, localisation, centres d'intérêt et trouvez la personne idéale.</p>
            </div>
            <div class="feature-item">
                <div class="feature-icon"><i class="fa-solid fa-heart-circle-bolt"></i></div>
                <h3>Matching intelligent</h3>
                <p>Notre algorithme calcule votre compatibilité pour des rencontres pertinentes.</p>
            </div>
            <div class="feature-item">
                <div class="feature-icon"><i class="fa-solid fa-comment-dots"></i></div>
                <h3>Messagerie instantanée</h3>
                <p>Discutez en temps réel avec vos matchs et faites connaissance.</p>
            </div>
        </div>
    </section>

    <footer class="landing-footer">
        <p><i class="fa-regular fa-copyright"></i> 2024 Coup de Foudre - Site de Rencontre. Tous droits réservés.</p>
    </footer>
</body>
</html>
