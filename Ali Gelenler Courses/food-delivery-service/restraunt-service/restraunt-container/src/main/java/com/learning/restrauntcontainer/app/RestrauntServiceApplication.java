package com.learning.restrauntcontainer.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.learning"})
@EntityScan(basePackages = {"com.learning"})
@EnableJpaRepositories(basePackages = {"com.learning"})
public class RestrauntServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestrauntServiceApplication.class, args);
	}

}
