package com.learning.orderservice.core.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.learning.commondomain.constants.CommonDomainConstants;
import com.learning.orderservice.core.entities.Order;
import com.learning.orderservice.core.entities.OrderItem;
import com.learning.orderservice.core.entities.Product;
import com.learning.orderservice.core.entities.Restraunt;
import com.learning.orderservice.core.events.OrderCancelledEvent;
import com.learning.orderservice.core.events.OrderCreatedEvent;
import com.learning.orderservice.core.events.OrderPaidEvent;
import com.learning.orderservice.core.exceptions.OrderDomainException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

	@Override
	public OrderCreatedEvent validateAndInitializeOrder(Order order, Restraunt restraunt) {
		validateRestraunt(restraunt);
		setOrderProductInformation(order, restraunt);

		order.validateOrder();
		order.initializeOrder();

		log.info("Order with id: {} is initiated", order.getId().getValue());
		return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)));
	}

	private void validateRestraunt(Restraunt restraunt) {
		if (!restraunt.isActive())
			throw new OrderDomainException(
					"Restaurant with id " + restraunt.getId().getValue() + " is currently not active!");
	}

	private void setOrderProductInformation(Order order, Restraunt restraunt) {
		order.getItems().stream().forEach(orderItem -> updateStockInOrderItemProduct(orderItem, restraunt));
	}

	private void updateStockInOrderItemProduct(OrderItem orderItem, Restraunt restraunt) {
		Product currentProduct = orderItem.getProduct();
		Product stockProduct = restraunt.getProductsMap().get(currentProduct.getId());
		currentProduct.updateProductInfo(stockProduct);
	}

	@Override
	public OrderPaidEvent payOrder(Order order) {
		order.pay();
		log.info("Order with id: {} is paid", order.getId().getValue());
		return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)));
	}

	@Override
	public void approveOrder(Order order) {
		order.approve();
		log.info("Order with id: {} is approved", order.getId().getValue());
	}

	@Override
	public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessage) {
		order.initCancelling(failureMessage);
		log.info("Order payment is cancelling for order id: {}", order.getId().getValue());
		return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)));
	}

	@Override
	public void cancelOrder(Order order, List<String> failureMessage) {
		order.cancel(failureMessage);
		log.info("Order with id: {} is cancelled", order.getId().getValue());
	}

}
