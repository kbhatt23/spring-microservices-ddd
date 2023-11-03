package com.learning.orderapplicationservice.response;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

import com.learning.commondomain.valueobjects.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderResponse {

	@NotNull
	private UUID trackingId;

	@NotNull
	private OrderStatus orderStatus;

	private String message;
}
