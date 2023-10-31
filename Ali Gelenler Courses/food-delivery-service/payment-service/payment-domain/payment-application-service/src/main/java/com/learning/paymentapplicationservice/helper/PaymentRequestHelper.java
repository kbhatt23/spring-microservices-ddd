package com.learning.paymentapplicationservice.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.commondomain.valueobjects.OrderId;
import com.learning.commondomain.valueobjects.PaymentStatus;
import com.learning.outbox.OutboxStatus;
import com.learning.paymentapplicationservice.command.PaymentRequest;
import com.learning.paymentapplicationservice.exceptions.PaymentApplicationServiceException;
import com.learning.paymentapplicationservice.mapper.PaymentDataMapper;
import com.learning.paymentapplicationservice.outbox.model.OrderOutboxMessage;
import com.learning.paymentapplicationservice.outbox.schedulers.OrderOutboxHelper;
import com.learning.paymentapplicationservice.output.CreditEntryRepository;
import com.learning.paymentapplicationservice.output.CreditHistoryRepository;
import com.learning.paymentapplicationservice.output.PaymentRepository;
import com.learning.paymentapplicationservice.output.PaymentResponseMessagePublisher;
import com.learning.paymentdomaincore.entities.CreditEntry;
import com.learning.paymentdomaincore.entities.CreditHistory;
import com.learning.paymentdomaincore.entities.Payment;
import com.learning.paymentdomaincore.events.PaymentEvent;
import com.learning.paymentdomaincore.exceptions.PaymentNotFoundException;
import com.learning.paymentdomaincore.services.PaymentDomainService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentRequestHelper {

	private final PaymentDomainService paymentDomainService;

	private final PaymentDataMapper paymentDataMapper;

	private final PaymentRepository paymentRepository;

	private final CreditEntryRepository creditEntryRepository;

	private final CreditHistoryRepository creditHistoryRepository;

	private final OrderOutboxHelper orderOutboxHelper;

	private final PaymentResponseMessagePublisher paymentResponseMessagePublisher;

	public PaymentRequestHelper(PaymentDomainService paymentDomainService, PaymentDataMapper paymentDataMapper,
			PaymentRepository paymentRepository, CreditEntryRepository creditEntryRepository,
			CreditHistoryRepository creditHistoryRepository, OrderOutboxHelper orderOutboxHelper,
			PaymentResponseMessagePublisher paymentResponseMessagePublisher) {
		this.paymentDomainService = paymentDomainService;
		this.paymentDataMapper = paymentDataMapper;
		this.paymentRepository = paymentRepository;
		this.creditEntryRepository = creditEntryRepository;
		this.creditHistoryRepository = creditHistoryRepository;
		this.orderOutboxHelper = orderOutboxHelper;
		this.paymentResponseMessagePublisher = paymentResponseMessagePublisher;
	}

	@Transactional
	public void persistPayment(PaymentRequest paymentRequest) {
		if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.COMPLETED)) {
			log.info("An outbox message with saga id: {} is already saved to database!", paymentRequest.getSagaId());
			return;
		}

		log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());

		Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);

		CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
		List<CreditHistory> creditHistory = getCreditHistory(payment.getCustomerId());

		List<String> failureMessages = new ArrayList<>();
		PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(payment, creditEntry, creditHistory,
				failureMessages);

		persistDbObjects(payment, creditEntry, creditHistory, failureMessages);

		orderOutboxHelper.saveOrderOutboxMessage(paymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
				paymentEvent.getPayment().getPaymentStatus(), OutboxStatus.STARTED,
				UUID.fromString(paymentRequest.getSagaId()));
	}

	private CreditEntry getCreditEntry(CustomerId customerId) {
		Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);
		if (creditEntry.isEmpty()) {
			log.error("Could not find credit entry for customer: {}", customerId.getValue());
			throw new PaymentApplicationServiceException(
					"Could not find credit entry for customer: " + customerId.getValue());
		}
		return creditEntry.get();
	}

	private List<CreditHistory> getCreditHistory(CustomerId customerId) {
		Optional<List<CreditHistory>> creditHistory = creditHistoryRepository.findByCustomerId(customerId);

		if (creditHistory.isEmpty()) {
			log.error("Could not find credit history for customer: {}", customerId.getValue());
			throw new PaymentApplicationServiceException(
					"Could not find credit history for customer: " + customerId.getValue());
		}
		return creditHistory.get();
	}

	@Transactional
	public void persistCancelPayment(PaymentRequest paymentRequest) {
		if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.CANCELLED)) {
			log.info("An outbox message with saga id: {} is already saved to database!", paymentRequest.getSagaId());
			return;
		}

		log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
		Payment existingPayment = paymentRepository
				.findByOrderId(new OrderId(UUID.fromString(paymentRequest.getOrderId()))).orElseThrow(() -> {
					log.error("Payment with order id: {} could not be found!", paymentRequest.getOrderId());
					throw new PaymentNotFoundException(
							"Payment with order id: " + paymentRequest.getOrderId() + " could not be found!");
				});
		CreditEntry creditEntry = getCreditEntry(existingPayment.getCustomerId());
		List<CreditHistory> creditHistories = getCreditHistory(existingPayment.getCustomerId());
		List<String> failureMessages = new ArrayList<>();
		PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(existingPayment, creditEntry,
				creditHistories, failureMessages);
		persistDbObjects(existingPayment, creditEntry, creditHistories, failureMessages);

		orderOutboxHelper.saveOrderOutboxMessage(paymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
				paymentEvent.getPayment().getPaymentStatus(), OutboxStatus.STARTED,
				UUID.fromString(paymentRequest.getSagaId()));
	}

	private void persistDbObjects(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories,
			List<String> failureMessages) {
		paymentRepository.save(payment);
		if (failureMessages.isEmpty()) {
			creditEntryRepository.save(creditEntry);
			creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
		}
	}

	// added publisher call for resilliency
	private boolean publishIfOutboxMessageProcessedForPayment(PaymentRequest paymentRequest,
			PaymentStatus paymentStatus) {
		Optional<OrderOutboxMessage> orderOutboxMessage = orderOutboxHelper
				.getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(UUID.fromString(paymentRequest.getSagaId()),
						paymentStatus);
		if (orderOutboxMessage.isPresent()) {
			paymentResponseMessagePublisher.publish(orderOutboxMessage.get(), orderOutboxHelper::updateOutboxMessage);
			return true;
		}
		return false;
	}
}
