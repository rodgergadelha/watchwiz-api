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
@RequestMapping("/search")
public class SearchController {
	
	private final RestTemplate restTemplate;
	
	public SearchController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@GetMapping("/basic")
	public Object searchBasic(
			@RequestParam("country") String country,
			@RequestParam("services") String services,
			@RequestParam("show_type") String showType,
			@RequestParam("output_language") String outputLanguage,
			@RequestParam("genre") String genre,
			@RequestParam("show_original_language") String showOriginalLanguage,
			@RequestParam("keyword") String keyword) {
		
		String url = String.format("%s/search/basic", XRapidAPIUtils.getBaseURL());
		HttpHeaders headers = XRapidAPIUtils.getBasicHttpHeaders();
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("country", "{country}")
				.queryParam("services", "{services}")				
				.queryParam("show_type", "{show_type}")
				.queryParam("output_language", "{output_language}")
				.queryParam("genre", "{genre}")
				.queryParam("show_original_language", "{show_original_language}")
				.queryParam("keyword", "{keyword}")
				.encode().toUriString();
		
		Map<String, Object> params = new HashMap<>();
		params.put("country", country);
		params.put("services", services);
		params.put("show_type", showType);
		params.put("output_language", outputLanguage);
		params.put("genre", genre);
		params.put("show_original_language", showOriginalLanguage);
		params.put("keyword", keyword);
		
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
		ResponseEntity<?> response = this.restTemplate.exchange(urlTemplate, HttpMethod.GET,
				entity, String.class, params);
		
		return response.getBody();
	}
	
	@GetMapping("/title")
	public Object searchByTitle(
			@RequestParam("title") String title,
			@RequestParam("country") String country,
			@RequestParam("show_type") String showType,
			@RequestParam("output_language") String outputLanguage) {
		
		String url = String.format("%s/search/title", XRapidAPIUtils.getBaseURL());
		HttpHeaders headers = XRapidAPIUtils.getBasicHttpHeaders();
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("title", "{title}")
				.queryParam("country", "{country}")
				.queryParam("show_type", "{show_type}")
				.queryParam("output_language", "{output_language}")
				.encode().toUriString();
		
		Map<String, Object> params = new HashMap<>();
		params.put("title", title);
		params.put("country", country);
		params.put("show_type", showType);
		params.put("output_language", outputLanguage);
		
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
		ResponseEntity<?> response = this.restTemplate.exchange(urlTemplate, HttpMethod.GET,
				entity, String.class, params);
		
		return response.getBody();
	}
	
}
