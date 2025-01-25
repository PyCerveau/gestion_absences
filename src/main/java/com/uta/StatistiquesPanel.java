package com.uta;

import com.uta.dao.StatistiquesDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class StatistiquesPanel extends JPanel {

    private JTable table;
    private JComboBox<String> classeComboBox;

    public StatistiquesPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel principal
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(244, 86, 86));

        ImageIcon icon = new ImageIcon("src/main/java/com/uta/icons/holidays-and-absence.png");
        JLabel imageLabel = new JLabel(icon);
        titlePanel.add(imageLabel);

        JLabel titleLabel = new JLabel("Statistique", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        topPanel.add(titlePanel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        // Récupérer les données de la base de données
        StatistiquesDAO dao = new StatistiquesDAO();
        List<String> statistiques = dao.getAllStatistiques();

        // Créer une liste unique de classes
        Set<String> classes = new LinkedHashSet<>();
        for (String stat : statistiques) {
            String[] rowData = stat.split(" \\| ");
            classes.add(rowData[0]); // Ajouter uniquement la classe
        }

        // Créer les données pour le tableau
        String[] columnNames = {"Classe", "Module", "Nombre d'absence"};
        Object[][] data = new Object[statistiques.size()][3];

        for (int i = 0; i < statistiques.size(); i++) {
            String[] rowData = statistiques.get(i).split(" \\| ");
            data[i][0] = rowData[0];
            data[i][1] = rowData[1];
            data[i][2] = Integer.parseInt(rowData[2]);
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(600, 200));
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        // Personnalisation du tableau
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        // Ajuster la taille du tableau

        JScrollPane tableScrollPane = new JScrollPane(table);
        add(tableScrollPane, BorderLayout.CENTER);

        // Panel pour les boutons et la ComboBox
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        classeComboBox = new JComboBox<>(classes.toArray(new String[0]));
        classeComboBox.setPreferredSize(new Dimension(200, 30)); // Ajuster la taille de la ComboBox
        buttonPanel.add(classeComboBox);

        JButton generateButton = new JButton("Générer Diagramme");
        generateButton.setBackground(new Color(37, 67, 2));
        generateButton.setFont(new Font("Arial", Font.PLAIN, 14));
        buttonPanel.add(generateButton);

        JButton printButton = new JButton("Imprimer Diagramme");
        printButton.setBackground(new Color(37, 67, 2));
        printButton.setFont(new Font("Arial", Font.PLAIN, 14));
        buttonPanel.add(printButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton Générer Diagramme
        generateButton.addActionListener(e -> {
            String selectedClass = (String) classeComboBox.getSelectedItem();
            if (selectedClass != null) {
                generateBarChart(selectedClass, statistiques);
            }
        });

        // Action du bouton Imprimer Diagramme
        printButton.addActionListener(e -> {
            String selectedClass = (String) classeComboBox.getSelectedItem();
            if (selectedClass != null) {
                // Imprimer le diagramme (à implémenter si nécessaire)
            }
        });
    }

    private void generateBarChart(String selectedClass, List<String> statistiques) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (String stat : statistiques) {
            String[] rowData = stat.split(" \\| ");
            String classe = rowData[0];
            String module = rowData[1];
            int absences = Integer.parseInt(rowData[2]);

            if (classe.equals(selectedClass)) {
                dataset.addValue(absences, "Absences", module);
            }
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Diagramme des absences pour " + selectedClass,
                "Module",
                "Nombre d'absences",
                dataset
        );
        CategoryPlot plot = barChart.getCategoryPlot();
        // Ajuster la largeur des barres
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setMaximumBarWidth(0.09);
        // Peindre les barres en blanc
        renderer.setSeriesPaint(0, new Color(114, 131, 156, 255));

        // Afficher dans un JDialog
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Diagramme", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);

        ChartPanel chartPanel = new ChartPanel(barChart);
        dialog.add(chartPanel);

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Statistiques des Absences et Présences");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.add(new StatistiquesPanel());
        frame.setVisible(true);
    }
}
