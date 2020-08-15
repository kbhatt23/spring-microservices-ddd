package com.learning.cloudstream.streamconsumer.bean.ddd.aggregator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
public class GreetingAggregator extends AbstractAggregateRoot<GreetingAggregator>{
	@Column
	private String message;
	@Id
	private int id;
	

	public GreetingAggregator() {
	}

	public GreetingAggregator(String message, int id) {
		this.message = message;
		this.id = id;
		addAggregatorToEvent(this);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Greeting [message=" + message + ", id=" + id + "]";
	}
	
	  public void addAggregatorToEvent(GreetingAggregator aggregator) {
	        // some domain operation
	        registerEvent(aggregator);
	    }
}
