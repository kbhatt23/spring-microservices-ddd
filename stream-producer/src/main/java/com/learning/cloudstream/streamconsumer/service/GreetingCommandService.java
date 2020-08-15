package com.learning.cloudstream.streamconsumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.cloudstream.streamconsumer.bean.ddd.aggregator.GreetingAggregator;
import com.learning.cloudstream.streamconsumer.bean.ddd.aggregator.GreetingRepository;
@Service
public class GreetingCommandService {

	@Autowired
	private GreetingRepository repostiory;
	
	public void createGreeting(GreetingAggregator greeting) {
		repostiory.save(greeting);
	}
	
	public GreetingAggregator findGreeting(int id) {
		return repostiory.findById(id).orElseThrow(() -> new RuntimeException("Greeting not found with id "+id));
	}
}
