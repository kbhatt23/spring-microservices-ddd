package com.learning.customerapplicationservice.dto;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateCustomerResponse {

	@NotNull
	private final UUID customerId;

	@NotNull
	private final String message;
}
