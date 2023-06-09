package com.example.movieappbackend.api.controller;

import java.text.ParseException;
import java.util.List;
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

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@CrossOrigin
@RestController
@RequestMapping("/genres")
public class GenresController {
	
	private final RestTemplate restTemplate;
	
	public GenresController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@GetMapping
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public Map<String, String> listGenres() throws ParseException {
		
		String url = String.format("%s/genres", XRapidAPIUtils.getBaseURL());
		HttpHeaders headers = XRapidAPIUtils.getBasicHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<?> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		Map<String, Object> responseBody = JSONObjectUtils.parse((String) response.getBody());
		return (Map<String, String>) responseBody.get("result");
	}
}
