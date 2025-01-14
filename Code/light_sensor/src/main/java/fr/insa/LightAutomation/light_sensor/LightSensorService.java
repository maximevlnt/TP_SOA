package fr.insa.LightAutomation.light_sensor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import fr.insa.LightAutomation.light_sensor.model.ConnectionDatabase;
import fr.insa.LightAutomation.light_sensor.model.LightSensorData;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import java.util.Random;

@Service
public class LightSensorService {
	
	
	public static float light_value;

	// Méthode pour simuler la récupération de la valeur du capteur et l'enregistrement dans la base de données
    @Scheduled(fixedRate = 12000)  // Exécute toutes les 12 secondes (valeur aléatoire entre 10 et 15 secondes)
    public void simulateAndInsertLuminosity() {
        try (Connection conn = ConnectionDatabase.getConnection()) {
            // Simuler une valeur de luminosité entre 0 et 1000 lux avec moins de variation
            float luminosityValue = 150 + (new Random().nextFloat() * 500);  // Valeur entre 500 et 1000 lux
            
            light_value = luminosityValue;
            // Insertion de la donnée dans la table luminosite
            String insertLuminositySQL = "INSERT INTO luminosite (valeur_luminosite, horodatage) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(insertLuminositySQL)) {
                preparedStatement.setFloat(1, luminosityValue);
                preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.executeUpdate();
                System.out.println("Inserted luminosity value: " + luminosityValue + " lux");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static float getValue() {
    	try {
			System.out.println("Send status: " + light_value);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return light_value;
    }
}
