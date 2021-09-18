package com.krishna;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
public class SpringBootSwaggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSwaggerApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public UUID uuid() {
		return UUID.randomUUID();
	}
	
	 @Bean
	  public OpenAPI springUserOpenAPI() {
	      return new OpenAPI()
	              .info(new Info().title("Spring Boot User API")
	              .description("Spring Boot Swagger Sample Application")
	              .version("v1.0")
	              .license(new License().name("Apache 2.0").url("http://springdoc.org")));
	  }
}
