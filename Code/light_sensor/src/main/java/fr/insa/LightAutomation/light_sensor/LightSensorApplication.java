package fr.insa.LightAutomation.light_sensor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
public class LightSensorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LightSensorApplication.class, args);
	}

}
