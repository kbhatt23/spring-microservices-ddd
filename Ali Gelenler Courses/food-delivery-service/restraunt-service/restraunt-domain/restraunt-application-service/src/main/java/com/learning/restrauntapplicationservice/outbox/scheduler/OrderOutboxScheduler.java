package com.learning.restrauntapplicationservice.outbox.scheduler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learning.outbox.OutboxScheduler;
import com.learning.outbox.OutboxStatus;
import com.learning.restrauntapplicationservice.outbox.model.OrderOutboxMessage;
import com.learning.restrauntapplicationservice.output.RestrauntApprovalResponseMessagePublisher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderOutboxScheduler implements OutboxScheduler {

	private final OrderOutboxHelper orderOutboxHelper;
	private final RestrauntApprovalResponseMessagePublisher responseMessagePublisher;

	public OrderOutboxScheduler(OrderOutboxHelper orderOutboxHelper,
			RestrauntApprovalResponseMessagePublisher responseMessagePublisher) {
		this.orderOutboxHelper = orderOutboxHelper;
		this.responseMessagePublisher = responseMessagePublisher;
	}

	@Transactional
	@Scheduled(fixedRateString = "${restaurant-service.outbox-scheduler-fixed-rate}", initialDelayString = "${restaurant-service.outbox-scheduler-initial-delay}")
	@Override
	public void processOutboxMessage() {
		Optional<List<OrderOutboxMessage>> outboxMessagesResponse = orderOutboxHelper
				.getOrderOutboxMessageByOutboxStatus(OutboxStatus.STARTED);
		if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
			List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();
			log.info("Received {} OrderOutboxMessage with ids {}, sending to message bus!", outboxMessages.size(),
					outboxMessages.stream().map(outboxMessage -> outboxMessage.getId().toString())
							.collect(Collectors.joining(",")));
			outboxMessages.forEach(orderOutboxMessage -> responseMessagePublisher.publish(orderOutboxMessage,
					orderOutboxHelper::updateOutboxStatus));
			log.info("{} OrderOutboxMessage sent to message bus!", outboxMessages.size());
		}
	}

}