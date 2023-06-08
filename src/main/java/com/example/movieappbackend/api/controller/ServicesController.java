package com.example.movieappbackend.api.controller;

import java.text.ParseException;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.jose.util.JSONObjectUtils;

@CrossOrigin
@RestController
@RequestMapping("/services")
public class ServicesController {
	
	private final RestTemplate restTemplate;
	
	public ServicesController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping
	public Map<String, Object> listServices() throws ParseException  {
		
		String url = String.format("%s/services", XRapidAPIUtils.getBaseURL());
		HttpHeaders headers = XRapidAPIUtils.getBasicHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<?> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		Map<String, Object> responseBody = JSONObjectUtils.parse((String) response.getBody());
		return (Map<String, Object>) responseBody.get("result");
	}
	
	
}
