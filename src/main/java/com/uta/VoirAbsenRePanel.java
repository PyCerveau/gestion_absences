package com.uta;

import com.uta.dao.ConsulterDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


public class VoirAbsenRePanel extends JPanel implements ActionListener {
    private JPanel Backx;
    private JLabel ClasseChoisie;
    private JLabel X;
    private JLabel date;
    private JButton jButton1;
    private JLabel jLabel1;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private JTable listeDEtudiant;
    private JPanel nomAdm;
    private JButton print;
    private JButton retourAuChoixDeLaClasse;
    private JLabel s;
    private ConsulterDAO consulterDAO;

    public VoirAbsenRePanel(Connection connection) {
        this.consulterDAO = new ConsulterDAO(connection);
        this.initComponents();
    }

    private void initComponents() {
        // Initialisation des composants
        this.jPanel2 = new JPanel();
        this.s = new JLabel();
        this.nomAdm = new JPanel();
        this.jLabel4 = new JLabel();
        this.date = new JLabel();
        this.jLabel5 = new JLabel();
        this.jLabel1 = new JLabel();
        this.ClasseChoisie = new JLabel();
        this.jLabel3 = new JLabel();
        this.retourAuChoixDeLaClasse = new JButton();
        this.jLabel6 = new JLabel();
        this.print = new JButton();
        this.jButton1 = new JButton();
        this.jScrollPane1 = new JScrollPane();
        this.listeDEtudiant = new JTable();
        this.Backx = new JPanel();
        this.X = new JLabel();

        // Configuration du panneau principal
        setLayout(new BorderLayout());
        // Ajoutez vos composants ici
        setPreferredSize(new Dimension(1200, 890)); // Utilisation de la disposition absolue pour les composants

        // Suppression de la première section avec le fond bleu et l'image "uta.jpg"

        // Configuration du fond du panneau
        this.jPanel2.setBackground(new Color(255, 255, 255));
        this.jPanel2.setLayout(null);

        // Définir l'icône pour l'autre image (par exemple : holidays and absence)
        ImageIcon holidayIcon = new ImageIcon("src/main/java/com/uta/icons/holidays-and-absence.png");
        Image holidayImage = holidayIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Redimensionner l'image
        this.jLabel4.setIcon(new ImageIcon(holidayImage));
        this.nomAdm.add(this.jLabel4);
        this.nomAdm.setBackground(new Color(26, 38, 55));
        this.nomAdm.setLayout(null);
        this.jLabel4.setBounds(650, 10, 210, 210);

        // Date et autres labels
        this.date.setBackground(new Color(255, 255, 255));
        this.date.setFont(new Font("Calibri", 0, 24));
        this.date.setForeground(new Color(255, 255, 255));
        this.date.setHorizontalAlignment(SwingConstants.CENTER);
        this.date.setText(new Date().toString());
        this.nomAdm.add(this.date);
        this.date.setBounds(610, 220, 500, 50);

        this.jLabel5.setFont(new Font("Calibri", 0, 36));
        this.jLabel5.setForeground(new Color(255, 255, 255));
        this.jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
        this.jLabel5.setText("Liste D'Absence");
        this.nomAdm.add(this.jLabel5);
        this.jLabel5.setBounds(280, 70, 350, 90);

        // Configuration des labels et autres composants
        this.jLabel1.setFont(new Font("Tahoma", Font.BOLD, 11));
        this.jLabel1.setForeground(Color.WHITE);
        this.jLabel1.setText("CLASSE : ");
        this.nomAdm.add(this.jLabel1);
        this.jLabel1.setBounds(390, 170, 62, 46);

        this.ClasseChoisie.setFont(new Font("Calibri", Font.ITALIC, 16));
        this.ClasseChoisie.setForeground(Color.WHITE);
        this.ClasseChoisie.setText("NomClasse");
        this.nomAdm.add(this.ClasseChoisie);
        this.ClasseChoisie.setBounds(450, 170, 500, 44);

        this.jPanel2.add(this.nomAdm);
        this.nomAdm.setBounds(0, 0, 1290, 290); // Ajusté pour occuper la zone visible uniquement

        // Action au retour
        this.retourAuChoixDeLaClasse.setText("Retour");
        this.retourAuChoixDeLaClasse.addActionListener(evt -> retourAuChoixDeLaClasseActionPerformed(evt));
        this.jPanel2.add(this.retourAuChoixDeLaClasse);
        this.retourAuChoixDeLaClasse.setBounds(30, 700, 111, 36);

        // Ajouter un message pour les détails des absences
        this.jLabel6.setFont(new Font("Calibri", Font.ITALIC, 12));
        this.jLabel6.setText(" ** Cliquez pour afficher les details des absences **");
        this.jPanel2.add(this.jLabel6);
        this.jLabel6.setBounds(190, 740, 350, 16);

        // Configuration du bouton imprimer
        this.print.setText("Imprimer");
        this.print.addActionListener(evt -> printActionPerformed(evt));
        this.jPanel2.add(this.print);
        this.print.setBounds(640, 710, 97, 40);

        // Envoi par mail
        this.jButton1.setText("Envoyer mail");
        this.jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));
        this.jPanel2.add(this.jButton1);
        this.jButton1.setBounds(760, 710, 105, 40);

        // Table des étudiants
        this.listeDEtudiant.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Nom", "Prenom", "Nombre d'heure"}) {
            boolean[] canEdit = new boolean[]{false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return this.canEdit[columnIndex];
            }
        });



        // Augmenter la hauteur des lignes
        this.listeDEtudiant.setRowHeight(40); // Augmenter la taille des lignes

        this.listeDEtudiant.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                listeDEtudiantMouseClicked(evt);
            }
        });
        this.jScrollPane1.setViewportView(this.listeDEtudiant);
        this.jPanel2.add(this.jScrollPane1);
        this.jScrollPane1.setBounds(2, 430, 1150, 232);

        // Panneau pour fermer la fenêtre
        this.Backx.setBackground(new Color(0, 102, 255));
        this.Backx.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                BackxMouseClicked(evt);
            }
        });
        this.Backx.setLayout(null);
        this.X.setFont(new Font("Calibri", Font.PLAIN, 18));
        this.X.setForeground(Color.WHITE);
        this.X.setHorizontalAlignment(SwingConstants.CENTER);
        this.X.setText("X");
        this.Backx.add(this.X);
        this.X.setBounds(850, 0, 30, 30);
        this.jPanel2.add(this.Backx);
        this.Backx.setBounds(0, 0, 1200, 40); // Taille de la barre de fermeture (X)

        // Finalisation du panneau
        this.add(this.jPanel2);
        this.setPreferredSize(new Dimension(1200, 810)); // Définir la taille préférée pour le JPanel

        loadAbsents();
    }

    private void loadAbsents() {
        try {
            // Charger les étudiants absents via DAO
            List<Object[]> absents = consulterDAO.getEtudiantsAbsents();
            DefaultTableModel model = (DefaultTableModel) listeDEtudiant.getModel();
            model.setRowCount(0); // Réinitialiser le tableau

            // Ajouter les absents dans le tableau
            for (Object[] etudiant : absents) {
                model.addRow(etudiant); // Ajouter chaque étudiant absent
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des absents : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listeDEtudiantMouseClicked(MouseEvent evt) {
        int selectedRow = listeDEtudiant.getSelectedRow();
        if (selectedRow != -1) {
            String nom = (String) listeDEtudiant.getValueAt(selectedRow, 0);
            String prenom = (String) listeDEtudiant.getValueAt(selectedRow, 1);

            if (nom == null || prenom == null || nom.trim().isEmpty() || prenom.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le nom ou prénom est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Ajouter des logs pour déboguer
                System.out.println("Nom: " + nom + ", Prénom: " + prenom);

                // Obtenir la classe de l'étudiant
                String classe = consulterDAO.getClasseEtudiant(nom, prenom);
                if (classe != null) {
                    ClasseChoisie.setText(classe);
                } else {
                    ClasseChoisie.setText("Classe non trouvée");
                }

                // Obtenir les détails de l'absence
                String detailsAbsence = consulterDAO.getDetailsAbsence(nom, prenom);
                jLabel6.setText(detailsAbsence);
            } catch (SQLException e) {
                e.printStackTrace();  // Affiche la trace de l'exception
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement des informations : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }




    @Override
    public void actionPerformed(ActionEvent e) {
        // Implémenter l'action du bouton si nécessaire
    }

    private void retourAuChoixDeLaClasseActionPerformed(ActionEvent evt) {
        // Action lorsque le bouton retour est cliqué
    }

    private void printActionPerformed(ActionEvent evt) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Erreur d'impression : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        // Action pour envoyer un email
    }

    private void BackxMouseClicked(MouseEvent evt) {
        // Fermer la fenêtre ou effectuer une autre action
    }

//    private void listeDEtudiantMouseClicked(MouseEvent evt) {
//        // Action quand un étudiant est sélectionné dans la table
//    }

}
