package com.example.movieappbackend.api.mapper;

import org.springframework.stereotype.Component;

import com.example.movieappbackend.api.dtos.dto.WatchedMovieDto;
import com.example.movieappbackend.domain.model.WatchedMovie;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class WatchedMovieMapper {
	
	public WatchedMovieDto entityToDto(WatchedMovie watchedMovie) {
		WatchedMovieDto watchedMovieDto = new WatchedMovieDto(
				watchedMovie.getUserMoviePair().getMovie().getImdbId(),
				watchedMovie.getUserMoviePair().getMovie().getTitle(),
				watchedMovie.getUserMoviePair().getMovie().getOverview(),
				watchedMovie.getUserMoviePair().getMovie().getPosterUrl(),
				watchedMovie.getRate()
		);
		return watchedMovieDto;
	}
}
