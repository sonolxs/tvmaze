package com.example.tvmaze;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TvmazeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TvmazeApplication.class, args);
	}

	@Bean
	CommandLineRunner run() {
		return args -> {
			System.out.println("Mongo URI: " + System.getenv("MONGO_URI"));
		};
	}
}
