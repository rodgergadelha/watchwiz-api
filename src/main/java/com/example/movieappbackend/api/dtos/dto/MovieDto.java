package com.example.movieappbackend.api.dtos.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MovieDto {
	
	private String type;
	
	private String title;
	
	private String overview;
	
	private String imdbId;
	
	private int imdbRating;
	
	private List<String> cast;
	
	private List<Map<String, Object>> genres;
	
	private Map<String, String> posterURLs;
	
	@JsonInclude(Include.NON_NULL)
	private Integer runtime;
	
	@JsonInclude(Include.NON_NULL)
	private Integer year;
	
	@JsonInclude(Include.NON_NULL)
	private Integer firstAirYear;
	
	@JsonInclude(Include.NON_EMPTY)
	private Map<String, Map<String, List<StreamingInfoDto>>> streamingInfo;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<SeasonDto> seasons;
	
	@JsonInclude(Include.NON_NULL)
	private Integer seasonCount;
	
	@JsonInclude(Include.NON_NULL)
	private Integer episodeCount;
}
