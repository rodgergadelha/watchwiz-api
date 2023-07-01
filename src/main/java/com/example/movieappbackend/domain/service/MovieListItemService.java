package com.example.movieappbackend.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.form.MovieListItemForm;
import com.example.movieappbackend.api.mapper.MovieMapper;
import com.example.movieappbackend.domain.exception.BusinessException;
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
		if(repository.existsById(form.getImdbId())) {
			throw new BusinessException(String.format("Movie with imdbId: %s already exists in database", form.getImdbId()));
		}
		MovieListItem movie = mapper.movieListItem(form);
		List<Genre> genres = genreService.findAllByNameIn(form.getGenres());
		if(genres.size() != form.getGenres().size()) throw new EntityNotFoundException("Cannot find some informed genres");
		movie.setGenres(genres);
		return repository.save(movie);
	}
	
	@Transactional
	public List<MovieListItem> saveAll(List<MovieListItemForm> forms) {
		List<MovieListItem> movies = forms.stream()
				.map(form -> {
					if(repository.existsById(form.getImdbId())) {
						throw new BusinessException(String.format("Movie with imdbId: %s already exists in database", form.getImdbId()));
					}
					MovieListItem movie = mapper.movieListItem(form);
					List<Genre> genres = genreService.findAllByNameIn(form.getGenres());
					if(genres.size() != form.getGenres().size()) throw new EntityNotFoundException("Cannot find some informed genres for movie: " + form.getImdbId());
					movie.setGenres(genres);
					return movie;
				})
				.collect(Collectors.toList());
		return repository.saveAll(movies);
	}
	
	public boolean existsByImdbId(String imdbId) {
		return repository.existsById(imdbId);
	}
}
