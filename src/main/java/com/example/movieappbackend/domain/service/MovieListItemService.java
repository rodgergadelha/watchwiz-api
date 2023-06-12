package com.example.movieappbackend.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.MovieListItem;
import com.example.movieappbackend.domain.repository.MovieListItemRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MovieListItemService {

	private final MovieListItemRepository repository;
	
	public MovieListItem findByImdbId(String imdbId) {
		return repository.findByImdbId(imdbId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Cannot find movie with imdbId: %s", imdbId)
				));
	}
	
	@Transactional
	public MovieListItem save(MovieListItem movie) {
		return repository.save(movie);
	}
	
	public boolean existsByImdbId(String imdbId) {
		return repository.existsById(imdbId);
	}
}
