package com.learning.orderapplicationservice.ports.output;

import java.util.Optional;

import com.learning.commondomain.valueobjects.OrderId;
import com.learning.orderservice.core.entities.Order;
import com.learning.orderservice.core.valueobjects.TrackingId;

public interface OrderRepository {

	Order save(Order order);
	
	Optional<Order> findByTrackingId(TrackingId trackingId);
	
	Optional<Order> findById(OrderId orderId);
}
