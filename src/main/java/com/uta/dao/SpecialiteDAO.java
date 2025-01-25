package com.uta.dao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpecialiteDAO {
    private final Connection connection;

    public  SpecialiteDAO(Connection connection){
        this.connection = connection;
    }

    // Ajouter une classe
    public void ajouterSpecialite(String nomEns, String libSpec) {
        String query = "INSERT INTO classe (code, libelle, niveau, specialite, idAdmin) VALUES (?, ?, ?, ?, 1)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nomEns);
            stmt.setString(2, libSpec);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Specialté ajoutée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de la classe: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modifierClasse(String code, String libelle, String niveau, String specialite) {
        // La requête doit mettre à jour tous les champs sauf le code, en fonction du code existant
        String query = "UPDATE classe SET libelle = ?, niveau = ?, specialite = ? WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // On lie les paramètres dans l'ordre des colonnes à modifier
            stmt.setString(1, libelle);
            stmt.setString(2, niveau);
            stmt.setString(3, specialite);
            stmt.setString(4, code);
            // Utilisation du code commecritère pour trouver la classe à modifier

            // Exécuter la requête et vérifier combien de lignes ont été affectées
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Classe modifiée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la modification de la classe: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }



    // Supprimer une classe
    public void supprimerClasse(String code) {
        String query = "DELETE FROM classe WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, code);
            stmt.executeUpdate();
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Classe suppremée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de la classe: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Afficher les classes
    public void afficherClasses(DefaultTableModel tableModel) {
        String query = "SELECT code, libelle, niveau, specialite FROM classe";
        tableModel.setRowCount(0); // Réinitialiser le tableau
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Object[] row = {
                        rs.getString("code"),
                        rs.getString("libelle"),
                        rs.getString("niveau"),
                        rs.getString("specialite"),
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
