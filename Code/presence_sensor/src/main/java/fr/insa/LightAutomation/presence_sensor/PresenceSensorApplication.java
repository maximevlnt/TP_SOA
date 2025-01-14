package fr.insa.LightAutomation.presence_sensor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PresenceSensorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PresenceSensorApplication.class, args);
	}

}
