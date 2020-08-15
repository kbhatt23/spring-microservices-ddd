package com.learning.cloudstream.streamconsumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import com.learning.cloudstream.streamconsumer.bean.ddd.aggregator.GreetingAggregator;
import com.learning.cloudstream.streamconsumer.config.GreetingSource;
@Service
public class GreetingEventService {

	@Autowired
	private GreetingSource greetingSource;
	
	@TransactionalEventListener
	public void sendGreetingMessage(GreetingAggregator greeting) {
		System.out.println("sending greeting aggregator event after succesful transaction");
		greetingSource.greetingSource().send(MessageBuilder.withPayload(greeting).build());
	}
}
