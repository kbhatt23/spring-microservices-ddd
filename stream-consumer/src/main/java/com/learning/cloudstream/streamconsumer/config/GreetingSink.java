package com.learning.cloudstream.streamconsumer.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface GreetingSink {
	
	//highest priority of channel name will be inside input
	//if input not present it takes method name as the channel nam
	@Input("greetingSinkChannel")
	SubscribableChannel greetingSink();
}
