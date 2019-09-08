package com.company.ReneSerulleU1Capstone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@SpringBootApplication
public class ReneSerulleU1CapstoneApplication {

	@Bean
	public Map<String, String> states() throws IOException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> states = mapper.readValue(
					new File("src/main/resources/json/states.json"),
					new TypeReference<Map<String, String>>(){}
			);
			return states;
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(ReneSerulleU1CapstoneApplication.class, args);
	}

}
