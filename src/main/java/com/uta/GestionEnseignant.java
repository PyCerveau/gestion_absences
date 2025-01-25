package com.uta;

import com.uta.dao.EnseignantDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class GestionEnseignant extends JPanel {

    private Connection connection;
    private DefaultTableModel tableModel;
    private JComboBox<String> classeComboBox;
    private JComboBox<String> moduleComboBox;

    public GestionEnseignant() {
        // Connexion à la base de données
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_absences", "root", "");  // Remplacez par vos informations de connexion
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Mise en page principale
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 240, 240));

// Panel pour le titre et l'image
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(63, 46, 113)); // Fond vert

        // Image à gauche
        ImageIcon icon = new ImageIcon("src/main/java/com/uta/icons/holidays-and-absence.png"); // Remplacez le chemin par le bon chemin de votre image
        JLabel imageLabel = new JLabel(icon);
        titlePanel.add(imageLabel);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Enseignants", JLabel.LEFT);
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
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Code:"), gbc);
        gbc.gridx = 1;
        JTextField codeField = new JTextField();
        codeField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(codeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        JTextField nomField = new JTextField();
        nomField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(nomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Prenom:"), gbc);
        gbc.gridx = 1;
        JTextField prenomField = new JTextField();
        prenomField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(prenomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Numero:"), gbc);
        gbc.gridx = 1;
        JTextField numField = new JTextField();
        numField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(numField, gbc);

        // Champs Classe et Module en ComboBox
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Classe:"), gbc);
        gbc.gridx = 1;
        classeComboBox = new JComboBox<>();
        classeComboBox.setPreferredSize(new Dimension(200, 30));
        formPanel.add(classeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Module:"), gbc);
        gbc.gridx = 1;
        moduleComboBox = new JComboBox<>();
        moduleComboBox.setPreferredSize(new Dimension(200, 30));
        formPanel.add(moduleComboBox, gbc);

        // Boutons Ajouter, Modifier, Supprimer
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBackground(new Color(230, 230, 250));

        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");

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
        tablePanel.setBackground(new Color(245, 245, 245));

        String[] columns = {"Code", "Nom", "Prénom", "Numéro", "Classe", "Module"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        // Personnalisation du tableau
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // DAO pour gérer les interactions avec la base de données
        EnseignantDAO enseignantDAO = new EnseignantDAO(connection);

        // Chargement des données dans les ComboBox
        enseignantDAO.chargerClasses(classeComboBox);
        enseignantDAO.chargerModules(moduleComboBox);

        // Afficher les enregistrements de la table enseignants
        enseignantDAO.afficherEnseignants(tableModel);

        // Événement de clic sur une ligne du tableau
        table.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                codeField.setText((String) table.getValueAt(selectedRow, 0));
                nomField.setText((String) table.getValueAt(selectedRow, 1));
                prenomField.setText((String) table.getValueAt(selectedRow, 2));
                numField.setText((String) table.getValueAt(selectedRow, 3));
                classeComboBox.setSelectedItem((String) table.getValueAt(selectedRow, 4));
                moduleComboBox.setSelectedItem((String) table.getValueAt(selectedRow, 5));
            }
        });

        // Logique des boutons
        addButton.addActionListener(e -> {
            String code = codeField.getText();
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String num = numField.getText();
            String classe = (String) classeComboBox.getSelectedItem();
            String module = (String) moduleComboBox.getSelectedItem();

            if (!code.isEmpty() && !nom.isEmpty() && !prenom.isEmpty() && !num.isEmpty() && classe != null && module != null) {
                enseignantDAO.ajouterEnseignant(code, nom, prenom, num, classe, module);
                enseignantDAO.afficherEnseignants(tableModel);
                codeField.setText("");
                nomField.setText("");
                prenomField.setText("");
                numField.setText("");
                classeComboBox.setSelectedIndex(0);
                moduleComboBox.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String code = codeField.getText();
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String num = numField.getText();
                String classe = (String) classeComboBox.getSelectedItem();
                String module = (String) moduleComboBox.getSelectedItem();

                if (!code.isEmpty() && !nom.isEmpty() && !prenom.isEmpty() && !num.isEmpty() && classe != null && module != null) {
                    String codeTable = (String) table.getValueAt(selectedRow, 0);
                    enseignantDAO.modifierEnseignant(codeTable, nom, prenom, num, classe, module);
                    enseignantDAO.afficherEnseignants(tableModel);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String code = (String) table.getValueAt(selectedRow, 0);
                enseignantDAO.supprimerEnseignant(code);
                enseignantDAO.afficherEnseignants(tableModel);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
