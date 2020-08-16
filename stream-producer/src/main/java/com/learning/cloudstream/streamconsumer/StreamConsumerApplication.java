package com.learning.cloudstream.streamconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.learning.cloudstream.streamconsumer.config.GreetingSource;
import com.learning.cloudstream.streamconsumer.config.RibbonConfiguration;

@SpringBootApplication(scanBasePackages = "com.learning.cloudstream")
@EnableBinding(GreetingSource.class)
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients
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
