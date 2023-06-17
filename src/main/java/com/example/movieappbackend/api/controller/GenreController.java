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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.movieappbackend.domain.model.Genre;
import com.example.movieappbackend.domain.service.GenreService;
import com.nimbusds.jose.util.JSONObjectUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {
	
	private final RestTemplate restTemplate;
	
	private final GenreService service;
	
	@GetMapping("/movie-night")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public Map<String, String> listGenresFromMovieNightAPI() throws ParseException {
		
		String url = String.format("%s/genres", XRapidAPIUtils.getBaseURL());
		HttpHeaders headers = XRapidAPIUtils.getBasicHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<?> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		Map<String, Object> responseBody = JSONObjectUtils.parse((String) response.getBody());
		return (Map<String, String>) responseBody.get("result");
	}
	
	@GetMapping
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public List<Genre> listGenresFromWatchWizAPI() {
		return service.findAll();
	}
	
	@PostMapping
	@ApiImplicitParams({
		@ApiImplicitParam(name = "name", paramType = "query", dataType = "string",
				   required = true, value = "Name of the genre"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public Genre save(@RequestParam("name") String name) {
		return service.save(name);
	}
}
