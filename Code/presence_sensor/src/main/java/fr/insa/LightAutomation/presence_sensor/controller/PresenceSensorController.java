package fr.insa.LightAutomation.presence_sensor.controller;

import fr.insa.LightAutomation.presence_sensor.PresenceSensorService;
import fr.insa.LightAutomation.presence_sensor.model.PresenceSensorData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/presence-sensor")
public class PresenceSensorController {

    private final PresenceSensorService presenceSensorService;

    public PresenceSensorController(PresenceSensorService presenceSensorService) {
        this.presenceSensorService = presenceSensorService;
    }
    
    @GetMapping("/status")
    public ResponseEntity<Integer> getStatus() {
        int status = -1;
		try {
			status = PresenceSensorService.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return ResponseEntity.ok(status);
    }

    
}