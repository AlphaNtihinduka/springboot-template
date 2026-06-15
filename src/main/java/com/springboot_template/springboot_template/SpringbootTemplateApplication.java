package com.springboot_template.springboot_template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class SpringbootTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootTemplateApplication.class, args);
	}

}
