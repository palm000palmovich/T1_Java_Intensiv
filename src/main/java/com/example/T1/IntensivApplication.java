package com.example.T1;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@OpenAPIDefinition
@SpringBootApplication
public class IntensivApplication{
	public static void main(String[] args) {
		SpringApplication.run(IntensivApplication.class, args);
	}

}
