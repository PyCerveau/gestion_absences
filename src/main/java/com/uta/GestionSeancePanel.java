package com.uta;

import com.uta.dao.SeanceDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;

public class GestionSeancePanel extends JPanel {
    private Connection connection;

    public GestionSeancePanel(Connection connection) {
        this.connection = connection;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(107, 227, 216)); // Fond vert

        ImageIcon icon = new ImageIcon("src/main/java/com/uta/icons/holidays-and-absence.png"); // Chemin de l'image
        JLabel imageLabel = new JLabel(icon);
        titlePanel.add(imageLabel);

        JLabel titleLabel = new JLabel("Gestion des Utilisateurs", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); // Texte en blanc
        titlePanel.add(titleLabel);

        add(titlePanel, BorderLayout.NORTH);

        // Contenu principal (formulaire à gauche et tableau à droite)
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        // Panneau du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Champs du formulaire
        JLabel enseignantLabel = new JLabel("Enseignant:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(enseignantLabel, gbc);

        JComboBox<String> enseignantBox = new JComboBox<>();
        gbc.gridx = 1;
        formPanel.add(enseignantBox, gbc);

        JLabel moduleLabel = new JLabel("Module:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(moduleLabel, gbc);

        JComboBox<String> moduleBox = new JComboBox<>();
        gbc.gridx = 1;
        formPanel.add(moduleBox, gbc);

        JLabel typeLabel = new JLabel("Type:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(typeLabel, gbc);

        JComboBox<String> typeBox = new JComboBox<>();
        gbc.gridx = 1;
        formPanel.add(typeBox, gbc);

        JLabel heureLabel = new JLabel("Heure:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(heureLabel, gbc);

        JTextField heureField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(heureField, gbc);

        // Champs pour Heure de Début et Heure de Fin
        JLabel heureDebutLabel = new JLabel("Heure Début:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(heureDebutLabel, gbc);

        JTextField heureDebutField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(heureDebutField, gbc);

        JLabel heureFinLabel = new JLabel("Heure Fin:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(heureFinLabel, gbc);

        JTextField heureFinField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(heureFinField, gbc);



        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        JButton addButton = new JButton("Ajouter");
        JButton deleteButton = new JButton("Supprimer");
        JButton modifyButton = new JButton("Modifier");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(modifyButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // Ajouter le formulaire au panneau principal (à gauche)
        mainPanel.add(formPanel);

        // Panneau du tableau
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        String[] columns = {"Enseignant", "Module", "Type", "Heure"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Ajouter le tableau au panneau principal (à droite)
        mainPanel.add(tablePanel);


        table.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                enseignantBox.setSelectedItem(table.getValueAt(selectedRow, 0));
                moduleBox.setSelectedItem(table.getValueAt(selectedRow, 1));
                typeBox.setSelectedItem(table.getValueAt(selectedRow, 2));
                heureField.setText((String) table.getValueAt(selectedRow, 3));
            }
        });


        // Remplir les données des comboBox avec DAO
        SeanceDAO seanceDAO = new SeanceDAO(connection);
        seanceDAO.chargerEnseignant(enseignantBox);
        seanceDAO.chargerModuleEnseignant(moduleBox);
        seanceDAO.chargerType(typeBox);
        seanceDAO.getAllSeances(tableModel);

        addButton.addActionListener(e -> {
            String enseignant = (String) enseignantBox.getSelectedItem();
            String module = (String) moduleBox.getSelectedItem();
            String type = (String) typeBox.getSelectedItem();
            String heureDebut = heureDebutField.getText();
            String heureFin = heureFinField.getText();

            if (enseignant != null && module != null && type != null && !heureDebut.isEmpty() && !heureFin.isEmpty()) {
                try {
                    // Calcul de la durée en heures
                    String[] debutParts = heureDebut.split(":");
                    String[] finParts = heureFin.split(":");

                    int debutHeure = Integer.parseInt(debutParts[0]);
                    int debutMinute = Integer.parseInt(debutParts[1]);
                    int finHeure = Integer.parseInt(finParts[0]);
                    int finMinute = Integer.parseInt(finParts[1]);

                    int dureeMinutes = (finHeure * 60 + finMinute) - (debutHeure * 60 + debutMinute);
                    String heureResultat = (dureeMinutes / 60) + "h" + (dureeMinutes % 60) + "min";

                    // Appel au DAO pour ajouter la séance
                    boolean success = seanceDAO.addSeance(enseignant, module, type, heureDebut, heureFin);

                    if (success) {
                        tableModel.addRow(new Object[]{enseignant, module, type, heureResultat});
                        JOptionPane.showMessageDialog(this, "Séance ajoutée avec succès !", "Information", JOptionPane.INFORMATION_MESSAGE);

                        enseignantBox.setSelectedIndex(0);
                        moduleBox.setSelectedIndex(0);
                        typeBox.setSelectedIndex(0);
                        heureDebutField.setText("");
                        heureFinField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la séance.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Veuillez entrer des heures valides au format HH:mm.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

    }
}
