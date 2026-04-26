package com.rencontre.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import com.rencontre.model.Utilisateur;
import com.rencontre.dao.UtilisateurDAO;

/**
 * Filtre d'authentification : protege les routes /app/*.
 * Redirige vers la page de connexion si l'utilisateur n'est pas authentifie.
 */
@WebFilter("/app/*")
public class AuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // Verifier si l'utilisateur est connecte
        boolean isLoggedIn = (session != null && session.getAttribute("utilisateur") != null);
        
        // Routes publiques (pas besoin d'etre connecte)
        boolean isPublicResource = requestURI.endsWith("/login") 
                || requestURI.endsWith("/register")
                || requestURI.endsWith("/logout")
                || requestURI.contains("/assets/")
                || requestURI.matches(".*\\.(css|js|png|jpg|jpeg|gif|svg)$");
        
        // Routes admin
        boolean isAdminRoute = requestURI.contains("/admin");
        
        // Si on essaie d'accéder à /app/ sans être connecté
        if (!isLoggedIn) {
            if (isPublicResource) {
                chain.doFilter(request, response);
                return;
            }
            httpResponse.sendRedirect(contextPath + "/login.html");
            return;
        }
        
        // Mettre à jour la dernière activité
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user != null) {
            new UtilisateurDAO().updateLastActivity(user.getId());
        }
        
        // Verifier les droits admin
        if (isAdminRoute) {
            if (!user.isAdmin()) {
                httpResponse.sendRedirect(contextPath + "/app/profile");
                return;
            }
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {}
}
