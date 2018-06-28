package io.nelium.archive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication(exclude = {WebMvcAutoConfiguration.class})
public class LongitudeApplication {
	public static void main(String[] args) {
		SpringApplication.run(LongitudeApplication.class, args);
	}
}
