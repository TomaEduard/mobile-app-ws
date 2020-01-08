package com.springframework;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry
//		.addMapping("/users/*")
		.addMapping("/**")
//		.allowedMethods("GET", "POST", "PUT", "DELETE")
		.allowedMethods("*")
		.allowedOrigins("*");

	}
	
}
