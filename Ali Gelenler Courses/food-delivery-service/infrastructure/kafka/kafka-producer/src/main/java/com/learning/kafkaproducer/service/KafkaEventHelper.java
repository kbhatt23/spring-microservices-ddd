package com.learning.kafkaproducer.service;

import java.util.function.BiConsumer;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.orderservice.core.exceptions.OrderDomainException;
import com.learning.outbox.OutboxStatus;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaEventHelper {

	private final ObjectMapper objectMapper;

	public KafkaEventHelper(ObjectMapper objectMapper) {
		super();
		this.objectMapper = objectMapper;
	}

	public <T, U> ListenableFutureCallback<SendResult<String, T>> callBack(String topicName, T modelEvent,
			String eventType, U outboxMessage, BiConsumer<U, OutboxStatus> outboxCallback) {

		return new ListenableFutureCallback<SendResult<String, T>>() {

			@Override
			public void onSuccess(SendResult<String, T> result) {
				RecordMetadata recordMetadata = result.getRecordMetadata();
				log.info(
						"callBack: succesfully sent eventType: {} to topic: {}, partition: {},"
								+ "offset: {}, timestamp: {} with eventPayload: {}",
						eventType, recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(),
						recordMetadata.timestamp(), modelEvent);

				if(outboxCallback != null && outboxMessage != null)
					outboxCallback.accept(outboxMessage, OutboxStatus.COMPLETED);
			}

			@Override
			public void onFailure(Throwable ex) {
				log.error("callBack: unable to send eventType: {} to topic: {}, with error:{}, with event: {}",
						eventType, topicName, ex, modelEvent);

				if(outboxCallback != null && outboxMessage != null)
					outboxCallback.accept(outboxMessage, OutboxStatus.FAILED);
			}

		};
	}

	public <T> T getOrderEventPayload(String payload, Class<T> outputType) {
		try {
			return objectMapper.readValue(payload, outputType);
		} catch (JsonProcessingException e) {
			log.error("Could not read {} object!", outputType.getName(), e);
			throw new OrderDomainException("Could not read " + outputType.getName() + " object!", e);
		}
	}
}
