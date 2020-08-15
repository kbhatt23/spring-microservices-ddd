package com.learning.cloudstream.idgeneratorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IdGeneratorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdGeneratorServiceApplication.class, args);
	}

}
