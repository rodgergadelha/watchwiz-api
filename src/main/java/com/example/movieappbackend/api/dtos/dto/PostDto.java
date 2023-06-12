package com.example.movieappbackend.api.dtos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

	private String uuid;
	private String url;
    private String text;
    private WatchedMovieDto watchedMovie;
	private String creationDate;
}
