package com.systempro.library.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.systempro.library.exceptions.BusinessException;
import com.systempro.library.exceptions.apierror.ApiErrors;

@RestControllerAdvice
public class ApplicationControllerAdvice {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationsExceptions(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		return new ApiErrors(bindingResult);
	}

	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handlerBusinessExceptions(BusinessException ex) {
		// BindingResult bindingResult = ex.getBindingResult();
		return new ApiErrors(ex);
	}


	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity handleResponseStatusExceptions( ResponseStatusException ex) {
		return new ResponseEntity(new ApiErrors(ex), ex.getStatusCode());
	}
}
