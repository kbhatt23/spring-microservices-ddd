package com.learning.cloudstream.streamconsumer.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface GreetingSource {
	
	@Output("greetingSourceChannel")
	MessageChannel greetingSource();
}
