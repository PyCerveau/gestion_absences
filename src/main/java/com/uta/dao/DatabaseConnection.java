package com.uta.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // URL JDBC pour MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_absences";  // Modifiez "GAU" par le nom de votre base de données
    private static final String USER = "root";  // Nom d'utilisateur MySQL, modifiez si nécessaire
    private static final String PASSWORD = "";  // Mot de passe MySQL, modifiez si nécessaire

    public static Connection getConnection() throws SQLException {
        // Charger le driver MySQL (inutile depuis Java 8, mais peut être nécessaire pour d'anciennes versions)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL introuvable.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
