package com.learning.cloudstream.streamconsumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.learning.cloudstream.streamconsumer.config.IdGeneratorFeign;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class IDGeneratorService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IdGeneratorFeign feignClient;
	
	private boolean isFeignNeeded = true;
	
	
	@HystrixCommand(fallbackMethod = "generateIdFallack",commandKey = "idGenerator", threadPoolKey = "idGenerator", 
            commandProperties = {@HystrixProperty(name="execution.timeout.enabled", value="false")})
	public int generateId() {
		System.out.println("Starting generation fo greeting ID");
		if(isFeignNeeded) {
			return feignClient.generateGreetingId();
		}
		//just removing for demo purpose
		ResponseEntity<Integer> greetingIdResponse = restTemplate.getForEntity("http://id-generator-service/generate", Integer.class, new Object[] {});
		//ResponseEntity<Integer> greetingIdResponse = restTemplate.getForEntity("http://fake-generator-service/generate", Integer.class, new Object[] {});
		
		int greetingId = greetingIdResponse.getBody();
		return greetingId;
	}
	public int generateIdFallack() {
		System.out.println("returnig fallback greeting ID");
		return 23;
	}
}
