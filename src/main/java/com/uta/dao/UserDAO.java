package com.uta.dao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
        try {
            // Remplacez par vos informations de connexion MySQL
            String url = "jdbc:mysql://localhost:3306/gestion_absences"; // Port par défaut de MySQL : 3306
            String user = "root"; // Nom d'utilisateur par défaut de MySQL
            String password = ""; // Mettez le mot de passe MySQL ici
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour vérifier l'authentification et le rôle de l'utilisateur
    public boolean authenticateUser(String login, String password, String role) {
        String sql = "SELECT * FROM utilisateurs WHERE login = ? AND mot_de_passe = ? AND role = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.setString(3, role);

            ResultSet rs = stmt.executeQuery();

            return rs.next(); // Si un utilisateur est trouvé avec ces informations, l'authentification réussit
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getIdentifiantByLogin(String login) {
        String identifiant = null;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT login FROM utilisateurs WHERE login = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                identifiant = rs.getString("login");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return identifiant;
    }
    // Méthode pour ajouter un utilisateur
    public boolean addUser(String login, String password, String role) {
        String sql = "INSERT INTO utilisateurs (login, mot_de_passe,role) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.setString(3, role);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour modifier un utilisateur
    public boolean updateUser(String login, String password, String role) {
        String sql = "UPDATE utilisateurs SET login = ?, mot_de_passe = ?  WHERE role = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.setString(3, role);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour supprimer un utilisateur
    public boolean deleteUser(String code) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void afficherClasse(DefaultTableModel tableModel) {
        // Requête SQL bien formatée pour une meilleure lisibilité
        String sql = "SELECT login, mot_de_passe, role from utilisateurs";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Efface les lignes existantes dans le tableau
            tableModel.setRowCount(0);

            // Ajout des lignes depuis la base de données
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("login"));
                row.add(rs.getString("mot_de_passe"));
                row.add(rs.getString("role"));
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            // Affichage du message d'erreur et log de l'exception
            JOptionPane.showMessageDialog(null, "Erreur lors de l'affichage : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();  // Il est possible de loguer l'exception dans un fichier ou un logger
        }
    }
}
