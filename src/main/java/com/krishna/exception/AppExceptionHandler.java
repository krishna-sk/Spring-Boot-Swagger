package com.krishna.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorMessage> handleAnyException(Exception ex, WebRequest request) {

		String errorMessageDescription = ex.getLocalizedMessage();

		if (errorMessageDescription == null)
			errorMessageDescription = ex.toString();
		
		System.out.println(ex);
		return new ResponseEntity<>(new ErrorMessage(errorMessageDescription, new Date()),
				HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(value = { UserNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorMessage> handleUserNotFoundException(Exception ex, WebRequest request) {

		String errorMessageDescription = ex.getLocalizedMessage();

		if (errorMessageDescription == null)
			errorMessageDescription = ex.toString();
		

		return new ResponseEntity<>(new ErrorMessage(errorMessageDescription, new Date()),
				HttpStatus.NOT_FOUND);
	}
	
}
