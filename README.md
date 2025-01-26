# Gestion des Absences - Université de Technologie d'Abidjan

## Description

Ce projet est une application de bureau (desktop) développée en Java Swing avec Maven. Elle vise à faciliter la gestion des absences pour l'Université de Technologie d'Abidjan en permettant de suivre les présences et absences des étudiants de manière efficace et intuitive.

---

## Fonctionnalités principales

- **Gestion des étudiants :** Ajouter, modifier, ou supprimer les informations des étudiants (nom, prénom, matricule, etc.).
- **Suivi des absences :** Enregistrer et visualiser les absences pour chaque étudiant selon les cours et les dates.
- **Rapports :** Générer des rapports détaillés sur les absences par étudiant, par classe ou par matière.
- **Authentification :** Système de connexion pour sécuriser l'accès à l'application (administrateurs uniquement).
- **Interface intuitive :** Une interface utilisateur conviviale utilisant Java Swing pour une navigation fluide.

---

## Technologies utilisées

- **Langage principal :** Java
- **Interface utilisateur :** Java Swing
- **Gestion de projet :** Maven
- **Base de données :** Mysql
- **Versionnement :** Git

---

## Prérequis

Avant de démarrer, assurez-vous d'avoir les outils suivants installés sur votre machine :

1. [Java JDK (version 11 ou supérieure)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
2. [Apache Maven](https://maven.apache.org/)
3. [PostgreSQL](https://www.postgresql.org/)
4. [Git](https://git-scm.com/)

---

## Installation

1. **Cloner le projet :**
```bash
git clone git@github.com:PyCerveau/gestion_absences.git
cd gestion_absences
```

2. **Configurer la base de données :**
   - Créez une base de données PostgreSQL nommée `gestion_absences`.
   - Importez les tables nécessaires (fichiers SQL fournis dans le dossier `db`).

3. **Configurer l'application :**
   - Modifiez le fichier `src/main/resources/application.properties` pour renseigner vos informations de connexion PostgreSQL (URL, utilisateur, mot de passe).

4. **Compiler et exécuter l'application :**
```bash
mvn clean install
java -jar target/gestion-absences.jar
```

---

## Structure du projet

- **src/main/java** : Contient les fichiers source Java.
- **src/main/resources** : Contient les fichiers de configuration et les ressources (ex. fichiers SQL, images).
- **target** : Contient les fichiers générés après compilation.
- **pom.xml** : Fichier de configuration Maven.

---

## Contributions

Les contributions sont les bienvenues ! Suivez ces étapes :

1. Forkez le projet.
2. Créez une branche pour votre fonctionnalité :
   ```bash
   git checkout -b ma-nouvelle-fonctionnalite
   ```
3. Effectuez vos modifications et commitez-les :
   ```bash
   git commit -m "Ajout d'une nouvelle fonctionnalité"
   ```
4. Poussez vos changements :
   ```bash
   git push origin ma-nouvelle-fonctionnalite
   ```
5. Ouvrez une Pull Request.

---

## Auteur

Ce projet a été créé et est maintenu par **PyCerveau** (pycerveau70@gmail.com).

---

## Licence

Ce projet est sous licence MIT. Consultez le fichier [LICENSE](LICENSE) pour plus d'informations.

---

## Contact

Pour toute question ou suggestion, n'hésitez pas à contacter l'équipe à pycerveau70@gmail.com.
