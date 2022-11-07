package com.uber.rocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(RocketApplication.class, args);
	}

}
