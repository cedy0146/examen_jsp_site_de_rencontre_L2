package com.rencontre.rest;

import com.rencontre.dao.UtilisateurDAO;
import com.rencontre.model.Utilisateur;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * API REST pour les utilisateurs (JSON).
 */
@WebServlet("/api/users/*")
public class RestUserServlet extends HttpServlet {

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Utilisateur> users = utilisateurDAO.findAll();
            JSONArray array = new JSONArray();
            for (Utilisateur u : users) {
                array.put(userToJson(u));
            }
            out.print(array.toString());
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Utilisateur u = utilisateurDAO.findById(id);
                if (u != null) {
                    out.print(userToJson(u).toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"User not found\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Invalid ID\"}");
            }
        }
    }

    private JSONObject userToJson(Utilisateur u) {
        JSONObject obj = new JSONObject();
        obj.put("id", u.getId());
        obj.put("email", u.getEmail());
        obj.put("nom", u.getNom());
        obj.put("prenom", u.getPrenom());
        obj.put("nomComplet", u.getNomComplet());
        obj.put("age", u.getAge());
        obj.put("sexe", u.getSexe());
        obj.put("localisation", u.getLocalisation());
        obj.put("bio", u.getBio());
        obj.put("role", u.getRole());
        obj.put("photoProfil", u.getPhotoProfil());
        return obj;
    }
}
