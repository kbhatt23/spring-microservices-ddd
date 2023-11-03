package com.learning.restrauntapplicationservice.outbox.scheduler;

import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learning.outbox.OutboxScheduler;
import com.learning.outbox.OutboxStatus;
import com.learning.restrauntapplicationservice.outbox.model.OrderOutboxMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderOutboxCleanerScheduler implements OutboxScheduler {

	private final OrderOutboxHelper orderOutboxHelper;

	public OrderOutboxCleanerScheduler(OrderOutboxHelper orderOutboxHelper) {
		this.orderOutboxHelper = orderOutboxHelper;
	}

	@Transactional
	@Scheduled(cron = "@midnight")
	@Override
	public void processOutboxMessage() {
		Optional<List<OrderOutboxMessage>> outboxMessagesResponse = orderOutboxHelper
				.getOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
		if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
			List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();
			log.info("Received {} OrderOutboxMessage for clean-up!", outboxMessages.size());
			orderOutboxHelper.deleteOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
			log.info("Deleted {} OrderOutboxMessage!", outboxMessages.size());
		}
	}
}