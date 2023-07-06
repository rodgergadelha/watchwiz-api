package com.example.movieappbackend.api.controller;

import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class XRapidAPIUtils {
	
	protected static String getBaseURL() {
		return "https://streaming-availability.p.rapidapi.com/v2";
	}
	
	protected static HttpHeaders getBasicHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("X-RapidAPI-Key", "db9e1c8595mshd6da4d78cf7db36p1413e4jsn7943972c7201");
		headers.set("X-RapidAPI-Host", "streaming-availability.p.rapidapi.com");
		return headers;
	}
}
