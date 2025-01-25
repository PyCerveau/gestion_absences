package com.uta.dao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class EtudiantDAO {
    private Connection connection;

    public EtudiantDAO(Connection connection) {
        this.connection = connection;
    }
    public void classeBox(JComboBox<String> comboBox) {
        String query = "SELECT CONCAT(libelle, ' ', specialite, ' ', niveau) AS classeInfo FROM classe";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            comboBox.removeAllItems();
            while (resultSet.next()) {
                String classeInfo = resultSet.getString("classeInfo");
                comboBox.addItem(classeInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des classes : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Ajouter un enseignant
    public void ajouterEtudiant(String mat, String nom, String prenom, String adresse, String email, String classe) {
        String sql = "INSERT INTO etudiant (mat, nom, prenom,adresse, email, idclasse) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, mat);
            stmt.setString(2, nom);
            stmt.setString(3, prenom);
            stmt.setString(4, adresse);
            stmt.setString(5, email);
            // Extraire les IDs des classes et modules depuis les noms concaténés
            stmt.setInt(6, extraireIdClasse(classe));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Etudiant ajoutée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Modifier un enseignant
    public void modifierEtudiant(String mat, String nom, String prenom, String adresse, String email, String classe) {
        String sql = "UPDATE etudiant SET nom = ?, prenom = ?, adresse = ?, email = ?, idclasse = ? WHERE mat = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, adresse);
            stmt.setString(4, email);
            stmt.setInt(5, extraireIdClasse(classe));
            stmt.setString(6, mat);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Etudiant modifié avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la modification : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Supprimer un enseignant
    public void supprimerEtudiant(String mat) {
        String sql = "DELETE FROM etudiant WHERE mat = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, mat);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Etudiant supprimer avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void afficherEtudiant(DefaultTableModel tableModel) {
        // Requête SQL corrigée
        String sql = "SELECT e.mat, e.nom, e.prenom, e.adresse, e.email, " +
                "CONCAT(c.libelle, ' ', c.specialite, ' ', c.niveau) AS classe " +
                "FROM etudiant e " +
                "JOIN classe c ON e.idclasse = c.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Efface les lignes existantes dans le tableau
            tableModel.setRowCount(0);

            // Ajout des lignes depuis la base de données
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("mat"));      // Correction : correspond à la colonne e.mat
                row.add(rs.getString("nom"));
                row.add(rs.getString("prenom"));
                row.add(rs.getString("adresse"));
                row.add(rs.getString("email"));
                row.add(rs.getString("classe"));  // Correspond à l'alias défini dans la requête
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            // Affichage du message d'erreur et log de l'exception
            JOptionPane.showMessageDialog(null, "Erreur lors de l'affichage : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Remplace cela par un logger dans un projet professionnel
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

}
