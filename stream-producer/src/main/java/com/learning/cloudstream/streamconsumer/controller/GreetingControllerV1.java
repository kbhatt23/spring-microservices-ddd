package com.learning.cloudstream.streamconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.cloudstream.streamconsumer.bean.Greeting;
import com.learning.cloudstream.streamconsumer.config.GreetingSource;

@RestController
@RequestMapping("/v1")
public class GreetingControllerV1 {
	@Autowired
	private GreetingSource greetingSource;

	@PostMapping("/greet")
	public Greeting sendGreetingMessage(@RequestBody Greeting greeting) {
		System.out.println("Sending greeting message " + greeting);
		greetingSource.greetingSource().send(MessageBuilder.withPayload(greeting).build());
		return greeting;
	}
}
