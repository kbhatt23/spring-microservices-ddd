package com.learning.cloudstream.streamconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.learning.cloudstream.streamconsumer.bean.Greeting;
import com.learning.cloudstream.streamconsumer.config.GreetingSink;

@SpringBootApplication
@EnableBinding(GreetingSink.class)
public class StreamConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamConsumerApplication.class, args);
	}
	
	
	@StreamListener("greetingSinkChannel")
	public void streamGreetings(Greeting greeting) {
		System.out.println("Greeting recieved "+greeting);
	}

}
