package com.example.movieappbackend.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.movieappbackend.domain.model.MovieListItem;
import com.example.movieappbackend.domain.repository.MovieListItemRepository;

@Service
public class LikedMovieService extends MovieListServiceAbstract {
	
	public LikedMovieService(MovieListItemRepository repository, UserService userService) {
		super(repository, userService);
	}
	
	@Override
	public List<MovieListItem> getMovieList() {
		return getAuthenticatedUser().getLiked();
	}
}
