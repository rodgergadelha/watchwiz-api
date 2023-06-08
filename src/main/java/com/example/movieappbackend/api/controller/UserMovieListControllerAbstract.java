package com.example.movieappbackend.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.movieappbackend.domain.model.MovieListItem;
import com.example.movieappbackend.domain.service.UserMovieListServiceAbstract;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;


public abstract class UserMovieListControllerAbstract {
	
	protected abstract UserMovieListServiceAbstract getService();
	
	@GetMapping
	public ResponseEntity<List<MovieListItem>> movieList() {
		return ResponseEntity.ok(getService().getMovieList());
	}
	
	@PostMapping
	public ResponseEntity<?> saveMovie(@RequestBody MovieListItem movie) {
		getService().saveMovie(movie);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{imdbId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "imdbId", paramType = "path", dataType = "String",
				   required = true, value = "imdb_id of the movie to delete from list"),
	})
	public ResponseEntity<?> removeMovie(@PathVariable String imdbId) {
		getService().removeMovie(imdbId);
		return ResponseEntity.ok().build();
	}
}
