package fr.insa.LightAutomation.presence_sensor;

import fr.insa.LightAutomation.presence_sensor.model.ConnectionDatabase;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import fr.insa.LightAutomation.presence_sensor.model.PresenceSensorData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PresenceSensorService {
	
	public static int presence_value;

	 // Simule l'état de présence et l'insère toutes les 30 secondes
    @Scheduled(fixedRate = 30000)  // Exécute toutes les 30 secondes
    public static void simulateAndInsertPresence() {
        try (Connection conn = ConnectionDatabase.getConnection()) {
            // Simuler un état de présence aléatoire (0 ou 1)
            Random random = new Random();
            int presenceStatus = random.nextInt(2);  // Valeur aléatoire entre 0 et 1
            presence_value = presenceStatus;
            // Insertion dans la table presence
            String insertPresenceSQL = "INSERT INTO presence (detection, horodatage) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(insertPresenceSQL)) {
                preparedStatement.setInt(1, presenceStatus);
                preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.executeUpdate();
                System.out.println("Inserted presence status: " + presenceStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int getStatus() {
    	try {
			System.out.println("Send status: " + presence_value);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return presence_value;
    }
    

}