package com.learning.kafkaproducer.service;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.learning.kafkaproducer.exception.KafkaProducerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

	private final KafkaTemplate<K, V> kafkaTemplate;

	public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public void publish(String topicName, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback) {
		try {
			CompletableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
			kafkaResultFuture.whenComplete(callback);
		} catch (Exception e) {
			log.error("Error on kafka producer with key: {}, message: {} and exception: {}", key, message,
					e.getMessage());
			throw new KafkaProducerException("Error on kafka producer with key: " + key + " and message: " + message);
		}
	}

}
