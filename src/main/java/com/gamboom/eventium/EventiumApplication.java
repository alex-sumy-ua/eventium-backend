package com.gamboom.eventium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gamboom.eventium")
public class EventiumApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventiumApplication.class, args);
	}

}
