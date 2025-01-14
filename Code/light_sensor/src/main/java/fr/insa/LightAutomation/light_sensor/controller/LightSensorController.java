package fr.insa.LightAutomation.light_sensor.controller;

import fr.insa.LightAutomation.light_sensor.LightSensorService;

import fr.insa.LightAutomation.light_sensor.model.LightSensorData;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/light-sensor")
public class LightSensorController {

    private final LightSensorService lightSensorService;

    public LightSensorController(LightSensorService lightSensorService) {
        this.lightSensorService = lightSensorService;
    }
    
    @GetMapping("/value")
    public ResponseEntity<Float> getStatus() {
        float status = (float) -1.0;
		try {
			status = LightSensorService.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return ResponseEntity.ok(status);
    }

}
