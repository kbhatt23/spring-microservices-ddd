package com.learning.cloudstream.streamconsumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.learning.cloudstream.streamconsumer.bean.Greeting;
import com.learning.cloudstream.streamconsumer.bean.ddd.aggregator.GreetingAggregator;
@Service
public class GreetingAggregatorService {
	@Autowired
	private GreetingCommandService greetingCommandService;
	
	@Autowired
	private RestTemplate restTemplate;

	public GreetingAggregator populateGreetingAggregator(Greeting greeting) {
		ResponseEntity<Integer> greetingIdResponse = restTemplate.getForEntity("http://id-generator-service/generate", Integer.class, new Object[] {});
		int greetingId = greetingIdResponse.getBody();
		GreetingAggregator greetingAggregator = new GreetingAggregator(greeting.getMessage(), greetingId);
		greetingCommandService.createGreeting(greetingAggregator);
		return greetingAggregator;
	}
	public GreetingAggregator findGreeting(int id) {
		return greetingCommandService.findGreeting(id);
	}
}

