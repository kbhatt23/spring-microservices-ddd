package com.learning.orderapplicationservice.mapper;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.PaymentOrderStatus;
import com.learning.commondomain.valueobjects.ProductId;
import com.learning.commondomain.valueobjects.RestaurantOrderStatus;
import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.orderapplicationservice.commands.CreateOrderCommand;
import com.learning.orderapplicationservice.commands.OrderAddress;
import com.learning.orderapplicationservice.commands.OrderItemCommand;
import com.learning.orderapplicationservice.oubox.model.OrderApprovalEventPayload;
import com.learning.orderapplicationservice.oubox.model.OrderApprovalEventProduct;
import com.learning.orderapplicationservice.oubox.model.OrderPaymentEventPayload;
import com.learning.orderapplicationservice.response.CreateOrderResponse;
import com.learning.orderapplicationservice.response.TrackOrderResponse;
import com.learning.orderservice.core.entities.Order;
import com.learning.orderservice.core.entities.OrderItem;
import com.learning.orderservice.core.entities.Product;
import com.learning.orderservice.core.entities.Restraunt;
import com.learning.orderservice.core.events.OrderCancelledEvent;
import com.learning.orderservice.core.events.OrderCreatedEvent;
import com.learning.orderservice.core.events.OrderPaidEvent;
import com.learning.orderservice.core.valueobjects.StreetAddress;

@Component
public class OrderMapper {

	public Restraunt getRestrauntFromCreateOrderCommand(CreateOrderCommand createOrderCommand) {
		return new Restraunt(new RestrauntId(createOrderCommand.getRestrauntId()),
				createOrderCommand.getOrderItems().stream().map(item -> new Product(new ProductId(item.getProductId())))
						.collect(Collectors.toMap(Product::getId, Function.identity())),
				false);
	}

	public Order createOrderFromOrderCommand(CreateOrderCommand createOrderCommand) {

		return new Order(null, new CustomerId(createOrderCommand.getCustomerId()),
				new RestrauntId(createOrderCommand.getRestrauntId()),
				createStreetAddressFromOrderAddress(createOrderCommand.getOrderAddress()),
				new Money(createOrderCommand.getPrice()),
				createOrderItemFromCommandItem(createOrderCommand.getOrderItems()));
	}

	private List<OrderItem> createOrderItemFromCommandItem(List<OrderItemCommand> orderItems) {
		return orderItems.stream()
				.map(commandItem -> new OrderItem(null, new Product(new ProductId(commandItem.getProductId())),
						commandItem.getQuantity(), new Money(commandItem.getUnitPrice()),
						new Money(commandItem.getTotalPrice())))
				.collect(Collectors.toList());
	}

	private StreetAddress createStreetAddressFromOrderAddress(OrderAddress orderAddress) {
		return new StreetAddress(UUID.randomUUID(), orderAddress.getStreet(), orderAddress.getPostalCode(),
				orderAddress.getCity());

	}

	public CreateOrderResponse createOrderResponse(Order order, String message) {
		return CreateOrderResponse.builder().trackingId(order.getTrackingId().getValue())
				.orderStatus(order.getOrderStatus()).message(message).build();
	}

	public TrackOrderResponse createTrackOrderResponseFromOrder(Order order) {
		return TrackOrderResponse.builder().orderTrackingId(order.getTrackingId().getValue())
				.orderStatus(order.getOrderStatus()).errors(order.getErrors()).build();
	}

	public OrderPaymentEventPayload orderCreatedEventToOrderPaymentEventPayload(OrderCreatedEvent orderCreatedEvent) {
		return OrderPaymentEventPayload.builder()
				.customerId(orderCreatedEvent.getOrder().getCustomerId().getValue().toString())
				.orderId(orderCreatedEvent.getOrder().getId().getValue().toString())
				.price(orderCreatedEvent.getOrder().getPrice().getAmount()).createdAt(orderCreatedEvent.getCreatedAt())
				.paymentOrderStatus(PaymentOrderStatus.PENDING.name()).build();
	}

	public OrderApprovalEventPayload orderPaidEventToOrderApprovalEventPayload(OrderPaidEvent orderPaidEvent) {
		return OrderApprovalEventPayload.builder().orderId(orderPaidEvent.getOrder().getId().getValue().toString())
				.restaurantId(orderPaidEvent.getOrder().getRestrauntId().getValue().toString())
				.restaurantOrderStatus(RestaurantOrderStatus.PAID.name())
				.products(orderPaidEvent.getOrder().getItems().stream()
						.map(orderItem -> OrderApprovalEventProduct.builder()
								.id(orderItem.getProduct().getId().getValue().toString())
								.quantity(orderItem.getQuantity()).build())
						.collect(Collectors.toList()))
				.price(orderPaidEvent.getOrder().getPrice().getAmount()).createdAt(orderPaidEvent.getCreatedAt())
				.build();
	}

	public OrderPaymentEventPayload orderCancelledEventToOrderPaymentEventPayload(
			OrderCancelledEvent orderCancelledEvent) {
		return OrderPaymentEventPayload.builder()
				.customerId(orderCancelledEvent.getOrder().getCustomerId().getValue().toString())
				.orderId(orderCancelledEvent.getOrder().getId().getValue().toString())
				.price(orderCancelledEvent.getOrder().getPrice().getAmount())
				.createdAt(orderCancelledEvent.getCreatedAt()).paymentOrderStatus(PaymentOrderStatus.CANCELLED.name())
				.build();
	}
}
