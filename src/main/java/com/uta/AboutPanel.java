package com.uta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutPanel extends JPanel {
    private JLabel scrollingTextLabel;
    private String[] infoLines = {
        "MEMBRE DU GROUPE: NASSERE YACOUBE, DABO ALI, ADE, ALLOMA N'DA YAO JEAN LUC",
        "CHEF DE PROJET : NASSERE YACOUBA",
        "CONCEPTEUR: DABO ALI",
        "DEVELOPPEUR BACKEND: ALLOMA N'DA YAO JEAN LUC",
        "DEVELOPPEUR FRONTEND: ADE, DABO ALI, NASSERE YACOUBA",
        "TESTEUR: ADE"
    };

    public AboutPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240)); // Blanc faible (gris clair)

        //JPanel avec un fond blanc faible
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 240, 240)); // Blanc faible (gris clair)

        // Label pour afficher les informations
        scrollingTextLabel = new JLabel();
        scrollingTextLabel.setForeground(Color.BLACK); // Texte en noir
        scrollingTextLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scrollingTextLabel.setHorizontalAlignment(JLabel.CENTER); // Centrer le texte

        // Effet d'affichage progressif
        Timer timer = new Timer(100, new ActionListener() { // 100 ms = 0.1 seconde
            private int lineIndex = 0;
            private int charIndex = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (lineIndex < infoLines.length) {
                    String currentLine = infoLines[lineIndex];
                    if (charIndex < currentLine.length()) {
                        String displayedText = currentLine.substring(0, charIndex + 1);
                        scrollingTextLabel.setText(displayedText);
                        charIndex++;
                    } else {
                        lineIndex++;
                        charIndex = 0;
                        if (lineIndex < infoLines.length) {
                            scrollingTextLabel.setText("");
                        }
                    }
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();

        contentPanel.add(scrollingTextLabel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // Configurer la taille du pannea
        setPreferredSize(new Dimension(600, 200));
    }

    public static void main(String[] args) {
        // Lancer l'application avec le JPanel
        JFrame frame = new JFrame("À propos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 200);
        frame.setLocationRelativeTo(null);
        frame.add(new AboutPanel());
        frame.setVisible(true);
    }
}