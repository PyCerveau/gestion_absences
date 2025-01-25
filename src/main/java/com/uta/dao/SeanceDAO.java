package com.uta.dao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceDAO {

    private final Connection connection;

    public SeanceDAO(Connection connection){
        this.connection = connection;
    }

    // Méthode pour récupérer toutes les séances
    // Méthode pour récupérer toutes les séances
    public List<String> getAllSeances(DefaultTableModel tableModel) {
        List<String> seances = new ArrayList<>();

        // Requête SQL corrigée
        String query = "SELECT CONCAT(e.nom, ' ', e.prenom) AS enseignant, " +
                "CONCAT(m.intitule, ' ', m.ECUE) AS module, " +
                "m.ECUE AS type, " +
                "CONCAT(TIMESTAMPDIFF(HOUR, s.heureDebut, s.heureFin), 'h') AS heure " +
                "FROM seance s " +
                "JOIN enseignant e ON s.idens = e.id " +
                "JOIN module m ON s.idmod = m.id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Réinitialisation du tableau
            tableModel.setRowCount(0);

            // Parcourir les résultats
            while (rs.next()) {
                String enseignant = rs.getString("enseignant");
                String module = rs.getString("module");
                String type = rs.getString("type");
                String heure = rs.getString("heure");

                // Ajouter chaque ligne au tableau
                tableModel.addRow(new Object[]{enseignant, module, type, heure});

                // Ajouter également à la liste si nécessaire
                seances.add(enseignant + " | " + module + " | " + type + " | " + heure);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seances;
    }


    public boolean addSeance(String enseignant, String module, String type, String heureDebut, String heureFin) {
        String query = "INSERT INTO seance (idens, idmod, type, heureDebut, heureFin, date) VALUES (?, ?, ?, ?, ?, ?)";

        // Recherche de l'ID de l'enseignant
        int idEnseignant = getIdEnseignant(enseignant);
        // Recherche de l'ID du module
        int idModule = getIdModule(module);

        if (idEnseignant == -1 || idModule == -1) {
            // Si l'enseignant ou le module n'existe pas, renvoyer false
            return false;
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Utilisation des IDs en tant qu'entiers pour les clés étrangères
            stmt.setInt(1, idEnseignant);
            stmt.setInt(2, idModule);
            stmt.setString(3, type);
            stmt.setString(4, heureDebut + ":00"); // Format heure:min:sec
            stmt.setString(5, heureFin + ":00");  // Format heure:min:sec
            stmt.setDate(6, java.sql.Date.valueOf(java.time.LocalDate.now())); // Date actuelle

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Méthode pour récupérer l'ID de l'enseignant à partir de son nom
    private int getIdEnseignant(String enseignant) {
        String query = "SELECT id FROM enseignant WHERE nom = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, enseignant);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retourner -1 si l'enseignant n'est pas trouvé
    }

    // Méthode pour récupérer l'ID du module à partir de son nom
    private int getIdModule(String module) {
        String query = "SELECT id FROM module WHERE ECUE = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, module);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retourner -1 si le module n'est pas trouvé
    }




    // Charger les modules dans une JComboBox
    public void chargerEnseignant(JComboBox<String> comboBox) {
        String sql = "SELECT CONCAT(nom,' ',prenom) as enseignant FROM enseignant";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            comboBox.removeAllItems();
            while (rs.next()) {
                comboBox.addItem(rs.getString("enseignant"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des modules : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void chargerModuleEnseignant(JComboBox<String> comboBox){
        String sql = "SELECT CONCAT(m.intitule,' ',m.ECUE) AS module " +
                "FROM enseignant e " +
                "JOIN module m  ON e.idmod = m.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            comboBox.removeAllItems();
            while (rs.next()) {
                comboBox.addItem(rs.getString("module"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des modules : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void chargerType(JComboBox comboBox){
        String sql = "SELECT m.ECUE as type FROM enseignant e JOIN module m ON e.idmod = m.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            comboBox.removeAllItems();
            while (rs.next()) {
                comboBox.addItem(rs.getString("type"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des modules : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Méthode utilitaire pour extraire l'idmod depuis une valeur concaténée
    private int extraireIdEnseignant(String moduleLibelle) throws SQLException {
        String sql = "SELECT id FROM enseignant WHERE nom = ?";
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
