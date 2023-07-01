package com.example.movieappbackend.api.controller;

import java.text.ParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.movieappbackend.api.dtos.dto.MovieDto;
import com.example.movieappbackend.api.dtos.form.MovieListForm;
import com.example.movieappbackend.api.mapper.MovieMapper;
import com.example.movieappbackend.domain.model.MovieListItem;
import com.example.movieappbackend.domain.service.MovieListItemService;
import com.nimbusds.jose.util.JSONObjectUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/movies")
@AllArgsConstructor
public class MovieController {

	private final RestTemplate restTemplate;
	
	private final MovieMapper mapper;
	
	private final MovieListItemService service;
	
	@GetMapping("/get")
	@ApiImplicitParams({
		   @ApiImplicitParam(name = "country", paramType = "query", dataType = "String",
				   required = true, value = ApiDocumentationUtils.COUNTRY_PARAM_DESCRIPTION),
		   
		   @ApiImplicitParam(name = "imdb_id", paramType = "query", dataType = "String",
		   required = true),
		   
		   @ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public MovieDto findMovie(
			@RequestParam(value = "country") String country,
			@RequestParam(value = "imdb_id") String imdbId) throws ParseException {
		
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
		Map<String, Object> responseBody = JSONObjectUtils.parse((String) response.getBody());
		
		return mapper.dto(responseBody.get("result"));
	}
	
	@PostMapping("/list")
	@ApiImplicitParams({
		   @ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<List<MovieListItem>> saveAll(@Valid @RequestBody MovieListForm movieListForm) {
		List<MovieListItem> movies = service.saveAll(movieListForm.getMovies());
		return movies.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(movies);
	}
}
