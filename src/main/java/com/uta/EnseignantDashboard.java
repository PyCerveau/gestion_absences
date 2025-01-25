package com.uta;

import com.uta.dao.DatabaseConnection;
import com.uta.login.LoginForm;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.sql.Connection;
import java.sql.SQLException;

public class EnseignantDashboard extends JFrame {
    private String ecole;
    private String user;
    private String anneeScolaire;
    private JPanel mainContentPanel;
    private JFrame gestionAbsencesFrame;
    public EnseignantDashboard(String ecole, String user, String anneeScolaire){
        this.ecole = ecole;
        this.user = user;
        this.anneeScolaire = anneeScolaire;
    }
    public void showPage() {
        gestionAbsencesFrame = new JFrame("Gestion des Absences");

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        gestionAbsencesFrame.setSize(width, height);

        gestionAbsencesFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gestionAbsencesFrame.setLayout(new BorderLayout());

        // Panneau d'en-tête
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180)); // Bleu acier
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Label pour la photo de l'utilisateur (arrondi)
        JLabel photoLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int diameter = Math.min(getWidth(), getHeight());
                g2d.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));
                g2d.drawImage(((ImageIcon) getIcon()).getImage(), 0, 0, diameter, diameter, this);
                g2d.dispose();
            }
        };
        ImageIcon userIcon = new ImageIcon("src/main/java/com/uta/icons/user.png");
        Image  Icon = userIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        photoLabel.setIcon(new ImageIcon(Icon));
        photoLabel.setPreferredSize(new Dimension(100, 100));
        photoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        photoLabel.setVerticalAlignment(SwingConstants.CENTER);

        // Panneau d'informations
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.setBackground(new Color(70, 130, 180));

        JLabel ecoleLabel = new JLabel("\u00c9cole: " + ecole);
        JLabel adminLabel = new JLabel("Enseignant: " + user);
        JLabel anneeLabel = new JLabel("Ann\u00e9e scolaire: " + anneeScolaire);

        ecoleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        adminLabel.setFont(new Font("Arial", Font.BOLD, 18));
        anneeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        ecoleLabel.setForeground(Color.WHITE);
        adminLabel.setForeground(Color.WHITE);
        anneeLabel.setForeground(Color.WHITE);

        infoPanel.add(ecoleLabel);
        infoPanel.add(adminLabel);
        infoPanel.add(anneeLabel);

        headerPanel.add(photoLabel, BorderLayout.WEST);
        headerPanel.add(infoPanel, BorderLayout.CENTER);

        gestionAbsencesFrame.add(headerPanel, BorderLayout.NORTH);

// Menu latéral
        JPanel menuLateral = new JPanel();
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS)); // Disposition en colonne
        menuLateral.setBackground(new Color(34, 139, 34)); // Vert foncé
        menuLateral.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // Espacement du menu

// Titre du menu
        JLabel menuTitle = new JLabel("Gestion Des Absences");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 16));
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer le titre
        menuTitle.setForeground(Color.WHITE);
        menuLateral.add(menuTitle);
        menuLateral.add(Box.createRigidArea(new Dimension(0, 20))); // Espacement entre le titre et les boutons

// Création des boutons avec icônes et texte alignés
        JButton btnStatistiques = createMenuButton("Statistiques", "src/main/java/com/uta/icons/—Pngtree—data charts creatives_605908.png");
        JButton btnEnregistrerAbsences = createMenuButton("Enregistrer les absences", "src/main/java/com/uta/icons/mark.png");
        JButton btnConsulterAbsences = createMenuButton("Consulter les absences", "src/main/java/com/uta/icons/consultant.png");
        JButton btnGestionListeClasse = createMenuButton("Imprimer la liste", "src/main/java/com/uta/icons/to-do-list.png");
        JButton btnGestionAbout = createMenuButton("A propos de nous", "src/main/java/com/uta/icons/information-button.png");
        JButton btnDeconnexion = createMenuButton("Se déconnecter", "src/main/java/com/uta/icons/logout.png");

// Liste des boutons à ajouter
        JButton[] buttons = {btnStatistiques, btnEnregistrerAbsences, btnConsulterAbsences,
                btnGestionListeClasse, btnGestionAbout, btnDeconnexion};

