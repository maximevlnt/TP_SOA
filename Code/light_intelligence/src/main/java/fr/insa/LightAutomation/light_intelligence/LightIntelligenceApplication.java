package fr.insa.LightAutomation.light_intelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 

public class LightIntelligenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LightIntelligenceApplication.class, args);
	}

}
