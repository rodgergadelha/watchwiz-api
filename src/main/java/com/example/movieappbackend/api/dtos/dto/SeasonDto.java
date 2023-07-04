package com.example.movieappbackend.api.dtos.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class SeasonDto {

	private String title;
	
	private String overview;
	
	private Integer firstAirYear;
	
	private Map<String, String> posterUrls;
	
	@JsonInclude(Include.NON_EMPTY)
	private Map<String, Map<String, List<StreamingInfoDto>>> streamingInfo;
	
	private List<MovieDto> episodes;
}
