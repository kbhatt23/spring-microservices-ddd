package com.learning.paymentmessaging.adapter.publisher;

import org.springframework.stereotype.Component;

import com.learning.kafkamodel.order.PaymentResponseAvroModel;
import com.learning.kafkaproducer.service.KafkaEventHelper;
import com.learning.kafkaproducer.service.KafkaProducer;
import com.learning.paymentapplicationservice.config.PaymentServiceConfigData;
import com.learning.paymentapplicationservice.output.PaymentCancelledMessagePublisher;
import com.learning.paymentdomaincore.events.PaymentCancelledEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentCancelledKafkaMessagePublisher implements PaymentCancelledMessagePublisher {

	private final PaymentMessagingDataMapper paymentMessagingDataMapper;

	private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;

	private final PaymentServiceConfigData paymentServiceConfigData;

	private final KafkaEventHelper kafkaEventHelper;
	
	public PaymentCancelledKafkaMessagePublisher(PaymentMessagingDataMapper paymentMessagingDataMapper,
			KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer,
			PaymentServiceConfigData paymentServiceConfigData, KafkaEventHelper kafkaEventHelper) {
		super();
		this.paymentMessagingDataMapper = paymentMessagingDataMapper;
		this.kafkaProducer = kafkaProducer;
		this.paymentServiceConfigData = paymentServiceConfigData;
		this.kafkaEventHelper = kafkaEventHelper;
	}

	@Override
	public void publish(PaymentCancelledEvent domainEvent) {
		String orderId = domainEvent.getPayment().getOrderId().getValue().toString();
		
		log.info("Received PaymentCancelledEvent for order id: {}", orderId);
		
		PaymentResponseAvroModel paymentResponseAvroModel = paymentMessagingDataMapper
				.paymentCancelledEventToPaymentResponseAvroModel(domainEvent);
		try {
			String topicName = paymentServiceConfigData.getPaymentResponseTopicName();
			kafkaProducer.publish(topicName, orderId, paymentResponseAvroModel,
					kafkaEventHelper.callBack(topicName, paymentResponseAvroModel, this.getClass().getSimpleName()));
			log.info("PaymentResponseAvroModel sent to kafka for order id: {}", orderId);
		} catch (Exception e) {
			log.error("Error while sending PaymentResponseAvroModel message" +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
		}

	}

}
