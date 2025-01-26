package com.uta.dao;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrintDAO {
    private Connection connection;

    public PrintDAO(Connection connection) {
        this.connection = connection;
    }

    // Charger les classes dans la liste déroulante
    public void chargerClasses(JComboBox<String> classBox) {
        String sql = "SELECT CONCAT(libelle, ' ', specialite, ' ', niveau) AS classe FROM classe";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                classBox.addItem(resultSet.getString("classe"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des classes : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Récupérer les données d'absences selon la classe sélectionnée
    public List<Object[]> getAbsenceData(String selectedClass) {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT ae.mat, ae.nom, ae.prenom, ae.absent, ae.present " +
                "FROM absenceEtu ae " +
                "JOIN classe c ON ae.classe = c.id " +
                "WHERE CONCAT(c.libelle, ' ', c.specialite, ' ', c.niveau) = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, selectedClass);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Object[] row = new Object[5];
                row[0] = resultSet.getString("mat");
                row[1] = resultSet.getString("nom");
                row[2] = resultSet.getString("prenom");
                row[3] = resultSet.getBoolean("absent");
                row[4] = resultSet.getBoolean("present");
                data.add(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des données d'absences : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        return data;
    }

    // Ajouter une nouvelle absence
    public boolean ajouterAbsence(String matricule, boolean absent, boolean present) {
        String sql = "INSERT INTO absenceEtu (mat, absent, present) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricule);
            statement.setBoolean(2, absent);
            statement.setBoolean(3, present);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de l'absence : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Modifier une absence existante
    public boolean modifierAbsence(String matricule, boolean absent, boolean present) {
        String sql = "UPDATE absenceEtu SET absent = ?, present = ? WHERE mat = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, absent);
            statement.setBoolean(2, present);
            statement.setString(3, matricule);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la modification de l'absence : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Supprimer une absence
    public boolean supprimerAbsence(String matricule) {
        String sql = "DELETE FROM absenceEtu WHERE mat = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricule);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de l'absence : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
