package com.uta.dao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class EnseignantDAO {
    private final Connection connection;

    // Constructeur
    public EnseignantDAO(Connection connection) {
        this.connection = connection;
    }

    // Ajouter un enseignant
    public void ajouterEnseignant(String code, String nom, String prenom, String num, String classe, String module) {
        String sql = "INSERT INTO enseignant (code, nom, prenom, num, idclasse, idmod) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.setString(2, nom);
            stmt.setString(3, prenom);
            stmt.setString(4, num);

            // Extraire les IDs des classes et modules depuis les noms concaténés
            stmt.setInt(5, extraireIdClasse(classe));
            stmt.setInt(6, extraireIdModule(module));

            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Modifier un enseignant
    public void modifierEnseignant(String code, String nom, String prenom, String num, String classe, String module) {
        String sql = "UPDATE enseignant SET nom = ?, prenom = ?, num = ?, idclasse = ?, idmod = ? WHERE code = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, num);

            stmt.setInt(4, extraireIdClasse(classe));
            stmt.setInt(5, extraireIdModule(module));
            stmt.setString(6, code);

            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la modification : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Supprimer un enseignant
    public void supprimerEnseignant(String code) {
        String sql = "DELETE FROM enseignant WHERE code = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void afficherEnseignants(DefaultTableModel tableModel) {
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

    // Charger les modules dans une JComboBox
    public void chargerModules(JComboBox<String> comboBox) {
        String sql = "SELECT id, ECUE FROM module";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            comboBox.removeAllItems();
            while (rs.next()) {
                comboBox.addItem(rs.getString("ECUE"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des modules : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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

    // Méthode utilitaire pour extraire l'idmod depuis une valeur concaténée
    private int extraireIdModule(String moduleLibelle) throws SQLException {
        String sql = "SELECT id FROM module WHERE ECUE = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, moduleLibelle);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Module non trouvé : " + moduleLibelle);
                }
            }
        }
    }
}
