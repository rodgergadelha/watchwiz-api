package com.example.movieappbackend.domain.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.MovieListItem;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.repository.MovieListItemRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class UserMovieListServiceAbstract {

	private final MovieListItemRepository repository;
	
	private final UserService userService;
	
	public abstract List<MovieListItem> getMovieList();
	
	public User getAuthenticatedUser() {
		return userService.getAuthenticatedUser();
	}
	
	@Transactional
	public void saveMovie(MovieListItem movie) {
		if(!repository.existsById(movie.getImdbId())) repository.save(movie);
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
