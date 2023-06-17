package com.example.movieappbackend.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieappbackend.api.dtos.dto.WatchedMovieDto;
import com.example.movieappbackend.api.dtos.form.MovieListItemForm;
import com.example.movieappbackend.api.mapper.WatchedMovieMapper;
import com.example.movieappbackend.domain.exception.BusinessException;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.example.movieappbackend.domain.model.MovieListItem;
import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.model.UserMoviePair;
import com.example.movieappbackend.domain.model.WatchedMovie;
import com.example.movieappbackend.domain.repository.WatchedMovieRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WatchedMovieService {
	
	private final WatchedMovieRepository repository;

	private final MovieListItemService movieListItemService;
	
	private final UserService userService;
	
	private final WatchedMovieMapper mapper;
	
	public List<WatchedMovieDto> watchedMovies() {
		User loggedInUser = userService.getAuthenticatedUser();
		return repository.findAllWatchedMoviesByUser(loggedInUser).stream()
				.map(watchedMovie -> mapper.entityToDto(watchedMovie))
				.collect(Collectors.toList());
	}
	
	@Transactional
	public WatchedMovieDto saveWatchedMovie(MovieListItemForm form, float rate) {
		
		MovieListItem movie = movieListItemService.findByImdbIdWithNoValidation(form.getImdbId());
		if(movie == null) movie = movieListItemService.save(form);
		
		User loggedInUser = userService.getAuthenticatedUser();
		WatchedMovie watchedMovie = new WatchedMovie();
		watchedMovie.setUserMoviePair(new UserMoviePair());
		watchedMovie.getUserMoviePair().setMovie(movie);
		watchedMovie.getUserMoviePair().setUser(loggedInUser);
		watchedMovie.setRate(rate);
		
		if(repository.existsByImdbIdAndUser(movie.getImdbId(), loggedInUser)) {
			throw new BusinessException(String.format("Movie with imdbId: %s is already saved", movie.getImdbId()));
		}
		
		watchedMovie = repository.save(watchedMovie);
		return mapper.entityToDto(watchedMovie);
	}
	
	public WatchedMovie findByImdbIdAndUser(String imdbId, User user) {
		return repository.findByImdbIdAndUser(imdbId, user)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Cannot find watched movie for imdbId: %s and user: %s", imdbId, user.getUsername())));
	}
	
	@Transactional
	public void removeWatched(String imdbId) {
		User loggedInUser = userService.getAuthenticatedUser();
		WatchedMovie watchedMovie = findByImdbIdAndUser(imdbId, loggedInUser);
		repository.delete(watchedMovie);
	}
}
