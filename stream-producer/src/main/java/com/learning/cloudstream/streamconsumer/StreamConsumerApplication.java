package com.learning.cloudstream.streamconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.learning.cloudstream.streamconsumer.config.GreetingSource;

@SpringBootApplication
@EnableBinding(GreetingSource.class)
@EnableDiscoveryClient
public class StreamConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamConsumerApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
