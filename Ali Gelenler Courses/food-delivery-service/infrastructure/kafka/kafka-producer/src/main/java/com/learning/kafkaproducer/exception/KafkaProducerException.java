package com.learning.kafkaproducer.exception;

public class KafkaProducerException extends RuntimeException{

	private static final long serialVersionUID = 5312630444499859593L;

	public KafkaProducerException(String message) {
        super(message);
    }
}
