package com.uta;


import com.uta.dao.EnregistrementDAO;
import com.uta.dao.ModuleDAO;
import com.uta.dao.UserDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GestionUtilisateurPanel extends JPanel {
    public GestionUtilisateurPanel() {
        // Couleur noir brillante pour le panneau principal
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

       // Panel pour le titre et l'image
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(107, 227, 216)); // Fond vert

        // Image à gauche
        ImageIcon icon = new ImageIcon("src/main/java/com/uta/icons/holidays-and-absence.png"); // Remplacez le chemin par le bon chemin de votre image
        JLabel imageLabel = new JLabel(icon);
        titlePanel.add(imageLabel);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Utilsateur", JLabel.LEFT);
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


        // Champ pour sélectionner un professeur
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Utilisateur:"), gbc);

        gbc.gridx = 1;
        JTextField userField = new JTextField();
        userField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(userField, gbc);

        // Champ pour sélectionner un professeur
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Mot de passe:"), gbc);

        gbc.gridx = 1;
        JTextField mdpField = new JTextField();
        mdpField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(mdpField, gbc);

        // Champ pour sélectionner un professeur
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Rôle:"), gbc);

        gbc.gridx = 1;
        JTextField roleField = new JTextField();
        roleField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(roleField, gbc);

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

        String[] columns = {"Nom Utilsateur", "Mot De Passe", "Rôle"};
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
                userField.setText((String) table.getValueAt(selectedRow, 0));
                mdpField.setText((String) table.getValueAt(selectedRow, 1));
                roleField.setText((String) table.getValueAt(selectedRow, 2));
            }
        });
        UserDAO userDAO = new UserDAO();
        userDAO.afficherClasse(tableModel);

        // Événement de clic sur une ligne du tableau
        table.getSelectionModel().addListSelectionListener(event -> {

        });



        // Action pour ajouter un module
        addButton.addActionListener(e -> {
            String user = userField.getText();
            String mdp = mdpField.getText();
            String role = roleField.getText();

            if (!user.isEmpty() && !mdp.isEmpty() && !role.isEmpty() ) {
//                // Ajouter dans la base de données
                userDAO.addUser(user, mdp, role);
                tableModel.addRow(new Object[]{user, user, role});

//                // Réinitialiser les champs
                userField.setText("");
                mdpField.setText("");
                roleField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        modifyButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow(); // Récupérer la ligne sélectionnée
            if (selectedRow != -1) { // Vérifier qu'une ligne est bien sélectionnée
                String codeTable = (String) table.getValueAt(selectedRow, 0); // Récupérer l'identifiant de l'utilisateur dans la table
                String user = userField.getText(); // Récupérer les nouvelles valeurs des champs texte
                String mdp = mdpField.getText();
                String role = roleField.getText();

                if (!user.isEmpty() && !mdp.isEmpty() && !role.isEmpty()) {
                    boolean updated = userDAO.updateUser(user, mdp, codeTable); // Mettre à jour l'utilisateur dans la base de données
                    if (updated) {
                        // Mettre à jour les données dans le tableau
                        table.setValueAt(user, selectedRow, 1);
                        table.setValueAt(mdp, selectedRow, 2);
                        table.setValueAt(role, selectedRow, 3);

                        JOptionPane.showMessageDialog(this, "Modification effectuée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis.", "Avertissement", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            }
        });



// Action pour supprimer une ligne sélectionnée
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow(); // Récupérer la ligne sélectionnée
            if (selectedRow != -1) { // Vérifier qu'une ligne est bien sélectionnée
                int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cet utilisateur ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String codeTable = (String) table.getValueAt(selectedRow, 0); // Récupérer l'identifiant de l'utilisateur dans la table
                    boolean deleted = userDAO.deleteUser(codeTable); // Supprimer l'utilisateur dans la base de données
                    if (deleted) {
                        tableModel.removeRow(selectedRow); // Supprimer la ligne du tableau
                        JOptionPane.showMessageDialog(this, "Utilisateur supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            }
        });

    }
}
