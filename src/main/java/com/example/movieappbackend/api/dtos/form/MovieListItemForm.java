package com.example.movieappbackend.api.dtos.form;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MovieListItemForm {

	@NotBlank
	private String imdbId;
	
	@NotBlank
	private String title;
	
	@NotBlank
	private String overview;
	
	@NotBlank
	private String posterUrl;
	
	private int imdbRating;
	
	private List<String> genres;
}
