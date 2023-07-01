package com.example.movieappbackend.domain.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.form.MovieListItemForm;
import com.example.movieappbackend.domain.exception.BusinessException;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.MovieListItem;
import com.example.movieappbackend.domain.model.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class MovieListServiceAbstract {

	private final MovieListItemService movieListItemService;
	
	private final UserService userService;
	
	public abstract List<MovieListItem> getMovieList();
	
	public User getAuthenticatedUser() {
		return userService.getAuthenticatedUser();
	}
	
	@Transactional
	public void saveMovie(MovieListItemForm form) {
		
		List<MovieListItem> movies = getMovieList();
		boolean movieExists = movies.stream()
				.anyMatch(movie -> movie.getImdbId().equals(form.getImdbId()));
		
		if(movieExists) {
			throw new BusinessException(
					String.format("Movie with imdbId: %s is already saved", form.getImdbId())
			);
		}
		
		MovieListItem movie = movieListItemService.findByImdbIdWithNoValidation(form.getImdbId());
		if(movie == null) movie = movieListItemService.save(form);
		
		getMovieList().add(movie);
	}
	
	@Transactional
	public void removeMovie(String imdbId) {
		List<MovieListItem> movies = getMovieList();
		for(MovieListItem movie : movies) {
			if(movie.getImdbId().equals(imdbId)) {
				movies.remove(movie);
				return;
			}
		}
		throw new EntityNotFoundException(String.format("Could not find movie with imdbId: %s", imdbId));
	}
}
