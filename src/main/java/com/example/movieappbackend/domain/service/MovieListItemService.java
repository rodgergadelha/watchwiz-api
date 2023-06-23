package com.example.movieappbackend.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.form.MovieListItemForm;
import com.example.movieappbackend.api.mapper.MovieMapper;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.Genre;
import com.example.movieappbackend.domain.model.MovieListItem;
import com.example.movieappbackend.domain.repository.MovieListItemRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MovieListItemService {

	private final MovieListItemRepository repository;
	
	private final MovieMapper mapper;
	
	private final GenreService genreService;
	
	public List<MovieListItem> findAll() {
		return repository.findAll();
	}
	
	public MovieListItem findByImdbId(String imdbId) {
		return repository.findByImdbId(imdbId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format("Cannot find movie with imdbId: %s", imdbId)
				));
	}
	
	public MovieListItem findByImdbIdWithNoValidation(String imdbId) {
		return repository.findByImdbId(imdbId)
				.orElse(null);
	}
	
	@Transactional
	public MovieListItem save(MovieListItemForm form) {
		MovieListItem movie = mapper.movieListItem(form);
		List<Genre> genres = genreService.findAllByIdIn(form.getGenresIds());
		if(genres.size() != form.getGenresIds().size()) throw new EntityNotFoundException("Cannot find some ids");
		movie.setGenres(genres);
		return repository.save(movie);
	}
	
	public boolean existsByImdbId(String imdbId) {
		return repository.existsById(imdbId);
	}
}
