package fr.insa.LightAutomation.light_actuator;
import fr.insa.LightAutomation.light_actuator.model.LightActuatorCommand;
import fr.insa.LightAutomation.light_actuator.model.LightActuatorStatus;
import fr.insa.LightAutomation.light_actuator.model.ConnectionDatabase;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

//LightActuatorService.java
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;


@Service
public class LightActuatorService {

	public void executeCommand(String command) {
        try (Connection conn = ConnectionDatabase.getConnection()) {
            // Insertion dans la table ordres
            String insertOrderSQL = "INSERT INTO ordres (action, horodatage) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(insertOrderSQL)) {
                preparedStatement.setString(1, command);
                preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.executeUpdate();
            }

            // Mise à jour de l'état dans la table etat_lumieres
            String updateStateSQL = "UPDATE etat_lumieres SET etat = ?, horodatage = ? WHERE id = 1";
            try (PreparedStatement preparedStatement = conn.prepareStatement(updateStateSQL)) {
                preparedStatement.setString(1, command.equalsIgnoreCase("ON") ? "ALLUMEE" : "ETEINTE");
                preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStatus() {
        try (Connection conn = ConnectionDatabase.getConnection()) {
            String query = "SELECT etat FROM etat_lumieres WHERE id = 1";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                	System.out.println(resultSet.getString("etat"));
                    return resultSet.getString("etat");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UNKNOWN";
    }
}
