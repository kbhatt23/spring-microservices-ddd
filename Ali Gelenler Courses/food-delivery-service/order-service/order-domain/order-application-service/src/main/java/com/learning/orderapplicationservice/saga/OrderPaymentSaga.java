package com.learning.orderapplicationservice.saga;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learning.commondomain.constants.CommonDomainConstants;
import com.learning.commondomain.valueobjects.OrderStatus;
import com.learning.commondomain.valueobjects.PaymentStatus;
import com.learning.orderapplicationservice.external.PaymentResponse;
import com.learning.orderapplicationservice.mapper.OrderMapper;
import com.learning.orderapplicationservice.oubox.model.OrderApprovalOutboxMessage;
import com.learning.orderapplicationservice.oubox.model.OrderPaymentOutboxMessage;
import com.learning.orderapplicationservice.oubox.scheduler.ApprovalOutboxHelper;
import com.learning.orderapplicationservice.oubox.scheduler.PaymentOutboxHelper;
import com.learning.orderapplicationservice.ports.input.helper.OrderSagaHelper;
import com.learning.orderservice.core.entities.Order;
import com.learning.orderservice.core.events.OrderPaidEvent;
import com.learning.orderservice.core.exceptions.OrderDomainException;
import com.learning.orderservice.core.services.OrderDomainService;
import com.learning.outbox.OutboxStatus;
import com.learning.saga.SagaStatus;
import com.learning.saga.SagaStep;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderPaymentSaga implements SagaStep<PaymentResponse> {

	private final OrderDomainService orderDomainService;

	private final OrderSagaHelper orderSagaHelper;

	private final PaymentOutboxHelper paymentOutboxHelper;

	private final ApprovalOutboxHelper approvalOutboxHelper;

	private final OrderMapper orderMapper;

	public OrderPaymentSaga(OrderDomainService orderDomainService, OrderSagaHelper orderSagaHelper,
			ApprovalOutboxHelper approvalOutboxHelper, OrderMapper orderMapper,
			PaymentOutboxHelper paymentOutboxHelper) {
		this.orderDomainService = orderDomainService;
		this.orderSagaHelper = orderSagaHelper;
		this.paymentOutboxHelper = paymentOutboxHelper;
		this.approvalOutboxHelper = approvalOutboxHelper;
		this.orderMapper = orderMapper;
	}

	@Override
	@Transactional
	public void process(PaymentResponse paymentResponse) {
		UUID sagaId = UUID.fromString(paymentResponse.getSagaId());
		Optional<OrderPaymentOutboxMessage> paymentOutboxMessageBySagaIdAndSagaStatus = paymentOutboxHelper
				.getPaymentOutboxMessageBySagaIdAndSagaStatus(sagaId, SagaStatus.STARTED);

		if (paymentOutboxMessageBySagaIdAndSagaStatus.isEmpty()) {
			log.info("An outbox message with saga id: {} is already processed!", paymentResponse.getSagaId());
			return;
		}

		OrderPaymentOutboxMessage orderPaymentOutboxMessage = paymentOutboxMessageBySagaIdAndSagaStatus.get();

		OrderPaidEvent domainEvent = completePaymentForOrder(paymentResponse);

		SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(domainEvent.getOrder().getOrderStatus());

		paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage,
				domainEvent.getOrder().getOrderStatus(), sagaStatus));

		approvalOutboxHelper.saveApprovalOutboxMessage(
				orderMapper.orderPaidEventToOrderApprovalEventPayload(domainEvent),
				domainEvent.getOrder().getOrderStatus(), sagaStatus, OutboxStatus.STARTED, sagaId);

		log.info("Order with id: {} is paid", domainEvent.getOrder().getId().getValue());
	}

	private OrderPaidEvent completePaymentForOrder(PaymentResponse paymentResponse) {
		log.info("Completing payment for order with id: {}", paymentResponse.getOrderId());
		Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
		OrderPaidEvent domainEvent = orderDomainService.payOrder(order);
		orderSagaHelper.saveOrder(order);
		return domainEvent;
	}

	private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(
			OrderPaymentOutboxMessage orderPaymentOutboxMessage, OrderStatus orderStatus, SagaStatus sagaStatus) {
		orderPaymentOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)));
		orderPaymentOutboxMessage.setOrderStatus(orderStatus);
		orderPaymentOutboxMessage.setSagaStatus(sagaStatus);
		return orderPaymentOutboxMessage;
	}

	@Override
	@Transactional
	public void rollback(PaymentResponse paymentResponse) {
		Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessageResponse = paymentOutboxHelper
				.getPaymentOutboxMessageBySagaIdAndSagaStatus(UUID.fromString(paymentResponse.getSagaId()),
						getCurrentSagaStatus(paymentResponse.getPaymentStatus()));

		if (orderPaymentOutboxMessageResponse.isEmpty()) {
			log.info("An outbox message with saga id: {} is already roll backed!", paymentResponse.getSagaId());
			return;
		}

		OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessageResponse.get();

		Order order = rollbackPaymentForOrder(paymentResponse);

		SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(order.getOrderStatus());

		paymentOutboxHelper
				.save(getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage, order.getOrderStatus(), sagaStatus));

		if (paymentResponse.getPaymentStatus() == PaymentStatus.CANCELLED) {
			approvalOutboxHelper.save(
					getUpdatedApprovalOutboxMessage(paymentResponse.getSagaId(), order.getOrderStatus(), sagaStatus));
		}

		log.info("Order with id: {} is cancelled", order.getId().getValue());
	}

	// TODO: If using jdk 17 can use new arrow based switch expression
	private SagaStatus[] getCurrentSagaStatus(PaymentStatus paymentStatus) {
		SagaStatus[] sagaStatuses = new SagaStatus[] {};

		switch (paymentStatus) {
		case COMPLETED:
			sagaStatuses = new SagaStatus[] { SagaStatus.STARTED };
			break;
		case CANCELLED:
			sagaStatuses = new SagaStatus[] { SagaStatus.PROCESSING };
			break;
		case FAILED:
			sagaStatuses = new SagaStatus[] { SagaStatus.STARTED, SagaStatus.PROCESSING };
			break;

		default:
			break;
		}
		return sagaStatuses;
	}

	private Order rollbackPaymentForOrder(PaymentResponse paymentResponse) {
		log.info("Cancelling order with id: {}", paymentResponse.getOrderId());
		Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
		orderDomainService.cancelOrder(order, paymentResponse.getErrors());
		orderSagaHelper.saveOrder(order);
		return order;
	}

	private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(String sagaId, OrderStatus orderStatus,
			SagaStatus sagaStatus) {
		Optional<OrderApprovalOutboxMessage> orderApprovalOutboxMessageResponse = approvalOutboxHelper
				.getApprovalOutboxMessageBySagaIdAndSagaStatus(UUID.fromString(sagaId), SagaStatus.COMPENSATING);
		if (orderApprovalOutboxMessageResponse.isEmpty()) {
			throw new OrderDomainException(
					"Approval outbox message could not be found in " + SagaStatus.COMPENSATING.name() + " status!");
		}
		OrderApprovalOutboxMessage orderApprovalOutboxMessage = orderApprovalOutboxMessageResponse.get();
		orderApprovalOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)));
		orderApprovalOutboxMessage.setOrderStatus(orderStatus);
		orderApprovalOutboxMessage.setSagaStatus(sagaStatus);
		return orderApprovalOutboxMessage;
	}
}
