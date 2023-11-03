package com.learning.orderapplicationservice.commands;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderCommand {

	// not a value object like we have used in core domain layer
	@NotNull
	private final UUID customerId;

	@NotNull
	private final UUID restrauntId;

	@NotNull
	private final BigDecimal price;

	@NotNull
	@Valid
	private final OrderAddress orderAddress;

	@NotNull
	@Valid
	private final List<OrderItemCommand> orderItems;

}
