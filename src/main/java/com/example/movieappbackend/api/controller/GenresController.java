package com.example.movieappbackend.api.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@CrossOrigin
@RestController
@RequestMapping("/genres")
public class GenresController {
	
	private final RestTemplate restTemplate;
	
	public GenresController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@GetMapping
	public Object listGenres() {
		
		String url = String.format("%s/genres", XRapidAPIUtils.getBaseURL());
		HttpHeaders headers = XRapidAPIUtils.getBasicHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<?> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		return response.getBody();
	}
}
