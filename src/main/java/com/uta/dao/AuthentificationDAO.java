package com.uta.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthentificationDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_absences"; // URL de la BD MySQL
    private static final String USER = "root"; // Nom d'utilisateur de MySQL (par défaut : root)
    private static final String PASSWORD = ""; // Mot de passe (vide ici, à modifier selon votre configuration)

    // Méthode pour authentifier un utilisateur
    public boolean authenticate(String username, String password) {
        String query = "SELECT * FROM admin WHERE nom = ? AND pwd = ?";

        try {
            // Charger le pilote MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, username);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next(); // Retourne true si un utilisateur est trouvé
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
