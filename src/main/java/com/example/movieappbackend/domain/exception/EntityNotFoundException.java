package com.example.movieappbackend.domain.exception;

public class EntityNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException() {
		super("Entity cannot be found!");
	}
	
	public EntityNotFoundException(String message) {
		super(message);
	}
}
