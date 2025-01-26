package com.uta;

import com.uta.dao.PrintDAO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Color;


import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ImprimerListePanel extends JPanel {
    private DefaultTableModel tableModel;
    private JComboBox<String> classBox;

    public ImprimerListePanel(Connection connection) {
        // Initialisation du DAO
        PrintDAO printDAO = new PrintDAO(connection);

        // Mise en page principale
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 240, 240));

        // Entête avec image et titre
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(26, 38, 55)); // Fond vert foncé

        ImageIcon icon = new ImageIcon("src/main/java/com/uta/icons/holidays-and-absence.png");
        JLabel imageLabel = new JLabel(icon);
        headerPanel.add(imageLabel);

        JLabel titleLabel = new JLabel("Liste de classes", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); // Texte en blanc
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Tableau pour afficher les données
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.setBackground(new Color(245, 245, 245));

        String[] columns = {"Matricule", "Nom", "Prénom", "Absent", "Present"};
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3 || columnIndex == 4) {
                    return Boolean.class; // Cases à cocher pour "Absent" et "Present"
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4; // Rendre seulement "Absent" et "Present" éditables
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Ajouter un listener pour rendre "Absent" et "Present" mutuellement exclusifs
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column == 3 || column == 4) {
                    Boolean absentChecked = (Boolean) tableModel.getValueAt(row, 3);
                    Boolean presentChecked = (Boolean) tableModel.getValueAt(row, 4);

                    if (absentChecked != null && presentChecked != null && absentChecked && presentChecked) {
                        if (column == 3) {
                            tableModel.setValueAt(false, row, 4);
                        } else {
                            tableModel.setValueAt(false, row, 3);
                        }
                    }
                }
            }
        });

        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Formulaire pour sélectionner la classe
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Liste déroulante pour sélectionner une classe
        classBox = new JComboBox<>();
        printDAO.chargerClasses(classBox); // Charger les classes dans la liste déroulante
        classBox.addActionListener(e -> {
            String selectedClass = (String) classBox.getSelectedItem();
            if (selectedClass != null) {
                chargerDonneesDansTable(printDAO, selectedClass);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Sélectionnez une classe :"), gbc);

        gbc.gridx = 1;
        formPanel.add(classBox, gbc);

        // Boutons Ajouter, Modifier, Supprimer, Imprimer
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton printButton = new JButton("Imprimer");

        // Gestion des événements des boutons
        addButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Fonctionnalité Ajouter en développement."));
        editButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Fonctionnalité Modifier en développement."));
        deleteButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Fonctionnalité Supprimer en développement."));
        printButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Impression en cours..."));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(printButton);

        // Gestion des événements des boutons
//        addButton.addActionListener(e -> {
//            String matricule = JOptionPane.showInputDialog(this, "Entrer le matricule de l'étudiant absent :");
//            if (matricule != null && !matricule.trim().isEmpty()) {
//                try {
//                    printDAO.ajouterAbsence(matricule, );
//                    chargerDonneesDansTable(printDAO, (String) classBox.getSelectedItem());
//                    JOptionPane.showMessageDialog(this, "Absence ajoutée avec succès !");
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String matricule = (String) tableModel.getValueAt(selectedRow, 0);
                Boolean absent = (Boolean) tableModel.getValueAt(selectedRow, 3);
                Boolean present = (Boolean) tableModel.getValueAt(selectedRow, 4);
                try {
                    printDAO.modifierAbsence(matricule, absent, present);
                    chargerDonneesDansTable(printDAO, (String) classBox.getSelectedItem());
                    JOptionPane.showMessageDialog(this, "Modification réussie !");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String matricule = (String) tableModel.getValueAt(selectedRow, 0);
                try {
                    printDAO.supprimerAbsence(matricule);
                    chargerDonneesDansTable(printDAO, (String) classBox.getSelectedItem());
                    JOptionPane.showMessageDialog(this, "Absence supprimée avec succès !");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.");
            }

        });

        printButton.addActionListener(e -> {
            try {
                // Chemin du fichier Excel à générer
                String cheminFichier = "src/main/java/com/uta/ListeDesAbsences.xlsx";

                // Créer un classeur Excel
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Liste des Absences");

                // Ajouter le logo de l'école en haut à gauche
                try (InputStream logoInputStream = new FileInputStream("src/main/java/com/uta/icons/uta.jpg")) {
                    byte[] logoBytes = new byte[logoInputStream.available()];
                    logoInputStream.read(logoBytes);

                    // Ajouter l'image dans le classeur
                    int logoIndex = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_PNG);

                    // Créer une cellule pour l'image (logo)
                    Drawing drawing = sheet.createDrawingPatriarch();
                    ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
                    anchor.setCol1(0); // Colonne 0 (A)
                    anchor.setRow1(0); // Ligne 0 (1ère ligne)
                    drawing.createPicture(anchor, logoIndex);
                }

                // Ajouter le titre centré "Liste de Classe: NomClasse"
                Row titleRow = sheet.createRow(1);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("Liste de Classe: NomClasse");

                // Fusionner les cellules pour le titre
                sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, tableModel.getColumnCount() - 1));

                // Style pour le titre (centré et en gras)
                CellStyle titleStyle = workbook.createCellStyle();
                Font titleFont = new Font("Arial", Font.BOLD, 24);  // Utilisation de Font classique

                titleStyle.setAlignment(HorizontalAlignment.CENTER);
                titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                titleCell.setCellStyle(titleStyle);

                // Ajouter les en-têtes de colonnes
                Row headerRow = sheet.createRow(2);
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(tableModel.getColumnName(i));

                    // Style pour l'en-tête (gras et fond coloré)
                    CellStyle headerStyle = workbook.createCellStyle();
                    Font headerFont = new Font("Arial", Font.BOLD, 24); // Utilisation de Font classique

                    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
                    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cell.setCellStyle(headerStyle);
                }

                // Ajouter les données de la liste d'absences
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 3); // Commence à la ligne 3 (après le titre et l'en-tête)
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object value = tableModel.getValueAt(i, j);
                        Cell cell = row.createCell(j);
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                        } else if (value instanceof Boolean) {
                            cell.setCellValue((Boolean) value);
                        } else {
                            cell.setCellValue(value != null ? value.toString() : "");
                        }
                    }
                }

                // Ajuster automatiquement la largeur des colonnes
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                // Écrire dans le fichier Excel
                try (FileOutputStream fileOut = new FileOutputStream(cheminFichier)) {
                    workbook.write(fileOut);
                }

                workbook.close();

                // Ouvrir le fichier automatiquement
                Desktop desktop = Desktop.getDesktop();
                desktop.open(new File(cheminFichier));

                JOptionPane.showMessageDialog(this, "Le fichier Excel a été généré et ouvert avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la génération du fichier Excel : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });





        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);
    }


    // Charger les données dans le tableau
    private void chargerDonneesDansTable(PrintDAO printDAO, String className) {
        List<Object[]> data = printDAO.getAbsenceData(className);
        tableModel.setRowCount(0); // Effacer les anciennes données
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    // Exemple de connexion (à remplacer par votre configuration réelle)
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/gestion_absences",
                    "root",
                    ""
            );

            JFrame frame = new JFrame("Imprimer Liste Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new ImprimerListePanel(connection));
            frame.setSize(800, 600);
            frame.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur de connexion à la base de données : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
