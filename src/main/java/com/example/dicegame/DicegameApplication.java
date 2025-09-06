package com.example.dicegame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.dicegame.model.entity")
public class DicegameApplication {

	public static void main(String[] args) {
		SpringApplication.run(DicegameApplication.class, args);
	}

}
