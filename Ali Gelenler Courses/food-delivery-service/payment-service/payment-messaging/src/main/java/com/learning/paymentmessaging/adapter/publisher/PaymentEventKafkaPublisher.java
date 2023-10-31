package com.learning.paymentmessaging.adapter.publisher;

import java.util.function.BiConsumer;

import org.springframework.stereotype.Component;

import com.learning.kafkamodel.order.PaymentResponseAvroModel;
import com.learning.kafkaproducer.service.KafkaEventHelper;
import com.learning.kafkaproducer.service.KafkaProducer;
import com.learning.outbox.OutboxStatus;
import com.learning.paymentapplicationservice.config.PaymentServiceConfigData;
import com.learning.paymentapplicationservice.outbox.model.OrderEventPayload;
import com.learning.paymentapplicationservice.outbox.model.OrderOutboxMessage;
import com.learning.paymentapplicationservice.output.PaymentResponseMessagePublisher;
import com.learning.paymentmessaging.adapter.helper.PaymentMessagingDataMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentEventKafkaPublisher implements PaymentResponseMessagePublisher {

	private final PaymentMessagingDataMapper paymentMessagingDataMapper;
	private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
	private final PaymentServiceConfigData paymentServiceConfigData;
	private final KafkaEventHelper kafkaEventHelper;

	public PaymentEventKafkaPublisher(PaymentMessagingDataMapper paymentMessagingDataMapper,
			KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer,
			PaymentServiceConfigData paymentServiceConfigData, KafkaEventHelper kafkaMessageHelper) {
		this.paymentMessagingDataMapper = paymentMessagingDataMapper;
		this.kafkaProducer = kafkaProducer;
		this.paymentServiceConfigData = paymentServiceConfigData;
		this.kafkaEventHelper = kafkaMessageHelper;
	}

	@Override
	public void publish(OrderOutboxMessage orderOutboxMessage,
			BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {
		OrderEventPayload orderEventPayload = kafkaEventHelper.getOrderEventPayload(orderOutboxMessage.getPayload(),
				OrderEventPayload.class);

		String sagaId = orderOutboxMessage.getSagaId().toString();

		log.info("Received OrderOutboxMessage for order id: {} and saga id: {}", orderEventPayload.getOrderId(),
				sagaId);

		try {
			PaymentResponseAvroModel paymentResponseAvroModel = paymentMessagingDataMapper
					.orderEventPayloadToPaymentResponseAvroModel(sagaId, orderEventPayload);

			String paymentResponseTopicName = paymentServiceConfigData.getPaymentResponseTopicName();
			kafkaProducer.publish(paymentResponseTopicName, sagaId, paymentResponseAvroModel,
					kafkaEventHelper.callBack(paymentResponseTopicName, paymentResponseAvroModel,
							this.getClass().getSimpleName(), orderOutboxMessage, outboxCallback));

			log.info("PaymentResponseAvroModel sent to kafka for order id: {} and saga id: {}",
					paymentResponseAvroModel.getOrderId(), sagaId);
		} catch (Exception e) {
			log.error(
					"Error while sending PaymentRequestAvroModel message"
							+ " to kafka with order id: {} and saga id: {}, error: {}",
					orderEventPayload.getOrderId(), sagaId, e.getMessage());
		}
	}
}