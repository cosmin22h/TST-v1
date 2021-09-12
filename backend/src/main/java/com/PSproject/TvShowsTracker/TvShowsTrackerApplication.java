package com.PSproject.TvShowsTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class TvShowsTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TvShowsTrackerApplication.class, args);
	}

}
