package com.learning.kafkaproducer.service;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaEventHelper {

	public <T> ListenableFutureCallback<SendResult<String, T>> callBack(String topicName, T modelEvent,
			String eventType) {

		return new ListenableFutureCallback<SendResult<String, T>>() {

			@Override
			public void onSuccess(SendResult<String, T> result) {
				RecordMetadata recordMetadata = result.getRecordMetadata();
				log.info(
						"callBack: succesfully sent eventType: {} to topic: {}, partition: {},"
								+ "offset: {}, timestamp: {} with eventPayload: {}",
						eventType, recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(),
						recordMetadata.timestamp(), modelEvent);
			}

			@Override
			public void onFailure(Throwable ex) {
				log.error("callBack: unable to send eventType: {} to topic: {}, with error:{}, with event: {}",
						eventType, topicName, ex, modelEvent);
			}

		};
	}
}
