package com.example.movieappbackend.api.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.example.movieappbackend.api.dtos.dto.MovieDto;
import com.example.movieappbackend.api.mapper.MovieMapper;
import com.nimbusds.jose.util.JSONObjectUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {
	
	private final RestTemplate restTemplate;
	
	private final MovieMapper mapper;
	
	@GetMapping("/basic")
	@ApiImplicitParams({
		   @ApiImplicitParam(name = "country", paramType = "query", dataType = "String",
				   required = true, value = ApiDocumentationUtils.COUNTRY_PARAM_DESCRIPTION),
		   
		   @ApiImplicitParam(name = "services", paramType = "query", dataType = "String", 
		   required = true, value = ApiDocumentationUtils.SERVICES_PARAM_DESCRIPTION),
		   
		   @ApiImplicitParam(name = "show_type", paramType = "query", dataType = "String",
				   value = ApiDocumentationUtils.SHOW_TYPE_PARAM_DESCRIPTION),
		   
		   @ApiImplicitParam(name = "output_language", paramType = "query", dataType = "String",
				   value = ApiDocumentationUtils.OUTPUT_LANGUAGE_PARAM_DESCRIPTION),
		   
		   @ApiImplicitParam(name = "genre", paramType = "query", dataType = "String",
				   value = ApiDocumentationUtils.GENRE_PARAM_DESCRIPTION),
		   
		   @ApiImplicitParam(name = "show_original_language", paramType = "query", dataType = "String",
				   value = ApiDocumentationUtils.SHOW_ORIGINAL_LANGUAGE_PARAM_DESCRIPTION),
		   
		   @ApiImplicitParam(name = "cursor", paramType = "query", dataType = "String",
		   value = ApiDocumentationUtils.CURSOR_PARAM_DESCRIPTION),
		   
		   @ApiImplicitParam(name = "keyword", paramType = "query", dataType = "String",
				   value = ApiDocumentationUtils.KEYWORD_LANGUAGE_PARAM_DESCRIPTION)
	})
	public List<MovieDto> searchBasic(
			@RequestParam("country") String country,
			@RequestParam("services") String services,
			@RequestParam(value = "show_type", required = false) String showType,
			@RequestParam(value = "output_language", required = false) String outputLanguage,
			@RequestParam(value = "genre", required = false) String genre,
			@RequestParam(value = "show_original_language", required = false) String showOriginalLanguage,
			@RequestParam(value = "cursor", required = false) String cursor,
			@RequestParam(value = "keyword", required = false) String keyword) throws ParseException {
		
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
		Map<String, Object> responseBody = JSONObjectUtils.parse((String) response.getBody());
		List<Object> result = (List<Object>) responseBody.get("result");
		
		return result.stream().map(movie -> mapper.dto(movie)).collect(Collectors.toList());
	}
	
	@GetMapping("/title")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "title", paramType = "query", dataType = "String",
				   required = true, value = "Title of movie"),
		
		@ApiImplicitParam(name = "country", paramType = "query", dataType = "String",
				   required = true, value = ApiDocumentationUtils.COUNTRY_PARAM_DESCRIPTION),
		   
		@ApiImplicitParam(name = "show_type", paramType = "query", dataType = "String",
				   value = ApiDocumentationUtils.SHOW_TYPE_PARAM_DESCRIPTION),
		   
		@ApiImplicitParam(name = "output_language", paramType = "query", dataType = "String",
				   value = ApiDocumentationUtils.OUTPUT_LANGUAGE_PARAM_DESCRIPTION)
	})
	public Object searchByTitle(
			@RequestParam("title") String title,
			@RequestParam("country") String country,
			@RequestParam(value = "show_type", required = false) String showType,
			@RequestParam(value = "output_language", required = false) String outputLanguage) throws ParseException {
		
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
		Map<String, Object> responseBody = JSONObjectUtils.parse((String) response.getBody());
		List<Object> result = (List<Object>) responseBody.get("result");
		
		return result.stream().map(movie -> mapper.dto(movie)).collect(Collectors.toList());
	}
	
}
