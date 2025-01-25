package com.uta;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.uta.dao.ClasseDAO;
import java.awt.*;
import java.sql.Connection;

public class GestionClasse extends JPanel {

    public GestionClasse(Connection connection) {
        // Mise en page principale
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE); // Couleur de fond principale

// Panel pour le titre et l'image
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(68, 69, 28)); // Fond vert

        // Image à gauche
        ImageIcon icon = new ImageIcon("src/main/java/com/uta/icons/holidays-and-absence.png"); // Remplacez le chemin par le bon chemin de votre image
        JLabel imageLabel = new JLabel(icon);
        titlePanel.add(imageLabel);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Classes", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); // Texte en blanc
        titlePanel.add(titleLabel);

        // Ajout du panel titre à la position North du JPanel principal
        add(titlePanel, BorderLayout.NORTH);

        // Formulaire à gauche
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Champs du formulaire
        JTextField codeField = new JTextField();
        JTextField libelleField = new JTextField();
        JTextField niveauField = new JTextField();
        JTextField specialiteField = new JTextField();
        JTextField adminField = new JTextField();

        String[] labels = {"Code:", "Libellé:", "Niveau:", "Spécialité:"};
        JTextField[] fields = {codeField, libelleField, niveauField, specialiteField, adminField};

        // Application des labels et champs de saisie avec des espacements appropriés
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            fields[i].setPreferredSize(new Dimension(300, 30)); // Agrandir légèrement les champs
            fields[i].setFont(new Font("Arial", Font.PLAIN, 14)); // Police agréable pour la lecture
            formPanel.add(fields[i], gbc);
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

        // Ajout des boutons au panel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Ajout du panel de boutons dans le formulaire
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // Tableau à droite
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.setBackground(new Color(245, 245, 245));

        String[] columns = {"Code", "Libellé", "Niveau", "Spécialité"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        // Personnalisation du tableau
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // ClasseDAO pour gérer les interactions avec la base de données
        ClasseDAO classeDAO = new ClasseDAO(connection);

        // Afficher les enregistrements de la table classe par défaut
        classeDAO.afficherClasse(tableModel);

        // Événement de clic sur une ligne du tableau
        table.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                codeField.setText((String) table.getValueAt(selectedRow, 0));
                libelleField.setText((String) table.getValueAt(selectedRow, 1));
                niveauField.setText((String) table.getValueAt(selectedRow, 2));
                specialiteField.setText((String) table.getValueAt(selectedRow, 3));
            }
        });

        addButton.addActionListener(e -> {
            String code = codeField.getText();
            String libelle = libelleField.getText();
            String niveau = niveauField.getText();
            String specialite = specialiteField.getText();
            String admin = adminField.getText(); // Vcous pouvez ignorer cette ligne si 'admin' n'est pas utilisé.

            if (!code.isEmpty() && !libelle.isEmpty() && !niveau.isEmpty() && !specialite.isEmpty()) {
                // Ajouter dans la base de données
                classeDAO.ajouterClasse(code, libelle, niveau, specialite);

                // Ajouter directement au tableau
                tableModel.addRow(new Object[]{code, libelle, niveau, specialite});

                // Réinitialiser les champs
                codeField.setText("");
                libelleField.setText("");
                niveauField.setText("");
                specialiteField.setText("");
                adminField.setText(""); // Facultatif si non utilisé.
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });


        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();  // Sélectionner la ligne
            if (selectedRow != -1) {
                // Récupérer les valeurs des champs de saisie
                String code = codeField.getText();
                String libelle = libelleField.getText();
                String niveau = niveauField.getText();
                String specialite = specialiteField.getText();

                // Vérifier si tous les champs sont remplis
                if (!code.isEmpty() && !libelle.isEmpty() && !niveau.isEmpty() && !specialite.isEmpty()) {
                    // Récupérer le code de la ligne sélectionnée dans le tableau
                    String codeTable = (String) table.getValueAt(selectedRow, 0);
                    classeDAO.modifierClasse(codeTable, libelle, niveau, specialite);
                    classeDAO.afficherClasse(tableModel);  // Met à jour le tableau
                    JOptionPane.showMessageDialog(this, "Modification effectuée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Afficher un message si aucune ligne n'est sélectionnée
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });



        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String code = (String) table.getValueAt(selectedRow, 0);
                classeDAO.supprimerClasse(code);
                classeDAO.afficherClasse(tableModel); // Met à jour le tableau
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
