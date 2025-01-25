package com.uta.dao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;

public class ModuleDAO {
    private final Connection connection;
    public ModuleDAO(Connection connection){
        this.connection = connection;
    }
    // Ajouter une classe
    public void ajouterModule(String code, String intitule, String ECUE, String semestre) {
        String query = "INSERT INTO module (code, intitule, ECUE, idsem) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, code);
            stmt.setString(2, intitule);
            stmt.setString(3, ECUE);
            stmt.setString(4, semestre);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Classe ajoutée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de la classe: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modifierModule(String code, String intitule, String ECUE, String semestre) {
        // La requête doit mettre à jour tous les champs sauf le code, en fonction du code existant
        String query = "UPDATE module SET intitule = ?, ECUE = ?, idsem = ? WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // On lie les paramètres dans l'ordre des colonnes à modifier
            stmt.setString(1, intitule);
            stmt.setString(2, ECUE);
            stmt.setString(3, semestre);
            stmt.setString(4, code);
            // Utilisation du code commecritère pour trouver la classe à modifier

            // Exécuter la requête et vérifier combien de lignes ont été affectées
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Module modifiée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la modification de la classe: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }



    // Supprimer une classe
    public void supprimerModule(String code) {
        String query = "DELETE FROM module WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, code);
            stmt.executeUpdate();
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Module suppremée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de la classe: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Afficher les classes
    public void afficherModule(DefaultTableModel tableModel) {
        String query = "SELECT code, intitule, ECUE, idsem FROM module";
        tableModel.setRowCount(0); // Réinitialiser le tableau
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Object[] row = {
                        rs.getString("code"),
                        rs.getString("intitule"),
                        rs.getString("ECUE"),
                        rs.getString("idsem"),
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
