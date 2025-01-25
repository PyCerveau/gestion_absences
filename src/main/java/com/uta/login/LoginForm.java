package com.uta.login;

import javax.swing.*;
import com.uta.GestionAbsencesPage;
import com.uta.dao.AuthentificationDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm {
    private JFrame loginFrame;

    public void showPage() {
        loginFrame = new JFrame("Connexion");
        loginFrame.setSize(500, 400);  // Taille réduite de la fenêtre
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());

        // Panneau principal
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());  // Utilisation de GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        loginPanel.setBackground(new Color(30, 30, 30));  // Fond noir brillant

        // Configuration des contraintes de GridBagLayout
        gbc.insets = new Insets(10, 10, 10, 10);  // Espacement entre les composants

        // Champs de saisie
        JLabel labelUsername = new JLabel("Nom d'utilisateur:");
        labelUsername.setFont(new Font("Arial", Font.BOLD, 16));
        labelUsername.setForeground(Color.WHITE);  // Texte blanc
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(labelUsername, gbc);

        JTextField textFieldUsername = new JTextField();
        textFieldUsername.setFont(new Font("Arial", Font.PLAIN, 14));  // Réduit la taille de la police
        textFieldUsername.setBackground(new Color(50, 50, 50));  // Fond noir brillant
        textFieldUsername.setBackground(Color.WHITE);
        textFieldUsername.setForeground(Color.BLACK); // Texte blanc
        textFieldUsername.setPreferredSize(new Dimension(200, 30));  // Taille réduite du champ
        textFieldUsername.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0)));  // Bordure dorée
        gbc.gridx = 1;
        gbc.gridy = 0;
        loginPanel.add(textFieldUsername, gbc);

        JLabel labelPassword = new JLabel("Mot de passe:");
        labelPassword.setFont(new Font("Arial", Font.BOLD, 16));
        labelPassword.setForeground(Color.WHITE);  // Texte blanc
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(labelPassword, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));  // Réduit la taille de la police
        passwordField.setPreferredSize(new Dimension(200, 30));  // Taille réduite du champ
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0)));
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);  // Bordure dorée
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(passwordField, gbc);

        // Bouton de connexion
        JButton buttonLogin = new JButton("Se connecter");
        buttonLogin.setPreferredSize(new Dimension(200, 40));
        buttonLogin.setBackground(Color.GREEN);
        buttonLogin.setForeground(Color.WHITE);
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textFieldUsername.getText();
                String password = new String(passwordField.getPassword());

                AuthentificationDAO authDAO = new AuthentificationDAO();
                if (authDAO.authenticate(username, password)) {
                    JOptionPane.showMessageDialog(loginFrame, "Connexion réussie !");
                    loginFrame.dispose(); // Fermer la fenêtre de connexion
                    new GestionAbsencesPage("UTA", "Admin", "2023-2024").showPage(); // Ouvrir la page de gestion
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Nom d'utilisateur ou mot de passe incorrect.");
                }
            }
        });

        buttonLogin.setFont(new Font("Arial", Font.BOLD, 16));
        buttonLogin.setBackground(new Color(255, 215, 0));  // Fond doré
        buttonLogin.setForeground(Color.WHITE);  // Texte en blanc
        buttonLogin.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0)));  // Bordure dorée
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;  // Le bouton occupe deux colonnes
        gbc.insets = new Insets(20, 10, 10, 10);  // Espace supplémentaire en bas
        loginPanel.add(buttonLogin, gbc);

        // Associer la touche "Entrée" au bouton "Se connecter"
        loginFrame.getRootPane().setDefaultButton(buttonLogin);

        // Ajout du panneau à la fenêtre
        loginFrame.add(loginPanel, BorderLayout.CENTER);
        loginFrame.setLocationRelativeTo(null); // Centrer la fenêtre
        loginFrame.setVisible(true);
    }
}
