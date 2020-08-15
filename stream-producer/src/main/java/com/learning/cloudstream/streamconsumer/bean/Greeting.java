package com.learning.cloudstream.streamconsumer.bean;

public class Greeting {

	private String message;
	private int id;

	public Greeting() {
	}

	public Greeting(String message, int id) {
		this.message = message;
		this.id = id;
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
}
