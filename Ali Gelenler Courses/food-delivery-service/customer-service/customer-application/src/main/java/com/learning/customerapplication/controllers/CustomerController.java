package com.learning.customerapplication.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.customerapplicationservice.dto.CreateCustomerCommand;
import com.learning.customerapplicationservice.dto.CreateCustomerResponse;
import com.learning.customerapplicationservice.ports.input.CustomerApplicationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CustomerController {

	private final CustomerApplicationService customerApplicationService;

	public CustomerController(CustomerApplicationService customerApplicationService) {
		this.customerApplicationService = customerApplicationService;
	}
	
	@PostMapping
	public ResponseEntity<CreateCustomerResponse> createCustomer(@RequestBody CreateCustomerCommand createCustomerCommand){
		log.info("Creating customer with username: {}", createCustomerCommand.getUsername());
		
		return ResponseEntity.created(null).body(customerApplicationService.createCustomer(createCustomerCommand));
	}
	
}
