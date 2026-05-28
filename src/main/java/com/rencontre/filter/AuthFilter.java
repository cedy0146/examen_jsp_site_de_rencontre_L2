package com.rencontre.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import com.rencontre.model.Utilisateur;
import com.rencontre.dao.UtilisateurDAO;

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

        boolean isLoggedIn = (session != null && session.getAttribute("utilisateur") != null);

        boolean isPublicResource = requestURI.endsWith("/login")
                || requestURI.endsWith("/register")
                || requestURI.endsWith("/logout")
                || requestURI.contains("/assets/")
                || requestURI.matches(".*\\.(css|js|png|jpg|jpeg|gif|svg)$");

        boolean isAdminRoute = requestURI.contains("/admin");

        if (!isLoggedIn) {
            if (isPublicResource) {
                chain.doFilter(request, response);
                return;
            }
            httpResponse.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        // ✅ CORRIGÉ : try/catch pour ne pas bloquer toutes les pages /app/* si MySQL est lent
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user != null) {
            try {
                new UtilisateurDAO().updateLastActivity(user.getId());
            } catch (Exception e) {
                System.err.println("Impossible de mettre à jour l'activité : " + e.getMessage());
            }
        }

        if (isAdminRoute) {
            if (user == null || !user.isAdmin()) {
                httpResponse.sendRedirect(contextPath + "/app/profile");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}