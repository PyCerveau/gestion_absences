package com.uta;

import com.uta.dao.AbsenceDAO;
import com.uta.dao.EnregistrementDAO;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;

public class EnregistrerAbsencesPanel extends JPanel {

    public EnregistrerAbsencesPanel(Connection connection) {
        // Mise en page principale
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE); // Couleur de fond principale

        // Panel pour le titre et l'image
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(4, 32, 32)); // Fond vert

        // Image à gauche
        ImageIcon icon = new ImageIcon("src/main/java/com/uta/icons/holidays-and-absence.png");
        JLabel imageLabel = new JLabel(icon);
        titlePanel.add(imageLabel);

        // Titre
        JLabel titleLabel = new JLabel("Enregistrer Absences", JLabel.LEFT);
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
        JTextField matField = new JTextField();
        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();

        // Remplacer les JTextField par des JComboBox
        JComboBox<String> classBox = new JComboBox<>();
        JComboBox<String> seaBox = new JComboBox<>();
        JComboBox<String> dateBox = new JComboBox<>();

        String[] labels = {"Matricule", "Nom:", "Prenom:", "Classe:", "Seance", "Date"};
        Component[] fields = {matField, nomField, prenomField, classBox, seaBox, dateBox};

        // Application des labels et champs de saisie avec des espacements appropriés
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            if (fields[i] instanceof JComboBox) {
                ((JComboBox<?>) fields[i]).setPreferredSize(new Dimension(300, 30)); // Ajuster la taille des combo boxes
                formPanel.add((JComboBox<?>) fields[i], gbc);
            } else {
                JTextField field = (JTextField) fields[i];
                field.setPreferredSize(new Dimension(300, 30)); // Agrandir légèrement les champs
                field.setFont(new Font("Arial", Font.PLAIN, 14)); // Police agréable pour la lecture
                formPanel.add(field, gbc);
            }
        }

        // Boutons Ajouter, Modifier, Supprimer
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");

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

        EnregistrementDAO absenceDAO = new EnregistrementDAO(connection);
        EnregistrementDAO enregistrementDAO = new EnregistrementDAO(connection);

        // Chargement des données dans les JComboBox
        absenceDAO.chargerClasses(classBox);
        enregistrementDAO.chargerSeances(seaBox);
        enregistrementDAO.chargerDates(dateBox);

        // Ajout des boutons au panel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Ajout du panel de boutons dans le formulaire
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // Tableau à droite
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.setBackground(new Color(245, 245, 245));

        String[] columns = {"Matricule", "Nom", "Prenom", "Classe", "Seance", "Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        // Personnalisation du tableau
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        // Ajuster la taille du tableau

        // Lorsque le matricule est saisi dans le champ de texte, récupérer les informations de l'étudiant
        matField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                // Appeler la méthode pour remplir les champs nom et prénom
                String matricule = matField.getText();
                enregistrementDAO.recupererInfoParMatricule(matricule, nomField, prenomField);
            }
        });


        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Événement de clic sur une ligne du tableau
        table.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                matField.setText((String) table.getValueAt(selectedRow, 0));
                nomField.setText((String) table.getValueAt(selectedRow, 1));
                prenomField.setText((String) table.getValueAt(selectedRow, 2));
                classBox.setSelectedItem(table.getValueAt(selectedRow, 3));
                seaBox.setSelectedItem(table.getValueAt(selectedRow, 4));
                dateBox.setSelectedItem(table.getValueAt(selectedRow, 5));
            }
        });

        enregistrementDAO.afficherAbsencesParClasse(tableModel);

        // Ajouter un ActionListener au bouton pour afficher les absences
        addButton.addActionListener(e -> {
            String mat = matField.getText();
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String classe = (String) classBox.getSelectedItem();
            String seance = (String) seaBox.getSelectedItem();
            String date = (String) dateBox.getSelectedItem();

            if (!mat.isEmpty() && !nom.isEmpty() && !prenom.isEmpty() && classe != null && seance != null && date != null) {
                tableModel.addRow(new Object[]{mat, nom, prenom, classe, seance, date});
                enregistrementDAO.ajouterAbsence(mat, nomField, prenomField, classe, seance, date);
                matField.setText("");
                nomField.setText("");
                prenomField.setText("");
                classBox.setSelectedIndex(0);
                seaBox.setSelectedIndex(0);
                dateBox.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();  // Sélectionner la ligne
            if (selectedRow != -1) {
                String mat = matField.getText();
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String classe = (String) classBox.getSelectedItem();
                String seance = (String) seaBox.getSelectedItem();
                String date = (String) dateBox.getSelectedItem();

                if (!mat.isEmpty() && !nom.isEmpty() && !prenom.isEmpty() && classe != null && seance != null && date != null) {
                    String matTable = (String) table.getValueAt(selectedRow, 0);
                    enregistrementDAO.modifierAbsence(matTable, nom, prenom, classe, seance, date);
                    enregistrementDAO.afficherAbsencesParClasse(tableModel);
                    JOptionPane.showMessageDialog(this, "Modification effectuée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String code = (String) table.getValueAt(selectedRow, 0);
                enregistrementDAO.supprimerAbsence(code);
                enregistrementDAO.afficherAbsencesParClasse(tableModel);
                JOptionPane.showMessageDialog(this, "Suppression effectuée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
