package com.learning.customerapplication.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.learning.commonapplication.error.ErrorDTO;
import com.learning.commonapplication.error.GenericExceptionHandler;
import com.learning.customerdomaincore.exceptions.CustomerDomainException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CustomerGlobalExceptionHandler extends GenericExceptionHandler {

	@ResponseBody
	@ExceptionHandler(value = { CustomerDomainException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDTO handleException(CustomerDomainException exception) {
		log.error(exception.getMessage(), exception);
		return new ErrorDTO(HttpStatus.BAD_REQUEST.getReasonPhrase(), exception.getMessage());
	}

}