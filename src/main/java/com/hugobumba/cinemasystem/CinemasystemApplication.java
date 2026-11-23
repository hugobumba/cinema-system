package com.hugobumba.cinemasystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CinemasystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemasystemApplication.class, args);
	}

}