// Ajouter les boutons au menu latéral avec espacement uniforme
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer chaque bouton sur l'axe X
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height)); // Pour occuper toute la largeur disponible
            menuLateral.add(button);
            menuLateral.add(Box.createRigidArea(new Dimension(0, 10))); // Espacement entre les boutons
        }

        // Ajouter le panneau des boutons dans un JScrollPane pour rendre le menu défilable
        JScrollPane scrollPane = new JScrollPane(menuLateral);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Barre de défilement verticale toujours visible
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Pas de défilement horizontal
        scrollPane.setPreferredSize(new Dimension(250, height)); // Ajuste la taille du panneau défilant
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Vitesse de défilement

// Ajouter le menu latéral au frame
        gestionAbsencesFrame.add(menuLateral, BorderLayout.WEST);

// Panel principal pour le contenu
        mainContentPanel = new JPanel(new CardLayout());


        // Panneau pour afficher le logo par défaut
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon logoIcon = new ImageIcon("src/main/java/com/uta/icons/uta.jpg");
                Image logoImage = logoIcon.getImage();
                int logoWidth = logoImage.getWidth(null);
                int logoHeight = logoImage.getHeight(null);
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int x = (panelWidth - logoWidth) / 2;
                int y = (panelHeight - logoHeight) / 2;
                g.drawImage(logoImage, x, y, null);
            }
        };
        logoPanel.setPreferredSize(new Dimension(800, 600)); // Définir la taille du panel
        mainContentPanel.add(logoPanel, "Logo"); // Ajouter le panel avec le logo
        gestionAbsencesFrame.add(mainContentPanel, BorderLayout.CENTER);

        // Ajout des actions aux boutons
        btnStatistiques.addActionListener(e -> showPanel("Statistiques", new StatistiquesPanel()));


        try {
            // Tentative de connexion à la base de données
            Connection connection = DatabaseConnection.getConnection();
            btnEnregistrerAbsences.addActionListener(e -> showPanel("Enregistrer les absences", new EnregistrerAbsencesPanel(connection)));
            btnConsulterAbsences.addActionListener(e -> showPanel("Consulter les absences", new VoirAbsenRePanel(connection)));
        } catch (SQLException ex) {
            // Gestion des erreurs de connexion
            ex.printStackTrace();
            JOptionPane.showMessageDialog(gestionAbsencesFrame, "Erreur de connexion à la base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        btnGestionListeClasse.addActionListener(e -> showPanel("Imprimer la liste", new ImprimerListePanel()));
        btnGestionAbout.addActionListener(e -> showPanel("A propos de nous", new AboutPanel()));

        btnDeconnexion.addActionListener(e -> {
            JOptionPane.showMessageDialog(gestionAbsencesFrame, "Déconnexion réussie!");
            redirectToLogin();
        });

        gestionAbsencesFrame.setLocationRelativeTo(null);
        gestionAbsencesFrame.setVisible(true);
    }

    private JButton createMenuButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setIcon(new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false); // Supprimer le fond blanc
        button.setOpaque(false);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Espacement interne

        // Ajouter l'effet de survol (hover)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(255, 255, 200)); // Changer la couleur du texte
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(10, 10, 10, 10),
                        BorderFactory.createLineBorder(new Color(255, 255, 200), 2)
                ));
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Réinitialiser la bordure
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(255, 150, 150)); // Effet de clic
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(70, 130, 180)); // Revenir à l'état hover
            }
        });

        // Ajouter l'ombre et l'effet de zoom
        button.setRolloverEnabled(true);
        button.addChangeListener(e -> {
            if (button.getModel().isRollover()) {
                button.setFont(new Font("Arial", Font.BOLD, 16)); // Zoom avant
            } else {
                button.setFont(new Font("Arial", Font.BOLD, 14)); // Taille normale
            }
        });

        return button;
    }


    private void showPanel(String panelName, JPanel panel) {
        CardLayout cl = (CardLayout) (mainContentPanel.getLayout());
        mainContentPanel.add(panel, panelName);
        cl.show(mainContentPanel, panelName);
    }

    private void redirectToLogin() {
        gestionAbsencesFrame.setVisible(false);
        new LoginForm().showPage();
        gestionAbsencesFrame.dispose();
    }
}
