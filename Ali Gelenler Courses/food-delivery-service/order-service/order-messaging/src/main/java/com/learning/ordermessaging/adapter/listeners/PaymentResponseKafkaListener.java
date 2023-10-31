package com.learning.ordermessaging.adapter.listeners;

import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.learning.kafkaconsumer.KafkaConsumer;
import com.learning.kafkamodel.order.PaymentResponseAvroModel;
import com.learning.kafkamodel.order.PaymentStatus;
import com.learning.orderapplicationservice.ports.input.PaymentResponseEventListener;
import com.learning.ordermessaging.helper.OrderMessagingMapper;
import com.learning.orderservice.core.exceptions.OrderNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

	private final PaymentResponseEventListener paymentResponseEventListener;

	private final OrderMessagingMapper orderMessagingMapper;

	public PaymentResponseKafkaListener(PaymentResponseEventListener paymentResponseEventListener,
			OrderMessagingMapper orderMessagingMapper) {
		super();
		this.paymentResponseEventListener = paymentResponseEventListener;
		this.orderMessagingMapper = orderMessagingMapper;
	}

	@Override
	@KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response-topic-name}")
	public void receive(@Payload List<PaymentResponseAvroModel> messages,
			@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
			@Header(KafkaHeaders.OFFSET) List<Long> offsets) {
		log.info("{} number of payment responses received with keys:{}, partitions:{} and offsets: {}", messages.size(),
				keys.toString(), partitions.toString(), offsets.toString());

		messages.forEach(message -> {
			try {
				if (PaymentStatus.COMPLETED == message.getPaymentStatus()) {
					log.info("Processing successful payment for order id: {}", message.getOrderId());
					paymentResponseEventListener
							.paymentApproved(orderMessagingMapper.paymentResponseAvroModelToPaymentResponse(message));
				} else if (PaymentStatus.CANCELLED == message.getPaymentStatus()
						|| PaymentStatus.FAILED == message.getPaymentStatus()) {
					log.info("Processing unsuccessful payment for order id: {}", message.getOrderId());
					paymentResponseEventListener
							.paymentCancelled(orderMessagingMapper.paymentResponseAvroModelToPaymentResponse(message));
				}
			} catch (OptimisticLockingFailureException e) {
				//NO-OP for optimistic lock. This means another thread finished the work, do not throw error to prevent reading the data from kafka again!
	               log.error("Caught optimistic locking exception in PaymentResponseKafkaListener for order id: {}",
	            		   message.getOrderId());
			} catch (OrderNotFoundException e) {
                //NO-OP for OrderNotFoundException
                log.error("No order found for order id: {}", message.getOrderId());
            }
		});

	}

}
