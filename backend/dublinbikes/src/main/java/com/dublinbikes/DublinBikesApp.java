package com.dublinbikes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DublinBikesApp {

	public static void main(String[] args) {
		SpringApplication.run(DublinBikesApp.class, args);
	}

}
