package com.smartcontactmanager.smartcontactmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan("com.smartcontactmanager.smartcontactmanager")
@Configuration
public class SmartcontactmanagerApplication implements WebMvcConfigurer{

	String your_drive_location = "file:///D://smartcontact_images//"; // my file path
	 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/***").addResourceLocations(your_drive_location);
    }
	public static void main(String[] args) {
		SpringApplication.run(SmartcontactmanagerApplication.class, args);
		System.out.println("project started...");
		// System.out.println("good project...");
		// System.out.println("password : a612c480-7b30-4472-bd53-d122863b4a56");
	}
}
