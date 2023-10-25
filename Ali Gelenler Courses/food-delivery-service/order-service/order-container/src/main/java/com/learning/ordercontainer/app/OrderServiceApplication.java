package com.learning.ordercontainer.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//all 3 are needed as this is multi-module project
@SpringBootApplication(scanBasePackages = { "com.learning" })
@EntityScan(basePackages = { "com.learning" })
@EnableJpaRepositories(basePackages = { "com.learning" })
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}
}
