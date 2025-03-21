package com.example.ballup_backend;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.example.ballup_backend")
public class BallupBackendApplication {

	public static void main(String[] args) {
		// Dotenv dotenv = Dotenv.load();
        // dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(BallupBackendApplication.class, args);
		
		System.out.println("App run at : http://localhost:8080");
	}

}
