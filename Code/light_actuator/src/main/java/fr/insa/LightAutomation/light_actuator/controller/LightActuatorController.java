package fr.insa.LightAutomation.light_actuator.controller;

import org.springframework.web.bind.annotation.RestController;

import fr.insa.LightAutomation.light_actuator.LightActuatorService;
import fr.insa.LightAutomation.light_actuator.model.LightActuatorCommand;
import fr.insa.LightAutomation.light_actuator.model.LightActuatorStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api/light-actuator")

public class LightActuatorController {

    private final LightActuatorService lightActuatorService;

    public LightActuatorController(LightActuatorService lightActuatorService) {
        this.lightActuatorService = lightActuatorService;
    }

    @PostMapping("/command/{command}")
    public ResponseEntity<String> executeCommand(@PathVariable String command) {
        lightActuatorService.executeCommand(command);
        return ResponseEntity.ok("Command executed: " + command);
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        String status = lightActuatorService.getStatus();
        return ResponseEntity.ok(status);
    }
}
