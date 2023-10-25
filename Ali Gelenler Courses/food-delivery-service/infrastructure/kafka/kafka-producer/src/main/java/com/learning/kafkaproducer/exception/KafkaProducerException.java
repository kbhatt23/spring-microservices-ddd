package com.learning.kafkaproducer.exception;

public class KafkaProducerException extends RuntimeException{

	public KafkaProducerException(String message) {
        super(message);
    }
}
