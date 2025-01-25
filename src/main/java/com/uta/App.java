package com.uta;

import com.uta.login.LoginForm;

public class App {
    public static void main(String[] args) {
        // Démarrer l'application en affichant le formulaire de connexion
        LoginForm loginForm = new LoginForm();
        loginForm.showPage();
    }
}