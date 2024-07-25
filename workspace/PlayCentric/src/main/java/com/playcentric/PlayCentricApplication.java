package com.playcentric;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class PlayCentricApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlayCentricApplication.class, args);
	}

}
