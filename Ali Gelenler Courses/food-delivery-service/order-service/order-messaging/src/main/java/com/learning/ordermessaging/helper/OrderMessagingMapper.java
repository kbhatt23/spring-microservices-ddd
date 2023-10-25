package com.learning.ordermessaging.helper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.kafka.common.Uuid;
import org.springframework.stereotype.Component;

import com.learning.kafkamodel.order.PaymentOrderStatus;
import com.learning.kafkamodel.order.PaymentRequestAvroModel;
import com.learning.kafkamodel.order.PaymentResponseAvroModel;
import com.learning.kafkamodel.order.RestaurantApprovalRequestAvroModel;
import com.learning.kafkamodel.order.RestaurantApprovalResponseAvroModel;
import com.learning.kafkamodel.order.RestaurantOrderStatus;
import com.learning.orderapplicationservice.external.PaymentResponse;
import com.learning.orderapplicationservice.external.RestrauntApprovalResponse;
import com.learning.orderservice.core.entities.Order;
import com.learning.orderservice.core.events.OrderCancelledEvent;
import com.learning.orderservice.core.events.OrderCreatedEvent;
import com.learning.orderservice.core.events.OrderEvent;
import com.learning.orderservice.core.events.OrderPaidEvent;

@Component
public class OrderMessagingMapper {

	public PaymentRequestAvroModel createPaymentModelFromEvent(OrderCreatedEvent orderCreatedEvent) {
		return createPaymentModel(orderCreatedEvent, orderCreatedEvent.getOrder(), PaymentOrderStatus.PENDING);
	}

	public PaymentRequestAvroModel cancelPaymentModelFromEvent(OrderCancelledEvent orderCancelledEvent) {
		return createPaymentModel(orderCancelledEvent, orderCancelledEvent.getOrder(), PaymentOrderStatus.CANCELLED);
	}

	private PaymentRequestAvroModel createPaymentModel(OrderEvent orderEvent, Order order,
			PaymentOrderStatus paymentOrderStatus) {
		return PaymentRequestAvroModel.newBuilder().setId(Uuid.randomUuid().toString()).setSagaId("")
				.setCustomerId(order.getCustomerId().getValue().toString())
				.setOrderId(order.getId().getValue().toString()).setPrice(order.getPrice().getAmount())
				.setCreatedAt(orderEvent.getCreatedAt().toInstant()).setPaymentOrderStatus(paymentOrderStatus).build();
	}

	public RestaurantApprovalRequestAvroModel orderApprovalEventToRestaurantApprovalRequestAvroModel(
			OrderPaidEvent orderPaidEvent) {

		Order order = orderPaidEvent.getOrder();
		List<com.learning.kafkamodel.order.Product> products = order.getItems().stream()
				.map(orderItem -> com.learning.kafkamodel.order.Product.newBuilder()
						.setId(orderItem.getProduct().getId().getValue().toString())
						.setQuantity(orderItem.getQuantity()).build())
				.collect(Collectors.toList());
		return RestaurantApprovalRequestAvroModel.newBuilder().setId(UUID.randomUUID().toString()).setSagaId("")
				.setOrderId(order.getId().getValue().toString())
				.setRestaurantId(order.getRestrauntId().getValue().toString())
				.setRestaurantOrderStatus(RestaurantOrderStatus.PAID).setProducts(products)
				.setPrice(order.getPrice().getAmount()).setCreatedAt(orderPaidEvent.getCreatedAt().toInstant()).build();
	}

	public PaymentResponse paymentResponseAvroModelToPaymentResponse(
			PaymentResponseAvroModel paymentResponseAvroModel) {
		return PaymentResponse.builder().id(paymentResponseAvroModel.getId())
				.sagaId(paymentResponseAvroModel.getSagaId()).paymentId(paymentResponseAvroModel.getPaymentId())
				.customerId(paymentResponseAvroModel.getCustomerId()).orderId(paymentResponseAvroModel.getOrderId())
				.price(paymentResponseAvroModel.getPrice()).createdAt(paymentResponseAvroModel.getCreatedAt())
				.paymentStatus(com.learning.commondomain.valueobjects.PaymentStatus
						.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
				.errors(paymentResponseAvroModel.getFailureMessages()).build();
	}

	public RestrauntApprovalResponse approvalResponseAvroModelToApprovalResponse(
			RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {
		return RestrauntApprovalResponse.builder().id(restaurantApprovalResponseAvroModel.getId())
				.sagaId(restaurantApprovalResponseAvroModel.getSagaId())
				.restaurantId(restaurantApprovalResponseAvroModel.getRestaurantId())
				.orderId(restaurantApprovalResponseAvroModel.getOrderId())
				.createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
				.orderApprovalStatus(com.learning.commondomain.valueobjects.OrderApprovalStatus
						.valueOf(restaurantApprovalResponseAvroModel.getOrderApprovalStatus().name()))
				.failureMessages(restaurantApprovalResponseAvroModel.getFailureMessages()).build();
	}

}
