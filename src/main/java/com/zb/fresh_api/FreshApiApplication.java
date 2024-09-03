package com.zb.fresh_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FreshApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreshApiApplication.class, args);
	}

}
