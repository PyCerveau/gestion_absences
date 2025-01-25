package com.uta;

import com.uta.dao.EnseignantDAO;
import com.uta.dao.EtudiantDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;  // Importation de la classe Connection
import java.sql.SQLException;

public class GestionEtudiant extends JPanel {
    private Connection connection;  // Déclaration de la variable connection

    public GestionEtudiant(Connection connection) {  // Le constructeur prend connection comme paramètre
        this.connection = connection;  // Initialisation de connection
        // Mise en page principale
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 240, 240)); // Couleur de fond principale

// Panel pour le titre et l'image
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(112, 89, 16)); // Fond vert

        // Image à gauche
        ImageIcon icon = new ImageIcon("src/main/java/com/uta/icons/holidays-and-absence.png"); // Remplacez le chemin par le bon chemin de votre image
        JLabel imageLabel = new JLabel(icon);
        titlePanel.add(imageLabel);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Etudiants", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); // Texte en blanc
        titlePanel.add(titleLabel);

        // Ajout du panel du titre au panneau principal
        add(titlePanel, BorderLayout.NORTH);


        // Formulaire à gauche
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Champs du formulaire
        // matricule
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Matricule:"), gbc);

        gbc.gridx = 1;
        JTextField matField = new JTextField();
        matField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(matField, gbc);

        // nom
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        JTextField nomField = new JTextField();
        nomField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(nomField, gbc);

        // prenom
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Prenom:"), gbc);

        gbc.gridx = 1;
        JTextField prenomField = new JTextField();
        prenomField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(prenomField, gbc);

        // adresse
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Adresse:"), gbc);

        gbc.gridx = 1;
        JTextField addField = new JTextField();
        addField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(addField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(emailField, gbc);

        // Champs Classe et Module en ComboBox
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Classe:"), gbc);
        gbc.gridx = 1;
        JComboBox classeComboBox = new JComboBox<>();
        classeComboBox.setPreferredSize(new Dimension(200, 30));
        formPanel.add(classeComboBox, gbc);

        // Boutons Ajouter, Modifier, Supprimer
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBackground(new Color(255, 255, 255));

        JButton addButton = new JButton("Ajouter", new ImageIcon("icons/add.png"));
        JButton editButton = new JButton("Modifier", new ImageIcon("icons/edit.png"));
        JButton deleteButton = new JButton("Supprimer", new ImageIcon("icons/delete.png"));

        // Personnalisation des boutons
        addButton.setBackground(new Color(0, 123, 255));
        addButton.setForeground(Color.GREEN);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.PLAIN, 14));

        editButton.setBackground(new Color(255, 193, 7));
        editButton.setForeground(Color.GREEN);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font("Arial", Font.PLAIN, 14));

        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.GREEN);
        deleteButton.setFocusPainted(false);
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 14));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // Tableau à droite
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.setBackground(new Color(245, 245, 245)); // Couleur de fond du tableau

        String[] columns = {"Matricule","Nom", "Prénom","Adresse", "Email", "Classe"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        // Personnalisation du tableau
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);



        // Événement de clic sur une ligne du tableau
        table.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                matField.setText((String) table.getValueAt(selectedRow, 0));
                nomField.setText((String) table.getValueAt(selectedRow, 1));
                prenomField.setText((String) table.getValueAt(selectedRow, 2));
                addField.setText((String) table.getValueAt(selectedRow, 3));
                emailField.setText((String) table.getValueAt(selectedRow, 4));
                classeComboBox.setSelectedItem((String) table.getValueAt(selectedRow, 5));
            }
        });

        EtudiantDAO etudiantDAO = new EtudiantDAO(connection);
        etudiantDAO.classeBox(classeComboBox);


        // Afficher les enregistrements de la table enseignants
        etudiantDAO.afficherEtudiant(tableModel);


        // Logique des boutons
        // Ajouter
        addButton.addActionListener(e -> {
            String mat = matField.getText();
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String adresse = addField.getText();
            String email = emailField.getText();
            String classe = (String) classeComboBox.getSelectedItem();

            if (!mat.isEmpty() && !nom.isEmpty() && !prenom.isEmpty() && !adresse.isEmpty() && ! email.isEmpty() && !classe.isEmpty()) {
                  // Assurez-vous que cette méthode est correcte
                etudiantDAO.ajouterEtudiant(mat, nom, prenom, adresse, email, classe);
                etudiantDAO.afficherEtudiant(tableModel);
                matField.setText("");
                nomField.setText("");
                prenomField.setText("");
                addField.setText("");
                emailField.setText("");
                classeComboBox.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Modifier
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String mat = matField.getText();
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String adresse = addField.getText();
                String email = emailField.getText();
                String classe = (String) classeComboBox.getSelectedItem();

                if (!mat.isEmpty() && !nom.isEmpty() && !prenom.isEmpty() && !adresse.isEmpty() && !email.isEmpty() && !classe.isEmpty()) {
                   // Assurez-vous que cette méthode est correcte
                    String matTable = (String) table.getValueAt(selectedRow, 0);
                    etudiantDAO.modifierEtudiant(matTable, nom, prenom, adresse, email, classe);
                    etudiantDAO.afficherEtudiant(tableModel);
                    tableModel.setValueAt(mat, selectedRow, 0);
                    tableModel.setValueAt(nom, selectedRow, 1);
                    tableModel.setValueAt(prenom, selectedRow, 2);
                    tableModel.setValueAt(adresse, selectedRow, 3);
                    tableModel.setValueAt(email, selectedRow, 4);
                    tableModel.setValueAt(classe, selectedRow, 5);
                } else {
                    JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Supprimer
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String mat = (String) table.getValueAt(selectedRow, 0);
                etudiantDAO.supprimerEtudiant(mat);
                etudiantDAO.afficherEtudiant(tableModel);
                // Assurez-vous que cette méthode est correcte
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
