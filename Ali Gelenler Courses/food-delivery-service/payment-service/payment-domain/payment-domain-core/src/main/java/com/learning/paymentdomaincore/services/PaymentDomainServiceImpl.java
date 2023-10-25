package com.learning.paymentdomaincore.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.learning.commondomain.constants.CommonDomainConstants;
import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.PaymentStatus;
import com.learning.paymentdomaincore.entities.CreditEntry;
import com.learning.paymentdomaincore.entities.CreditHistory;
import com.learning.paymentdomaincore.entities.Payment;
import com.learning.paymentdomaincore.events.PaymentCancelledEvent;
import com.learning.paymentdomaincore.events.PaymentCompletedEvent;
import com.learning.paymentdomaincore.events.PaymentEvent;
import com.learning.paymentdomaincore.events.PaymentFailedEvent;
import com.learning.paymentdomaincore.valueobjects.CreditHistoryId;
import com.learning.paymentdomaincore.valueobjects.TransactionType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService {

	@Override
	public PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
			List<CreditHistory> creditHistories, List<String> failureMessages,
			DomainEventPublisher<PaymentCompletedEvent> paymentCompleteDomainEventPublisher,
			DomainEventPublisher<PaymentFailedEvent> paymentFailedDomainEventPublisher) {
		payment.validatePayment(failureMessages);
		payment.initializePayment();

		validateCreditEntry(payment, creditEntry, failureMessages);
		subtractCreditEntry(payment, creditEntry);
		updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);

		validateCreditHistory(creditEntry, creditHistories, failureMessages);

		if (failureMessages.isEmpty()) {
			log.info("Payment is initiated for order id: {}", payment.getOrderId().getValue());
			payment.updateStatus(PaymentStatus.COMPLETED);
			return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)),
					paymentCompleteDomainEventPublisher);
		} else {
			log.info("Payment initiation is failed for order id: {}", payment.getOrderId().getValue());
			payment.updateStatus(PaymentStatus.FAILED);
			return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)),
					failureMessages, paymentFailedDomainEventPublisher);
		}
	}

	private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
		if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
			log.error("Customer with id: {} doesn't have enough credit for payment!",
					payment.getCustomerId().getValue());
			failureMessages.add("Customer with id=" + payment.getCustomerId().getValue()
					+ " doesn't have enough credit for payment!");
		}
	}

	private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
		creditEntry.subtractCreditAmount(payment.getPrice());
	}

	private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistories,
			TransactionType transactionType) {
		creditHistories.add(new CreditHistory(new CreditHistoryId(UUID.randomUUID()), payment.getCustomerId(),
				payment.getPrice(), transactionType));

	}

	private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistories,
			List<String> failureMessages) {
		Money creditTransactionAmount = findTransactionPrice(creditHistories, TransactionType.CREDIT);
		Money debitTransactionAmount = findTransactionPrice(creditHistories, TransactionType.DEBIT);

		if (debitTransactionAmount.isGreaterThan(creditTransactionAmount)) {
			log.error("Customer with id: {} doesn't have enough credit according to credit history",
					creditEntry.getCustomerId().getValue());
			failureMessages.add("Customer with id=" + creditEntry.getCustomerId().getValue()
					+ " doesn't have enough credit according to credit history!");
		}

		if (!creditEntry.getTotalCreditAmount().equals(creditTransactionAmount.substract(debitTransactionAmount))) {
			log.error("Credit history total is not equal to current credit for customer id: {}!",
					creditEntry.getCustomerId().getValue());
			failureMessages.add("Credit history total is not equal to current credit for customer id: "
					+ creditEntry.getCustomerId().getValue() + "!");
		}
	}

	private Money findTransactionPrice(List<CreditHistory> creditHistories, TransactionType transactionType) {
		return creditHistories.stream().filter(creditHistory -> transactionType == creditHistory.getTransactionType())
				.map(CreditHistory::getAmount).reduce(Money.ZERO_MONEY, Money::add);
	}

	@Override
	public PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
			List<CreditHistory> creditHistories, List<String> failureMessages,
			DomainEventPublisher<PaymentCancelledEvent> paymentCancelledDomainEventPublisher,
			DomainEventPublisher<PaymentFailedEvent> paymentFailedDomainEventPublisher) {
		payment.validatePayment(failureMessages);
		addCreditEntry(payment, creditEntry);
		updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);

		if (failureMessages.isEmpty()) {
			log.info("Payment is cancelled for order id: {}", payment.getOrderId().getValue());
			payment.updateStatus(PaymentStatus.CANCELLED);
			return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)),paymentCancelledDomainEventPublisher);
		} else {
			log.info("Payment cancellation is failed for order id: {}", payment.getOrderId().getValue());
			payment.updateStatus(PaymentStatus.FAILED);
			return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)),
					failureMessages,paymentFailedDomainEventPublisher);
		}
	}

	private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
		creditEntry.addCreditAmount(payment.getPrice());
	}

}
