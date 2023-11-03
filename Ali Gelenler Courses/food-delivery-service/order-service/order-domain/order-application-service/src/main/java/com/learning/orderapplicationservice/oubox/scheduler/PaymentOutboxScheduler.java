package com.learning.orderapplicationservice.oubox.scheduler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learning.orderapplicationservice.oubox.model.OrderPaymentOutboxMessage;
import com.learning.orderapplicationservice.ports.output.PaymentRequestOutboxMessagePublisher;
import com.learning.outbox.OutboxScheduler;
import com.learning.outbox.OutboxStatus;
import com.learning.saga.SagaStatus;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentOutboxScheduler implements OutboxScheduler {

	private final PaymentOutboxHelper paymentOutboxHelper;

	private final PaymentRequestOutboxMessagePublisher paymentRequestOutboxMessagePublisher;

	public PaymentOutboxScheduler(PaymentOutboxHelper paymentOutboxHelper,
			PaymentRequestOutboxMessagePublisher paymentRequestOutboxMessagePublisher) {
		this.paymentOutboxHelper = paymentOutboxHelper;
		this.paymentRequestOutboxMessagePublisher = paymentRequestOutboxMessagePublisher;
	}

	@Override
	@Transactional
	@Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}", initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
	public void processOutboxMessage() {

		Optional<List<OrderPaymentOutboxMessage>> outboxMessagesResponse = paymentOutboxHelper
				.getPaymentOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus.STARTED, SagaStatus.STARTED,
						SagaStatus.COMPENSATING);

		if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
			List<OrderPaymentOutboxMessage> outboxMessages = outboxMessagesResponse.get();
			log.info("Received {} OrderPaymentOutboxMessage with ids: {}, sending to message bus!",
					outboxMessages.size(), outboxMessages.stream()
							.map(outboxMessage -> outboxMessage.getId().toString()).collect(Collectors.joining(",")));
			outboxMessages.forEach(
					outboxMessage -> paymentRequestOutboxMessagePublisher.publish(outboxMessage, this::updateOutboxStatus));
			log.info("{} OrderPaymentOutboxMessage sent to message bus!", outboxMessages.size());
		}
	}

	public void updateOutboxStatus(OrderPaymentOutboxMessage orderPaymentOutboxMessage, OutboxStatus outboxStatus) {
		orderPaymentOutboxMessage.setOutboxStatus(outboxStatus);
		paymentOutboxHelper.save(orderPaymentOutboxMessage);
	}

}