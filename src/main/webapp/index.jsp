
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Coup de Foudre - Site de Rencontre</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
</head>
<body class="landing-page">
    <nav class="main-nav" style="background: rgba(0,0,0,0.2);">
        <div class="nav-container">
            <a href="${ctx}/index.jsp" class="nav-brand">&#10084; Coup de Foudre</a>
            <div class="nav-links">
                <a href="${ctx}/login.jsp" class="nav-link">Connexion</a>
                <a href="${ctx}/register.jsp" class="btn btn-primary btn-sm">S'inscrire</a>
            </div>
        </div>
    </nav>

    <section class="landing-hero">
        <h1>Trouvez l'amour</h1>
        <p>Des milliers de célibataires vous attendent. Inscrivez-vous gratuitement et commencez votre histoire.</p>
        <div class="landing-actions">
            <a href="${ctx}/register.jsp" class="btn btn-lg btn-primary">Créer un compte gratuit</a>
            <a href="${ctx}/login.jsp" class="btn btn-lg btn-outline" style="color:white;border-color:white;">J'ai déjà un compte</a>
        </div>
    </section>

    <section class="landing-features">
        <h2>Pourquoi choisir Coup de Foudre ?</h2>
        <div class="features-grid">
            <div class="feature-item">
                <div class="feature-icon">&#128269;</div>
                <h3>Recherche avancée</h3>
                <p>Filtrer par âge, localisation, centres d'intérêt et trouvez la personne idéale.</p>
            </div>
            <div class="feature-item">
                <div class="feature-icon">&#128150;</div>
                <h3>Matching intelligent</h3>
                <p>Notre algorithme calcule votre compatibilité pour des rencontres pertinentes.</p>
            </div>
            <div class="feature-item">
                <div class="feature-icon">&#128172;</div>
                <h3>Messagerie instantanée</h3>
                <p>Discutez en temps réel avec vos matchs et faites connaissance.</p>
            </div>
        </div>
    </section>

    <footer class="landing-footer">
        <p>&copy; 2024 Coup de Foudre - Site de Rencontre. Tous droits réservés.</p>
    </footer>
</body>
</html>

