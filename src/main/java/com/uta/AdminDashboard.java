package com.uta;

import javax.swing.*;
import java.util.jar.JarFile;

public class AdminDashboard extends JFrame{
    public AdminDashboard(){
        this.setTitle("Tableau de bord Enseignant");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JLabel label = new JLabel("Bienvenue, Enseigant !");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label);
    }
}
