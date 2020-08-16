package com.learning.cloudstream.streamconsumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.learning.cloudstream.streamconsumer.bean.Greeting;
import com.learning.cloudstream.streamconsumer.bean.ddd.aggregator.GreetingAggregator;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
@Service
public class GreetingAggregatorService {
	@Autowired
	private GreetingCommandService greetingCommandService;
	
	@Autowired
	private IDGeneratorService idGeneratorService;

	public GreetingAggregator populateGreetingAggregator(Greeting greeting) {
		int greetingId = idGeneratorService.generateId();
		GreetingAggregator greetingAggregator = new GreetingAggregator(greeting.getMessage(), greetingId);
		greetingCommandService.createGreeting(greetingAggregator);
		return greetingAggregator;
	}
	public GreetingAggregator findGreeting(int id) {
		return greetingCommandService.findGreeting(id);
	}
	}

