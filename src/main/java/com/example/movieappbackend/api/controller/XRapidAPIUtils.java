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
		headers.set("X-RapidAPI-Key", "553dacfb9dmshdaa2584494d8cd6p1a6918jsn674950764974");
		headers.set("X-RapidAPI-Host", "streaming-availability.p.rapidapi.com");
		return headers;
	}
}
