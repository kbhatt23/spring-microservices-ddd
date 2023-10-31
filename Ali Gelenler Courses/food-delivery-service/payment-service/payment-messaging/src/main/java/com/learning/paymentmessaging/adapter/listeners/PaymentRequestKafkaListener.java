package com.learning.paymentmessaging.adapter.listeners;

import java.sql.SQLException;
import java.util.List;

import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.learning.kafkaconsumer.KafkaConsumer;
import com.learning.kafkamodel.order.PaymentOrderStatus;
import com.learning.kafkamodel.order.PaymentRequestAvroModel;
import com.learning.paymentapplicationservice.command.PaymentRequest;
import com.learning.paymentapplicationservice.exceptions.PaymentApplicationServiceException;
import com.learning.paymentapplicationservice.input.PaymentRequestMessageListener;
import com.learning.paymentdomaincore.exceptions.PaymentNotFoundException;
import com.learning.paymentmessaging.adapter.helper.PaymentMessagingDataMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {

	private final PaymentMessagingDataMapper paymentMessagingDataMapper;

	private final PaymentRequestMessageListener paymentRequestMessageListener;

	public PaymentRequestKafkaListener(PaymentMessagingDataMapper paymentMessagingDataMapper,
			PaymentRequestMessageListener paymentRequestMessageListener) {
		this.paymentMessagingDataMapper = paymentMessagingDataMapper;
		this.paymentRequestMessageListener = paymentRequestMessageListener;
	}

	@Override
	@KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${payment-service.payment-request-topic-name}")
	public void receive(@Payload List<PaymentRequestAvroModel> messages,
			@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
			@Header(KafkaHeaders.OFFSET) List<Long> offsets) {

		log.info("{} number of payment requests received with keys:{}, partitions:{} and offsets: {}", messages.size(),
				keys.toString(), partitions.toString(), offsets.toString());

		messages.forEach(paymentRequestAvro -> {
			try {
				PaymentRequest paymentRequest = paymentMessagingDataMapper
						.paymentRequestAvroModelToPaymentRequest(paymentRequestAvro);
				if (paymentRequestAvro.getPaymentOrderStatus() == PaymentOrderStatus.PENDING) {
					log.info("Processing payment for order id: {}", paymentRequestAvro.getOrderId());
					paymentRequestMessageListener.completePayment(paymentRequest);
				} else if (paymentRequestAvro.getPaymentOrderStatus() == PaymentOrderStatus.CANCELLED) {
					log.info("Cancelling payment for order id: {}", paymentRequestAvro.getOrderId());
					paymentRequestMessageListener.cancelPayment(paymentRequest);
				}
			} catch (DataAccessException e) {
				SQLException sqlException = (SQLException) e.getRootCause();
				if (sqlException != null && sqlException.getSQLState() != null
						&& PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
					// NO-OP for unique constraint exception
					log.error(
							"Caught unique constraint exception with sql state: {} "
									+ "in PaymentRequestKafkaListener for order id: {}",
							sqlException.getSQLState(), paymentRequestAvro.getOrderId());
				} else {
					throw new PaymentApplicationServiceException(
							"Throwing DataAccessException in" + " PaymentRequestKafkaListener: " + e.getMessage(), e);
				}
			} catch (PaymentNotFoundException e) {
				// NO-OP for PaymentNotFoundException
				log.error("No payment found for order id: {}", paymentRequestAvro.getOrderId());
			}
		});
	}

}
