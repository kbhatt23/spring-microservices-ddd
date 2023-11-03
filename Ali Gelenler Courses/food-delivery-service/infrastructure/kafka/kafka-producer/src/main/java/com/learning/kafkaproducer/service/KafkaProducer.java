package com.learning.kafkaproducer.service;

import java.io.Serializable;
import java.util.function.BiConsumer;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.SendResult;

public interface KafkaProducer<K extends Serializable, V extends SpecificRecordBase> {

	public void publish(String topicName , K key, V value, BiConsumer<SendResult<K, V>, Throwable> callback);
}
