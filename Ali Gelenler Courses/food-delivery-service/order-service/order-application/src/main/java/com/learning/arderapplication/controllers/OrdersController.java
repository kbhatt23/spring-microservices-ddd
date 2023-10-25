package com.learning.arderapplication.controllers;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.orderapplicationservice.commands.CreateOrderCommand;
import com.learning.orderapplicationservice.ports.input.OrderApplicationService;
import com.learning.orderapplicationservice.queries.TrackOrderQuery;
import com.learning.orderapplicationservice.response.CreateOrderResponse;
import com.learning.orderapplicationservice.response.TrackOrderResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class OrdersController {

	private final OrderApplicationService orderApplicationService;

	public OrdersController(OrderApplicationService orderApplicationService) {
		this.orderApplicationService = orderApplicationService;
	}

	@PostMapping
	public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {
		CreateOrderResponse createOrder = orderApplicationService.createOrder(createOrderCommand);

		log.info("Order created with tracking id: {}", createOrder.getTrackingId());

		return ResponseEntity.created(null).body(createOrder);
	}

	@GetMapping("/{trackingId}")
	public ResponseEntity<TrackOrderResponse> trackOrder(@PathVariable UUID trackingId) {
		TrackOrderQuery trackOrderQuery = TrackOrderQuery.builder().orderTrackingId(trackingId).build();

		TrackOrderResponse trackOrder = orderApplicationService.trackOrder(trackOrderQuery);

		log.info("Returning order status with tracking id: {}", trackOrder.getOrderTrackingId());

		return ResponseEntity.ok(trackOrder);
	}

}
