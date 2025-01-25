package com.uta.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsulterDAO {
    private Connection connection;

    public ConsulterDAO(Connection connection) {
        this.connection = connection;
    }
    public List<Object[]> getEtudiantsAbsents() throws SQLException {
        List<Object[]> etudiants = new ArrayList<>();
        String query = "SELECT \n" +
                "ae.nom ,\n" +
                "ae.prenom ,\n" +
                "CONCAT(TIMESTAMPDIFF(HOUR, s.heureDebut, s.heureFin),'h' ) as date\n" +
                "FROM absenceEtu ae\n" +
                "JOIN classe c ON ae.classe = c.id\n" +
                "JOIN seance s ON ae.seance = s.id";

//                "JOIN absence a ON ae.id = a.idetu;\n";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                etudiants.add(new Object[]{rs.getString("nom"), rs.getString("prenom"), rs.getString("date")});
            }
        }

        return etudiants;
    }
    public String getClasseEtudiant(String nom, String prenom) throws SQLException {
        String query = "SELECT CONCAT(c.libelle, ' ', c.specialite, ' ', c.niveau) AS nom_classe " +
                "FROM etudiant e " +
                "JOIN classe c ON e.idclasse = c.id " +
                "WHERE e.nom = ? AND e.prenom = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom_classe"); // Retourne la concaténation
                }
            }
        }
        return null; // Retourne null si la classe n'est pas trouvée
    }

    public String getDetailsAbsence(String nom, String prenom) throws SQLException {
        StringBuilder details = new StringBuilder();

        // Première requête pour obtenir l'ID de l'étudiant
        String getIdQuery = "SELECT id FROM etudiant WHERE nom = ? AND prenom = ?";
        int idetu = -1;

        try (PreparedStatement stmt = connection.prepareStatement(getIdQuery)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idetu = rs.getInt("id");  // Récupérer l'ID de l'étudiant
                }
            }
        }

        // Si l'ID de l'étudiant existe, on interroge la table des absences
        if (idetu != -1) {
            String query = "SELECT idmod, date FROM absence WHERE idetu = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, idetu);  // Utiliser l'ID de l'étudiant pour la requête

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        details.append("Séance : ").append(rs.getInt("idmod"))
                                .append(", Date : ").append(rs.getDate("date")).append("\n");
                    }
                }
            }
        } else {
            details.append("Aucun étudiant trouvé avec ce nom et prénom.");
        }

        return details.toString();  // Retourner les détails sous forme de chaîne
    }





    // Charger les noms, prénoms et dates des absents par classe
    public List<String[]> getAbsencesByClasse(int idClasse) throws SQLException {
        List<String[]> absences = new ArrayList<>();
        String query = " SELECT e.nom, e.prenom, a.date "+
            "FROM absence a "+
            "JOIN etudiant e ON a.idetu = e.id "+
            "WHERE a.idclasse = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idClasse);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String[] absence = {
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("date")
                };
                absences.add(absence);
            }
        }
        return absences;
    }

    // Charger toutes les classes
    public List<String[]> getClasses() throws SQLException {
        List<String[]> classes = new ArrayList<>();
        String query = "SELECT id, libelle FROM classe";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                String[] classe = {
                        String.valueOf(rs.getInt("id")),
                        rs.getString("libelle")
                };
                classes.add(classe);
            }
        }
        return classes;
    }
}
