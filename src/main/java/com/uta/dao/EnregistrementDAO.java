package com.uta.dao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class EnregistrementDAO {
    private Connection connection;

    public EnregistrementDAO(Connection connection){
        this.connection = connection;
    }

    // Afficher les absences pour une classe donnée
    public void afficherAbsencesParClasse(DefaultTableModel tableModel) {
        try {
            // Requête SQL avec jointures pour obtenir les informations de la classe et de la séance
            String query = "SELECT ae.mat, ae.nom, ae.prenom, " +
                    "CONCAT(c.libelle, ' ', c.specialite, ' ', c.niveau) AS classe, " +
                    "CONCAT(s.type, '-', DATE_FORMAT(s.heureDebut, '%H')-DATE_FORMAT(s.heureFin, '%H'), 'h') AS seance, " +
                    "ae.date FROM absenceEtu ae " +
                    "JOIN classe c ON ae.classe = c.id " +
                    "JOIN seance s ON ae.seance = s.id ";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();


            // Vider les anciennes données
            tableModel.setRowCount(0);

            // Ajouter les nouvelles données
            while (resultSet.next()) {
                String mat = resultSet.getString("mat");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String classe = resultSet.getString("classe");
                String seance = resultSet.getString("seance");
                String date = resultSet.getString("date");

                // Ajouter une nouvelle ligne au tableau avec les données formatées
                tableModel.addRow(new Object[]{mat, nom, prenom, classe, seance, date});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des absences : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode utilitaire pour extraire l'idclasse depuis une valeur concaténée
    private int extraireIdClasse(String classeConcat) throws SQLException {
        String sql = "SELECT id FROM classe WHERE CONCAT(libelle, ' ', specialite, ' ', niveau) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, classeConcat);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Classe non trouvée : " + classeConcat);
                }
            }
        }
    }
    // Méthode utilitaire pour extraire l'idseance depuis une valeur concaténée
    private int extraireIdSeance(String seanceConcat) throws SQLException {
        String sql = "SELECT id FROM seance WHERE CONCAT(type, '-', DATE_FORMAT(heureFin, '%H') - DATE_FORMAT(heureDebut, '%H'), 'h') = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, seanceConcat);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Séance non trouvée : " + seanceConcat);
                }
            }
        }
    }


    // Gérer la sélection de classe et afficher les absences correspondantes
    public void gererSelectionClasse(String selectedClass, DefaultTableModel tableModel) {
        if (selectedClass != null) {
            try {
                // Extraire l'ID de la classe
                int classeId = extraireIdClasse(selectedClass);

                // Vérifier si l'ID est valide
                if (classeId > 0) {
                    // Afficher les absences pour cette classe
                    afficherAbsencesParClasse(tableModel);
                } else {
                    JOptionPane.showMessageDialog(null, "Classe introuvable pour la sélection : " + selectedClass, "Erreur", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erreur lors de la gestion de la classe sélectionnée : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Aucune classe sélectionnée.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void ajouterAbsence(String matricule, JTextField nomField, JTextField prenomField, String classe, String seance, String date) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();

        if (nom.isEmpty() || prenom.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nom ou prénom manquant.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Vérifier si l'étudiant existe déjà dans la base de données
            String checkStudentQuery = "SELECT COUNT(*) FROM etudiant WHERE mat = ?";
            PreparedStatement checkStudentStmt = connection.prepareStatement(checkStudentQuery);
            checkStudentStmt.setString(1, matricule);
            ResultSet resultSet = checkStudentStmt.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) == 0) {
                JOptionPane.showMessageDialog(null, "Étudiant non trouvé dans la base de données.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int classeId = extraireIdClasse(classe);
            int seanceId = extraireIdSeance(seance);

            String query = "INSERT INTO absenceEtu (mat, nom, prenom, classe, seance, date) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, matricule);
            statement.setString(2, nom);
            statement.setString(3, prenom);
            statement.setInt(4, classeId);
            statement.setInt(5, seanceId);
            statement.setString(6, date);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Absence ajoutée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de l'absence.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {

        }
    }



    public void modifierAbsence(String mat, String nom, String prenom, String classeNom, String seanceNom, String date) {
        try {
            String classeQuery = "SELECT id FROM classe WHERE CONCAT(libelle, ' ', specialite, ' ', niveau) = ?";
            PreparedStatement stmtClasse = connection.prepareStatement(classeQuery);
            stmtClasse.setString(1, classeNom);
            ResultSet rsClasse = stmtClasse.executeQuery();

            String seanceQuery = "SELECT id FROM seance WHERE CONCAT(type, '-', DATE_FORMAT(heureDebut, '%H'), '-', DATE_FORMAT(heureFin, '%H')) = ?";
            PreparedStatement stmtSeance = connection.prepareStatement(seanceQuery);
            stmtSeance.setString(1, seanceNom);
            ResultSet rsSeance = stmtSeance.executeQuery();


            if (rsClasse.next() && rsSeance.next()) {
                int classeId = rsClasse.getInt("id"); // ID de la classe
                int seanceId = rsSeance.getInt("id"); // ID de la séance

                // Requête pour modifier l'absence
                String query = "UPDATE absenceEtu SET nom = ?, prenom = ?, classe = ?, seance = ?, date = ? WHERE mat = ?";
                PreparedStatement statement = connection.prepareStatement(query);

                statement.setString(1, nom);
                statement.setString(2, prenom);
                statement.setInt(3, classeId); // Utiliser l'ID de la classe ici
                statement.setInt(4, seanceId); // Utiliser l'ID de la séance ici
                statement.setString(5, date);
                statement.setString(6, mat);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Absence modifiée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour de l'absence.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Classe ou Séance non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour de l'absence : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Méthode pour supprimer une absence dans la base de données et dans le tableau
    public void supprimerAbsence(String code) {
        try {
            String query = "DELETE FROM absenceEtu WHERE mat = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, code);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Absence supprimée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de l'absence.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de l'absence : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Charger les classes dans une JComboBox
    public void chargerClasses(JComboBox<String> comboBox) {
        String sql = "SELECT CONCAT(c.libelle, ' ', c.specialite, ' ', c.niveau) AS classe " +
                "FROM absenceEtu ae " +
                "JOIN classe c ON ae.classe = c.id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            comboBox.removeAllItems();
            while (rs.next()) {
                comboBox.addItem(rs.getString("classe"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des classes : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Méthode pour charger les séances
    public void chargerSeances(JComboBox<String> seaBox) {
        try {
            String query = "SELECT CONCAT(type,'-',DATE_FORMAT(heureFin, '%H')-DATE_FORMAT(heureDebut, '%H'),'h') as seance FROM seance";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                seaBox.addItem(rs.getString("seance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour charger les dates
    public void chargerDates(JComboBox<String> dateBox) {
        try {
            String query = "SELECT DISTINCT date FROM absence";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                dateBox.addItem(rs.getString("date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Méthode pour récupérer les informations d'un étudiant par matricule
    public void recupererInfoParMatricule(String matricule, JTextField nomField, JTextField prenomField) {
        try {
            String query = "SELECT nom, prenom FROM etudiant WHERE mat = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, matricule);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");

                // Affichage des informations dans les champs de texte
                nomField.setText(nom);
                prenomField.setText(prenom);
            } else {
                JOptionPane.showMessageDialog(null, "Matricule introuvable.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des informations : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }

}
