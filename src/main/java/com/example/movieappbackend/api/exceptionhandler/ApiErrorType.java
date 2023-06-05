package com.example.movieappbackend.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ApiErrorType {
	
	INCOMPREHENSIBLE_MESSAGE("/incomprehensible-message", "Incomprehensible message"),
    RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
    ENTITY_IN_USE("/entity-in-use", "Entity in use"),
    BUSINESS_ERROR("/business-error", "Business error"),
    INVALID_PARAMETER("/invalid-parameter", "Invalid parameter"),
    SYSTEM_ERROR("/system-error", "System error"),
    INVALID_DATA("/invalid-data", "Invalid data");
	
	private String title;
	private String uri;
	
	ApiErrorType(String path, String title) {
		this.uri = "https://localhost:8080" + path;
        this.title = title;
	}
}
