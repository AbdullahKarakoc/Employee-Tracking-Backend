package com.EmployeeTracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class EmployeeTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeTrackingApplication.class, args);
	}
}
