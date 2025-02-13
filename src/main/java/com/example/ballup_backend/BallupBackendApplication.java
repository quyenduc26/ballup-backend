package com.example.ballup_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.ballup_backend")
public class BallupBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BallupBackendApplication.class, args);
		System.out.println("App run at : http://localhost:8080");
	}

}
