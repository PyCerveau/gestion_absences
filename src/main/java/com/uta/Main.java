package com.uta;

import com.uta.dao.UserDAO;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

public class Main extends JFrame {
    private JPanel Backx;
    private JPanel Bleu;
    private JLabel IDENTIFIANT;
    private JTextField LOGIN;
    private JLabel STATUT;
    private JLabel Taswira;
    private JLabel X;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPasswordField pwd;
    private JComboBox<String> statut;
    private JButton valideAutentifiant;
    private ImageIcon imageIcon;

    public Main() {
        this.initComponents();
    }

    private void initComponents() {
        this.jPanel1 = new JPanel();
        this.jPanel2 = new JPanel();
        this.STATUT = new JLabel();
        this.Bleu = new JPanel();
        this.Taswira = new JLabel();
        this.Backx = new JPanel();
        this.X = new JLabel();
        this.LOGIN = new JTextField();
        this.IDENTIFIANT = new JLabel();
        this.pwd = new JPasswordField();
        JLabel MotsDePasse = new JLabel();
        this.statut = new JComboBox();
        this.valideAutentifiant = new JButton();

        // Setting up JFrame properties
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setResizable(false);

        this.jPanel2.setBackground(new Color(255, 255, 255));
        this.jPanel2.setLayout(null);

        this.STATUT.setFont(new Font("Calibri", 0, 18));
        this.STATUT.setForeground(new Color(153, 153, 153));
        this.STATUT.setHorizontalAlignment(SwingConstants.CENTER);
        this.STATUT.setText("Statut");
        this.jPanel2.add(this.STATUT);
        this.STATUT.setBounds(470, 380, 150, 50);

        ImageIcon backgroundIcon = new ImageIcon("src/main/java/com/uta/icons/a.png");
        Image backgroundImage = backgroundIcon.getImage().getScaledInstance(410, 560, Image.SCALE_SMOOTH); // Redimensionner l'image
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        this.Bleu.setLayout(new BorderLayout());  // Utiliser un layout pour positionner correctement l'image
        this.Bleu.add(backgroundLabel, BorderLayout.CENTER);


        this.jPanel2.add(this.Bleu);
        this.Bleu.setBounds(-10, -4, 410, 560);

        this.Backx.setBackground(new Color(83, 151, 4));
        this.Backx.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                BackxMouseClicked(evt);
            }
        });
        this.X.setFont(new Font("Calibri", 0, 18));
        this.X.setForeground(new Color(255, 255, 255));
        this.X.setHorizontalAlignment(SwingConstants.CENTER);
        this.X.setText("X");
        this.X.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.X.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                XKeyPressed(evt);
            }
        });
        GroupLayout BackxLayout = new GroupLayout(this.Backx);
        this.Backx.setLayout(BackxLayout);
        BackxLayout.setHorizontalGroup(BackxLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.X, GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE));
        BackxLayout.setVerticalGroup(BackxLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.X, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE));
        this.jPanel2.add(this.Backx);
        this.Backx.setBounds(745, 0, 55, 36);

        this.LOGIN.setFont(new Font("Calibri", 0, 12));
        this.LOGIN.setHorizontalAlignment(SwingConstants.CENTER);
        this.jPanel2.add(this.LOGIN);
        this.LOGIN.setBounds(510, 160, 200, 50);



        this.IDENTIFIANT.setFont(new Font("Calibri", 0, 18));
        this.IDENTIFIANT.setForeground(new Color(153, 153, 153));
        this.IDENTIFIANT.setHorizontalAlignment(SwingConstants.CENTER);
        this.IDENTIFIANT.setText("Identifiant");
        this.jPanel2.add(this.IDENTIFIANT);
        this.IDENTIFIANT.setBounds(450, 120, 200, 50);

        this.jPanel2.add(this.pwd);
        this.pwd.setBounds(510, 280, 200, 50);

        MotsDePasse.setFont(new Font("Calibri", 0, 18));
        MotsDePasse.setForeground(new Color(153, 153, 153));
        MotsDePasse.setHorizontalAlignment(SwingConstants.CENTER);
        MotsDePasse.setText("Mot de passe");
        this.jPanel2.add(MotsDePasse);
        MotsDePasse.setBounds(450, 240, 250, 50);

        this.statut.setModel(new DefaultComboBoxModel<>(new String[]{"ETUDIANT", "ENSEIGNANT", "RESPONSABLE"}));
        this.jPanel2.add(this.statut);
        this.statut.setBounds(510, 390, 200, 100);

        this.valideAutentifiant.setFont(new Font("Calibri", 0, 14));
        this.valideAutentifiant.setForeground(new Color(51, 51, 51));
        this.valideAutentifiant.setText("Connecter");
        this.valideAutentifiant.setBorder(new LineBorder(Color.GREEN, 2)); // Bordure noire de 2px
        this.valideAutentifiant.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.valideAutentifiant.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                valideAutentifiantActionPerformed(evt);
            }
        });
        this.jPanel2.add(this.valideAutentifiant);
        this.valideAutentifiant.setBounds(530, 473, 150, 30);

        GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
        this.jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jPanel2, GroupLayout.PREFERRED_SIZE, 800, GroupLayout.PREFERRED_SIZE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jPanel2, GroupLayout.PREFERRED_SIZE, 543, GroupLayout.PREFERRED_SIZE));

        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jPanel1, GroupLayout.PREFERRED_SIZE, 800, GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jPanel1, GroupLayout.PREFERRED_SIZE, 554, GroupLayout.PREFERRED_SIZE));

        LOGIN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Utilisation de SwingUtilities.invokeLater pour s'assurer que le clic se fait dans le thread de l'UI
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            valideAutentifiant.doClick();  // Simule un clic sur le bouton
                        }
                    });
                }
            }
        });


        this.setSize(new Dimension(800, 541));
        this.setLocationRelativeTo(null);


    }

    private void valideAutentifiantActionPerformed(ActionEvent evt) {
        String login = LOGIN.getText();
        String password = new String(pwd.getPassword());
        String role = statut.getSelectedItem().toString();

        UserDAO userDAO = new UserDAO();

        if (userDAO.authenticateUser(login, password, role)) {
            // Si l'authentification réussie, afficher un message
            JOptionPane.showMessageDialog(this, "Authentification réussie pour " + login, "Succès", JOptionPane.INFORMATION_MESSAGE);

            // Charger le tableau de bord en fonction du rôle
            loadDashboard(role);
        } else {
            JOptionPane.showMessageDialog(this, "Identifiant ou mot de passe erroné", "ECHEC D'AUTHENTIFICATION", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadDashboard(String role) {
        // Supposons que vous avez une fenêtre différente pour chaque rôle
        UserDAO userDAO = new UserDAO();
        String identifiant = userDAO.getIdentifiantByLogin(LOGIN.getText());

        if (identifiant == null) {
            JOptionPane.showMessageDialog(this, "Erreur : Identifiant introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ("ETUDIANT".equals(role)) {
            this.dispose();
             new EtudiantDashboard("Universite De Technologie d'Abidjan", identifiant, "2024-2025").showPage(); // Remplacez par votre classe de tableau de bord pour l'étudiant
        } else if ("ENSEIGNANT".equals(role)) {
            this.dispose();
             new EnseignantDashboard("Universite De Technologie d'Abidjan", identifiant, "2024-2025").showPage(); // Remplacez par votre classe de tableau de bord pour l'enseignant
        } else if ("RESPONSABLE".equals(role)) {
            this.dispose();
             new GestionAbsencesPage("Universite De Technologie d'Abidjan", identifiant, "2024-2025").showPage(); // Remplacez par votre classe de tableau de bord pour le responsable
        }

    }



    private void BackxMouseClicked(MouseEvent evt) {
        System.exit(0);
    }

    private void XKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);

            }
        });
    }



}
