package com.uta.dao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class AbsenceDAO {
    private Connection connection;

    public AbsenceDAO(Connection connection) {
        this.connection = connection;
    }

    // Afficher les absences pour une classe donnée
    public void afficherAbsencesParClasse(DefaultTableModel tableModel) {
        try {
            String query = "SELECT mat, nom, prenom, classe, seance, date FROM absenceEtu";
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
                tableModel.addRow(new Object[]{mat, nom, prenom, classe, seance, date});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des absences : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Charger les classes dans une JComboBox
    public void chargerClasses(JComboBox<String> comboBox) {
        String sql = "SELECT id, CONCAT(libelle, ' ', specialite, ' ', niveau) AS classe FROM classe";

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
}
