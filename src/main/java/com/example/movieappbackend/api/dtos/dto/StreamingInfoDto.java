package com.example.movieappbackend.api.dtos.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class StreamingInfoDto {

	private String type;
	private String quality;
	private String addOn;
	private String link;
	private String watchLink;
	private List<Map<String, String>> audios;
	private List<Map<String, String>> subtitles;
	private Map<String, String> price;
}
