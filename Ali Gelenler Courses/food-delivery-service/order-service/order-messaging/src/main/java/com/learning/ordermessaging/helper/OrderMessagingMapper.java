package com.learning.ordermessaging.helper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.kafka.common.Uuid;
import org.springframework.stereotype.Component;

import com.learning.kafkamodel.order.CustomerAvroModel;
import com.learning.kafkamodel.order.PaymentOrderStatus;
import com.learning.kafkamodel.order.PaymentRequestAvroModel;
import com.learning.kafkamodel.order.PaymentResponseAvroModel;
import com.learning.kafkamodel.order.Product;
import com.learning.kafkamodel.order.RestaurantApprovalRequestAvroModel;
import com.learning.kafkamodel.order.RestaurantApprovalResponseAvroModel;
import com.learning.kafkamodel.order.RestaurantOrderStatus;
import com.learning.orderapplicationservice.external.PaymentResponse;
import com.learning.orderapplicationservice.external.RestrauntApprovalResponse;
import com.learning.orderapplicationservice.oubox.model.CustomerModel;
import com.learning.orderapplicationservice.oubox.model.OrderApprovalEventPayload;
import com.learning.orderapplicationservice.oubox.model.OrderPaymentEventPayload;
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

	public PaymentRequestAvroModel orderPaymentEventToPaymentRequestAvroModel(String sagaId,
			OrderPaymentEventPayload orderPaymentEventPayload) {
		return PaymentRequestAvroModel.newBuilder().setId(UUID.randomUUID().toString()).setSagaId(sagaId)
				.setCustomerId(orderPaymentEventPayload.getCustomerId())
				.setOrderId(orderPaymentEventPayload.getOrderId()).setPrice(orderPaymentEventPayload.getPrice())
				.setCreatedAt(orderPaymentEventPayload.getCreatedAt().toInstant())
				.setPaymentOrderStatus(PaymentOrderStatus.valueOf(orderPaymentEventPayload.getPaymentOrderStatus()))
				.build();
	}

	public RestaurantApprovalRequestAvroModel orderApprovalEventToRestaurantApprovalRequestAvroModel(String sagaId,
			OrderApprovalEventPayload orderApprovalEventPayload) {
		return RestaurantApprovalRequestAvroModel.newBuilder().setId(UUID.randomUUID().toString()).setSagaId(sagaId)
				.setOrderId(orderApprovalEventPayload.getOrderId())
				.setRestaurantId(orderApprovalEventPayload.getRestaurantId())
				.setRestaurantOrderStatus(
						RestaurantOrderStatus.valueOf(orderApprovalEventPayload.getRestaurantOrderStatus()))
				.setProducts(orderApprovalEventPayload.getProducts().stream()
						.map(orderApprovalEventProduct -> Product.newBuilder().setId(orderApprovalEventProduct.getId())
								.setQuantity(orderApprovalEventProduct.getQuantity()).build())
						.collect(Collectors.toList()))
				.setPrice(orderApprovalEventPayload.getPrice())
				.setCreatedAt(orderApprovalEventPayload.getCreatedAt().toInstant()).build();
	}

	public CustomerModel customerAvroModeltoCustomerModel(CustomerAvroModel customerAvroModel) {
		return CustomerModel.builder().id(customerAvroModel.getId()).userName(customerAvroModel.getUserName())
				.firstName(customerAvroModel.getFirstName()).lastName(customerAvroModel.getLastName()).build();
	}

}
