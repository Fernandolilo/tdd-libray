package com.systempro.library.exceptions;

import org.springframework.validation.BindingResult;

public class BusinessException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public BusinessException(String message) {
		super(message);
	}

}
