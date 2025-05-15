package com.blink.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
@EntityScan("com.blink.backend.persistence.entity.*")
public class BlinkBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlinkBackendApplication.class, args);
	}

}