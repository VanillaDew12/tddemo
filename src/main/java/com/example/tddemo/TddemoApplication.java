package com.example.tddemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TddemoApplication.class, args);
	}


}
