package com.learning.customerapplicationservice.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
@AllArgsConstructor
public class CreateCustomerCommand {

	@NotNull
	private final String username;

	@NotNull
	private final String firstName;

	@NotNull
	private final String lastName;

}
