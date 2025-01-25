package com.uta;

import com.uta.dao.ModuleDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;

public class GestionModulesPanel extends JPanel {
    public GestionModulesPanel(Connection connection) {
        // Couleur noir brillante pour le panneau principal
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

// Panel pour le titre et l'image
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(239, 163, 61)); // Fond vert

        // Image à gauche
        ImageIcon icon = new ImageIcon("src/main/java/com/uta/icons/holidays-and-absence.png"); // Remplacez le chemin par le bon chemin de votre image
        JLabel imageLabel = new JLabel(icon);
        titlePanel.add(imageLabel);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Modules", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); // Texte en blanc
        titlePanel.add(titleLabel);

        // Ajout du panel du titre au panneau principal
        add(titlePanel, BorderLayout.NORTH);


        // Conteneur principal pour le formulaire et le tableau
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        // Formulaire pour ajouter un module
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Champ Nom du module
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Code:"), gbc);

        gbc.gridx = 1;
        JTextField codeField = new JTextField();
        codeField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(codeField, gbc);

        // Champ pour sélectionner un professeur
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Intitule:"), gbc);

        gbc.gridx = 1;
        JTextField intField = new JTextField();
        intField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(intField, gbc);

        // Champ pour sélectionner un professeur
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("ECUE:"), gbc);

        gbc.gridx = 1;
        JTextField ecueField = new JTextField();
        ecueField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(ecueField, gbc);


        // Champ pour sélectionner un professeur
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Semestre:"), gbc);

        gbc.gridx = 1;
        JTextField semField = new JTextField();
        semField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(semField, gbc);

        // Boutons Ajouter, Modifier, Supprimer
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(50, 50, 50));
        JButton addButton = new JButton("Ajouter");
        JButton deleteButton = new JButton("Supprimer");
        JButton modifyButton = new JButton("Modifier");

        // Personnalisation des boutons
        addButton.setBackground(new Color(0, 123, 255));
        addButton.setForeground(Color.GREEN);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.PLAIN, 14));

        modifyButton.setBackground(new Color(255, 193, 7));
        modifyButton.setForeground(Color.GREEN);
        modifyButton.setFocusPainted(false);
        modifyButton.setFont(new Font("Arial", Font.PLAIN, 14));

        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.GREEN);
        deleteButton.setFocusPainted(false);
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 14));


        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(modifyButton);
        buttonPanel.setBackground(Color.WHITE);

        formPanel.add(buttonPanel, gbc);
        formPanel.setBackground(Color.WHITE);

        // Ajouter le formulaire à gauche
        mainPanel.add(formPanel);

        // Tableau pour afficher les modules
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        String[] columns = {"Code", "Intitulé", "ECUE", "Semetre"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        // Réglages pour améliorer la présentation du tableau
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 30));
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));


        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Ajouter le tableau à droite
        mainPanel.add(tablePanel);

        // Événement de clic sur une ligne du tableau
        table.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                codeField.setText((String) table.getValueAt(selectedRow, 0));
                intField.setText((String) table.getValueAt(selectedRow, 1));
                ecueField.setText((String) table.getValueAt(selectedRow, 2));
                semField.setText((String) table.getValueAt(selectedRow, 3));
            }
        });


        ModuleDAO moduleDAO = new ModuleDAO(connection);
        moduleDAO.afficherModule(tableModel);


        // Action pour ajouter un module
        addButton.addActionListener(e -> {
            String code = codeField.getText();
            String intitule = intField.getText();
            String ecue = ecueField.getText();
            String semestre = semField.getText();
            if (!code.isEmpty() && intitule != null) {
                moduleDAO.ajouterModule(code, intitule, ecue, semestre);
                tableModel.addRow(new Object[]{code, intitule, ecue, semestre});
                JOptionPane.showMessageDialog(this, "Module ajouté : " + code);
                codeField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            }
        });

        // Action pour supprimer une ligne sélectionnée
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Récupérer la valeur de la colonne `code` de la ligne sélectionnée
                String codeToDelete = (String) tableModel.getValueAt(selectedRow, 0);

                int confirmation = JOptionPane.showConfirmDialog(
                        this,
                        "Êtes-vous sûr de vouloir supprimer ce module ?",
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        // Appeler la méthode DAO pour supprimer de la base de données

                        moduleDAO.supprimerModule(codeToDelete);

                        // Supprimer la ligne du tableau
                        tableModel.removeRow(selectedRow);

                        // Message de confirmation
                        JOptionPane.showMessageDialog(this, "Module supprimé avec succès.");
                    } catch (Exception ex) {
                        // Gestion des erreurs
                        JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.");
            }
        });


        modifyButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Récupérer les nouvelles valeurs des champs du formulaire
                String newCode = codeField.getText();
                String newIntitule = intField.getText();
                String newEcue = ecueField.getText();
                String newSemestre = semField.getText();

                // Vérifier que les champs ne sont pas vides
                if (!newCode.isEmpty() && !newIntitule.isEmpty() && !newEcue.isEmpty() && !newSemestre.isEmpty()) {
                    try {
                        // Appeler la méthode DAO pour mettre à jour dans la base de données
                        moduleDAO.modifierModule(newCode, newIntitule, newEcue, newSemestre);

                        // Mettre à jour la ligne sélectionnée dans le tableau
                        tableModel.setValueAt(newCode, selectedRow, 0);
                        tableModel.setValueAt(newIntitule, selectedRow, 1);
                        tableModel.setValueAt(newEcue, selectedRow, 2);
                        tableModel.setValueAt(newSemestre, selectedRow, 3);

                        // Message de confirmation
                        JOptionPane.showMessageDialog(this, "Module modifié avec succès.");

                        // Réinitialiser les champs du formulaire
                        codeField.setText("");
                        intField.setText("");
                        ecueField.setText("");
                        semField.setText("");
                    } catch (Exception ex) {
                        // Gestion des erreurs
                        JOptionPane.showMessageDialog(this, "Erreur lors de la modification : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            }
        });


    }
}
