package com.learning.orderapplicationservice.saga;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learning.commondomain.constants.CommonDomainConstants;
import com.learning.commondomain.valueobjects.OrderStatus;
import com.learning.orderapplicationservice.external.RestrauntApprovalResponse;
import com.learning.orderapplicationservice.mapper.OrderMapper;
import com.learning.orderapplicationservice.oubox.model.OrderApprovalOutboxMessage;
import com.learning.orderapplicationservice.oubox.model.OrderPaymentOutboxMessage;
import com.learning.orderapplicationservice.oubox.scheduler.ApprovalOutboxHelper;
import com.learning.orderapplicationservice.oubox.scheduler.PaymentOutboxHelper;
import com.learning.orderapplicationservice.ports.input.helper.OrderSagaHelper;
import com.learning.orderservice.core.entities.Order;
import com.learning.orderservice.core.events.OrderCancelledEvent;
import com.learning.orderservice.core.exceptions.OrderDomainException;
import com.learning.orderservice.core.services.OrderDomainService;
import com.learning.outbox.OutboxStatus;
import com.learning.saga.SagaStatus;
import com.learning.saga.SagaStep;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderApprovalSaga implements SagaStep<RestrauntApprovalResponse> {

	private final OrderSagaHelper orderSagaHelper;

	private final OrderDomainService orderDomainService;

	private final OrderMapper orderMapper;

	private final PaymentOutboxHelper paymentOutboxHelper;

	private final ApprovalOutboxHelper approvalOutboxHelper;

	public OrderApprovalSaga(OrderSagaHelper orderSagaHelper, OrderDomainService orderDomainService,
			OrderMapper orderMapper, PaymentOutboxHelper paymentOutboxHelper,
			ApprovalOutboxHelper approvalOutboxHelper) {
		this.orderSagaHelper = orderSagaHelper;
		this.orderDomainService = orderDomainService;
		this.orderMapper = orderMapper;
		this.paymentOutboxHelper = paymentOutboxHelper;
		this.approvalOutboxHelper = approvalOutboxHelper;
	}

	@Override
	@Transactional
	public void process(RestrauntApprovalResponse restrauntApprovalResponse) {
		Optional<OrderApprovalOutboxMessage> orderApprovalOutboxMessageResponse = approvalOutboxHelper
				.getApprovalOutboxMessageBySagaIdAndSagaStatus(UUID.fromString(restrauntApprovalResponse.getSagaId()),
						SagaStatus.PROCESSING);

		if (orderApprovalOutboxMessageResponse.isEmpty()) {
			log.info("An outbox message with saga id: {} is already processed!", restrauntApprovalResponse.getSagaId());
			return;
		}
		OrderApprovalOutboxMessage orderApprovalOutboxMessage = orderApprovalOutboxMessageResponse.get();

		Order order = approveOrder(restrauntApprovalResponse);

		SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(order.getOrderStatus());

		approvalOutboxHelper
				.save(getUpdatedApprovalOutboxMessage(orderApprovalOutboxMessage, order.getOrderStatus(), sagaStatus));

		paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(restrauntApprovalResponse.getSagaId(),
				order.getOrderStatus(), sagaStatus));

		log.info("Order with id: {} is approved", order.getId().getValue());

		log.info("Order with id: {} is approved", order.getId().getValue());

	}

	private Order approveOrder(RestrauntApprovalResponse restaurantApprovalResponse) {
		log.info("Approving order with id: {}", restaurantApprovalResponse.getOrderId());

		Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
		orderDomainService.approveOrder(order);
		orderSagaHelper.saveOrder(order);
		return order;
	}

	private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(
			OrderApprovalOutboxMessage orderApprovalOutboxMessage, OrderStatus orderStatus, SagaStatus sagaStatus) {
		orderApprovalOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)));
		orderApprovalOutboxMessage.setOrderStatus(orderStatus);
		orderApprovalOutboxMessage.setSagaStatus(sagaStatus);
		return orderApprovalOutboxMessage;
	}

	private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(String sagaId, OrderStatus orderStatus,
			SagaStatus sagaStatus) {
		Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessageResponse = paymentOutboxHelper
				.getPaymentOutboxMessageBySagaIdAndSagaStatus(UUID.fromString(sagaId), SagaStatus.PROCESSING);
		if (orderPaymentOutboxMessageResponse.isEmpty()) {
			throw new OrderDomainException(
					"Payment outbox message cannot be found in " + SagaStatus.PROCESSING.name() + " state");
		}
		OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessageResponse.get();
		orderPaymentOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)));
		orderPaymentOutboxMessage.setOrderStatus(orderStatus);
		orderPaymentOutboxMessage.setSagaStatus(sagaStatus);
		return orderPaymentOutboxMessage;
	}

	@Override
	@Transactional
	public void rollback(RestrauntApprovalResponse restaurantApprovalResponse) {
		Optional<OrderApprovalOutboxMessage> orderApprovalOutboxMessageResponse = approvalOutboxHelper
				.getApprovalOutboxMessageBySagaIdAndSagaStatus(UUID.fromString(restaurantApprovalResponse.getSagaId()),
						SagaStatus.PROCESSING);

		if (orderApprovalOutboxMessageResponse.isEmpty()) {
			log.info("An outbox message with saga id: {} is already roll backed!",
					restaurantApprovalResponse.getSagaId());
			return;
		}

		OrderApprovalOutboxMessage orderApprovalOutboxMessage = orderApprovalOutboxMessageResponse.get();

		OrderCancelledEvent domainEvent = rollbackOrder(restaurantApprovalResponse);

		SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(domainEvent.getOrder().getOrderStatus());

		approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(orderApprovalOutboxMessage,
				domainEvent.getOrder().getOrderStatus(), sagaStatus));

		paymentOutboxHelper.savePaymentOutboxMessage(
				orderMapper.orderCancelledEventToOrderPaymentEventPayload(domainEvent),
				domainEvent.getOrder().getOrderStatus(), sagaStatus, OutboxStatus.STARTED,
				UUID.fromString(restaurantApprovalResponse.getSagaId()));

		log.info("Order with id: {} is cancelling", domainEvent.getOrder().getId().getValue());

	}

	private OrderCancelledEvent rollbackOrder(RestrauntApprovalResponse restaurantApprovalResponse) {
        log.info("Cancelling order with id: {}", restaurantApprovalResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        OrderCancelledEvent domainEvent = orderDomainService.cancelOrderPayment(order,
                restaurantApprovalResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        return domainEvent;
    }
}
