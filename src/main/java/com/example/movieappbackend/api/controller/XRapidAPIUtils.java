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
		headers.set("X-RapidAPI-Key", "2d51fa8debmsheed8f46be152fc2p18fdb9jsn7577ff73281f");
		headers.set("X-RapidAPI-Host", "streaming-availability.p.rapidapi.com");
		return headers;
	}
}
