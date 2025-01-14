package fr.insa.LightAutomation.light_intelligence;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class LightIntelligenceService {

    private final RestTemplate restTemplate;

    // URLs des microservices
    private final String presenceSensorUrl = "http://localhost:8081/api/presence-sensor";
    private final String lightSensorUrl = "http://localhost:8082/api/light-sensor";
    private final String lightActuatorUrl = "http://localhost:8083/api/light-actuator";

    // Variable pour suivre l'état actuel des lumières
    private boolean isLightOn;

    private boolean isInitialized = false; // Indique si l'initialisation a été effectuée

    public LightIntelligenceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Évaluation et contrôle des lumières toutes les 1 seconde
    @Scheduled(fixedRate = 1000)
    public void evaluateAndControlLights() {
        try {
            // Effectuer l'initialisation une seule fois
            if (!isInitialized) {
                initializeLightState();
                isInitialized = true;
            }

            // Obtenir les données de présence
            boolean presenceDetected = fetchPresenceStatus();

            // Obtenir les données de luminosité
            float lightIntensity = fetchLightIntensity();

            System.out.println("Presence Detected: " + presenceDetected);
            System.out.println("Light Intensity: " + lightIntensity);

            // Décider du nouvel état des lumières
            boolean shouldLightBeOn = presenceDetected && lightIntensity < 300;

            // Envoyer une commande uniquement si l'état change
            if (shouldLightBeOn != isLightOn) {
                if (shouldLightBeOn) {
                    sendLightCommand("ON");
                } else {
                    sendLightCommand("OFF");
                }
                isLightOn = shouldLightBeOn; // Mettre à jour l'état actuel
            }

        } catch (Exception e) {
            System.err.println("Error during sensor evaluation: " + e.getMessage());
        }
    }

    // Méthode pour initialiser l'état des lumières au démarrage
    private void initializeLightState() {
        try {
            boolean presenceDetected = fetchPresenceStatus();
            float lightIntensity = fetchLightIntensity();

            System.out.println("Initial Presence Detected: " + presenceDetected);
            System.out.println("Initial Light Intensity: " + lightIntensity);

            // Décider de l'état initial des lumières
            isLightOn = presenceDetected && lightIntensity < 300;

            // Envoyer la commande correspondante
            if (isLightOn) {
                sendLightCommand("ON");
            } else {
                sendLightCommand("OFF");
            }

            System.out.println("Initial light state set to: " + (isLightOn ? "ON" : "OFF"));

        } catch (Exception e) {
            System.err.println("Error during initialization: " + e.getMessage());
        }
    }

    // Méthode pour récupérer les données de présence
    private boolean fetchPresenceStatus() {
        String url = presenceSensorUrl + "/status";
        return restTemplate.getForObject(url, Boolean.class);
    }

    // Méthode pour récupérer l’intensité lumineuse
    private float fetchLightIntensity() {
        String url = lightSensorUrl + "/value";
        return restTemplate.getForObject(url, Float.class);
    }

    // Méthode pour envoyer une commande à l’actionneur
    private void sendLightCommand(String command) {
        String url = lightActuatorUrl + "/command/" + command;
        restTemplate.postForLocation(url, command);
        System.out.println("Commande envoyée à l’actionneur : " + command);
    }
}
