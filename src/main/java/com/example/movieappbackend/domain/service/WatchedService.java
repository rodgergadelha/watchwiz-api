package com.example.movieappbackend.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.movieappbackend.domain.model.User;
import com.example.movieappbackend.domain.repository.WatchedRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WatchedService {

	private final WatchedRepository repository;
	
	private final UserService userService;
	
	public List<String> findUserWatchedMovies(String username) {
		User user = userService.findByUsername(username);
		return repository.findAllByUser(user).stream()
				.map(userMoviePair -> userMoviePair.getMovieImdbId())
				.collect(Collectors.toList());
	}
}
