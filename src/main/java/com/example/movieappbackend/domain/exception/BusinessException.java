package com.example.movieappbackend.domain.exception;

public class BusinessException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public BusinessException() {
		super("Business error!");
	}
	
	public BusinessException(String message) {
		super(message);
	}
}
