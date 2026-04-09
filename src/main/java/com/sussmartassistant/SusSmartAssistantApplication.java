package com.sussmartassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SusSmartAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(SusSmartAssistantApplication.class, args);
	}
}
