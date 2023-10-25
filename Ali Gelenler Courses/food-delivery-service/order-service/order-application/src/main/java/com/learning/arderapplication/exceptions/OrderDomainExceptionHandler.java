package com.learning.arderapplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.learning.commonapplication.error.ErrorDTO;
import com.learning.commonapplication.error.GenericExceptionHandler;
import com.learning.orderservice.core.exceptions.OrderDomainException;
import com.learning.orderservice.core.exceptions.OrderNotFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class OrderDomainExceptionHandler extends GenericExceptionHandler {

	@ResponseBody
	@ExceptionHandler(OrderNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorDTO handle(OrderNotFoundException orderNotFoundException) {
		log.error("handle: ordernotfoundexception occurred error: {}, message: {}", orderNotFoundException,
				orderNotFoundException.getMessage());

		return new ErrorDTO(HttpStatus.NOT_FOUND.getReasonPhrase(), orderNotFoundException.getMessage());
	}

	@ResponseBody
	@ExceptionHandler(OrderDomainException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDTO handle(OrderDomainException orderDomainException) {
		log.error("handle: OrderDomainException occurred error: {}, message: {}", orderDomainException,
				orderDomainException.getMessage());

		return new ErrorDTO(HttpStatus.BAD_REQUEST.getReasonPhrase(), orderDomainException.getMessage());
	}
}
