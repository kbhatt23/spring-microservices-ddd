package com.learning.commonapplication.error;

import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GenericExceptionHandler {

	@ResponseBody
	@ExceptionHandler(value = { Exception.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorDTO handle(Exception exception) {
		log.error("handle: generic exception occurred, {}, message {}", exception, exception.getMessage());
		return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage());
	}

	@ResponseBody
	@ExceptionHandler(value = { ValidationException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDTO handle(ValidationException validationException) {
		ErrorDTO errorDto;
		if(validationException instanceof ConstraintViolationException) {
			String violations = extractViolationsFromException(((ConstraintViolationException)validationException));
			log.error("handle: validation exception occurred: {}, exception: {}",violations, validationException);
			errorDto = new ErrorDTO(HttpStatus.BAD_REQUEST.getReasonPhrase(), violations);
		}else {
			String errorMessage = validationException.getMessage();
			log.error("handle: validation exception occurred: {}, exception: {}",validationException, errorMessage);
			errorDto = new ErrorDTO(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
		}
		
		return errorDto;
	}
	

    private String extractViolationsFromException(ConstraintViolationException constraintViolationException) {
        return constraintViolationException.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("--"));
    }

}
