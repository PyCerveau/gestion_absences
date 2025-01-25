package com.uta.dao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class ClasseDAO {

    private final Connection connection;
    public ClasseDAO(Connection connection) {
        this.connection = connection;
    }

    // Ajouter une classe
    public void ajouterClasse(String code, String libelle, String niveau, String specialite) {
        String query = "INSERT INTO classe (code, libelle, niveau, specialite, idAdmin) VALUES (?, ?, ?, ?, 1)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, code);
            stmt.setString(2, libelle);
            stmt.setString(3, niveau);
            stmt.setString(4, specialite);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Classe ajoutée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
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

    public void afficherAbsence(DefaultTableModel tableModel) {
        // Requête SQL bien formatée pour une meilleure lisibilité
        String sql = "SELECT e.code, e.nom, e.prenom, e.num, " +
                "CONCAT(c.libelle, ' ', c.specialite, ' ', c.niveau) AS classe, " +
                "m.ECUE AS module " +
                "FROM enseignant e " +
                "JOIN classe c ON e.idclasse = c.id " +
                "JOIN module m ON e.idmod = m.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Efface les lignes existantes dans le tableau
            tableModel.setRowCount(0);

            // Ajout des lignes depuis la base de données
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("code"));
                row.add(rs.getString("nom"));
                row.add(rs.getString("prenom"));
                row.add(rs.getString("num"));
                row.add(rs.getString("classe"));
                row.add(rs.getString("module"));
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            // Affichage du message d'erreur et log de l'exception
            JOptionPane.showMessageDialog(null, "Erreur lors de l'affichage : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();  // Il est possible de loguer l'exception dans un fichier ou un logger
        }
    }

    public void afficherClasse(DefaultTableModel tableModel) {
        // Requête SQL bien formatée pour une meilleure lisibilité
        String sql = "SELECT code, libelle, niveau, specialite from classe";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Efface les lignes existantes dans le tableau
            tableModel.setRowCount(0);

            // Ajout des lignes depuis la base de données
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("code"));
                row.add(rs.getString("libelle"));
                row.add(rs.getString("niveau"));
                row.add(rs.getString("specialite"));
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            // Affichage du message d'erreur et log de l'exception
            JOptionPane.showMessageDialog(null, "Erreur lors de l'affichage : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();  // Il est possible de loguer l'exception dans un fichier ou un logger
        }
    }
}
