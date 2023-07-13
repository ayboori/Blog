package com.example.hanghaeblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HangHaeBlogApplication {
	public static void main(String[] args) {
		SpringApplication.run(HangHaeBlogApplication.class, args);
	}

}
