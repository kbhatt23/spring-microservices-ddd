package com.learning.cloudstream.streamconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.cloudstream.streamconsumer.bean.Greeting;
import com.learning.cloudstream.streamconsumer.bean.ddd.aggregator.GreetingAggregator;
import com.learning.cloudstream.streamconsumer.service.GreetingAggregatorService;

@RestController
@RequestMapping("/v2/greet")
public class GreetingControllerV2 {
	@Autowired
	private GreetingAggregatorService service;
	
	@PostMapping
	public GreetingAggregator sendGreetingMessage(@RequestBody Greeting greeting) {
		System.out.println("Sending greeting message " + greeting);
		if(greeting.getId() > 0 || greeting.getId() < 0) {
			throw new RuntimeException("Greeting should not have id "+greeting);
		}
		
		return service.populateGreetingAggregator(greeting);
	}
	
	@GetMapping("/{greetingId}")
	public GreetingAggregator findGreeting(@PathVariable int greetingId) {
		return service.findGreeting(greetingId);
	}
}
