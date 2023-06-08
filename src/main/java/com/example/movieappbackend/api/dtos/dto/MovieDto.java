package com.example.movieappbackend.api.dtos.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MovieDto {
	
	private String title;
	private String overview;
	private String imdbId;
	private int imdbRating;
	private List<String> cast;
	private List<Map<String, Object>> genres;
	private Map<String, String> posterUrls;
}
