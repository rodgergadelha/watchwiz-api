package com.example.movieappbackend.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.movieappbackend.domain.model.MovieListItem;

@Service
public class FavoriteMovieService extends MovieListServiceAbstract {
	
	public FavoriteMovieService(MovieListItemService movieListItemService, UserService userService) {
		super(movieListItemService, userService);
	}
	
	@Override
	public List<MovieListItem> getMovieList() {
		return getAuthenticatedUser().getFavorites();
	}
}
