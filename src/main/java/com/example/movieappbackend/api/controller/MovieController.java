package com.example.movieappbackend.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@CrossOrigin
@RestController
@RequestMapping("/movies")
public class MovieController {

private final RestTemplate restTemplate;
	
	public MovieController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@GetMapping("/get")
	public Object findMovie(
			@RequestParam("country") String country,
			@RequestParam("imdb_id") String imdbId) {
		
		String url = String.format("%s/get/basic", XRapidAPIUtils.getBaseURL());
		HttpHeaders headers = XRapidAPIUtils.getBasicHttpHeaders();
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("country", "{country}")
				.queryParam("imdb_id", "{imdb_id}")
				.encode().toUriString();
		
		Map<String, Object> params = new HashMap<>();
		params.put("country", country);
		params.put("imdb_id", imdbId);
		
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
		ResponseEntity<?> response = this.restTemplate.exchange(urlTemplate,
				HttpMethod.GET, entity, String.class, params);
		return response.getBody();
	}
}
