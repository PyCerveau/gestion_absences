package com.uta.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatistiquesDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_absences";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Méthode pour charger les données depuis la base de données
    public List<String> getAllStatistiques() {
        List<String> statistiques = new ArrayList<>();

        // Requête SQL
        String query = "SELECT CONCAT(c.libelle,' ',c.specialite,' ',c.niveau) AS classe, m.ECUE AS module, COUNT(a.id) AS nombre_absences " +
                "FROM absenceEtu a " +
                "INNER JOIN classe c ON a.classe = c.id " +
                "INNER JOIN module m ON a.module = m.id " +
                "GROUP BY classe, module " +
                "ORDER BY classe, module";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Ajouter les données sous forme de String
            while (rs.next()) {
                String classe = rs.getString("classe");
                String module = rs.getString("module");
                int absences = rs.getInt("nombre_absences");

                // Ajouter les données sous forme d'une ligne de tableau
                statistiques.add(classe + " | " + module + " | " + absences);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statistiques;
    }
}
