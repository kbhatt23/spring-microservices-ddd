package com.learning.cloudstream.idgeneratorservice;

import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generate")
public class IDGeneratorController {

	@GetMapping
	public int generateRandomNumber() {
		Random random = new Random();
		int randomID = random.nextInt();
		return randomID;
	}
}
