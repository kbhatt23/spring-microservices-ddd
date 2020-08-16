package com.learning.cloudstream.idgeneratorservice;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generate")
@RefreshScope
public class IDGeneratorController {
	
	@Value("${greetingMessage:default Generating Greeting ID}")
	private String greetingMsg;
	
	@GetMapping
	public int generateRandomNumber() {
		System.out.println(greetingMsg);
		Random random = new Random();
		int randomID = random.nextInt();
		return randomID;
	}
}
