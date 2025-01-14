package fr.insa.LightAutomation.light_intelligence.model;

import java.sql.Connection;
import java.sql.DriverManager;


public class ConnectionDatabase {

    public static Connection getConnection() {
        String BDD = "projet_gei_075";
        String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/" + BDD + "?serverTimezone=Europe/Paris";
        String user = "projet_gei_075";
        String passwd = "aaquue5H";

        try {
            // Établir la connexion et la renvoyer
            return DriverManager.getConnection(url, user, passwd);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur de connexion à la base de données");
            return null;
        }
    }
}
