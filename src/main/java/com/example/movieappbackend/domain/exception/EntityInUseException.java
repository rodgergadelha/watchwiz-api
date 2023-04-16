package com.example.movieappbackend.domain.exception;

public class EntityInUseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityInUseException() {
		super("Entity is referenced by other!");
	}
	
	public EntityInUseException(String message) {
		super(message);
	}
}
