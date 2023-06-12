package com.example.movieappbackend.api.dtos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WatchedMovieDto {

	private String imdbId;
	private String title;
	private String overview;
	private String posterUrl;
	private float rate;
}
