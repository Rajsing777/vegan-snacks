package com.examly.springapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.examly.springapp", "com.vegansnacks.app"})
@EntityScan(basePackages = {"com.examly.springapp", "com.vegansnacks.app"})
@EnableJpaRepositories(basePackages = {"com.examly.springapp", "com.vegansnacks.app"})
public class SpringappApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringappApplication.class, args);
	}

}
