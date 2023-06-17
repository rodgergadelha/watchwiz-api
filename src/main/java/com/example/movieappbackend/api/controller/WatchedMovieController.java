package com.example.movieappbackend.api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieappbackend.api.dtos.dto.WatchedMovieDto;
import com.example.movieappbackend.api.dtos.form.MovieListItemForm;
import com.example.movieappbackend.domain.service.WatchedMovieService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/users/my-account/watched-movies")
@AllArgsConstructor
public class WatchedMovieController {
	
	private final WatchedMovieService service;
	
	@GetMapping
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", paramType = "query", dataType = "integer",
				   required = true, value = "number of the page"),
		
		@ApiImplicitParam(name = "size", paramType = "query", dataType = "integer",
		   required = true, value = "size of a page"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<Page<WatchedMovieDto>> watchedMovies(
			@RequestParam("page") int page,
			@RequestParam("size") int size) {
		Pageable pageable = PageRequest.of(page, size);
		List<WatchedMovieDto> movies = service.watchedMovies();
		Page<WatchedMovieDto> moviesPages = new PageImpl(movies, pageable, movies.size());
		return ResponseEntity.ok(moviesPages);
	}
	
	@PostMapping
	@ApiImplicitParams({
		@ApiImplicitParam(name = "rate", paramType = "query", dataType = "integer",
				   required = true, value = "rate for watched movie"), 
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<WatchedMovieDto> saveWatchedMovie(@RequestBody MovieListItemForm form,
															@RequestParam("rate") int rate) {
		WatchedMovieDto watchedMovieDto = service.saveWatchedMovie(form, rate);
		return ResponseEntity.ok(watchedMovieDto);
	}
	
	@DeleteMapping("/{imdbId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "imdbId", paramType = "path", dataType = "String",
				   required = true, value = "imdb_id of the movie to delete from list"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<?> removeWatchedMovie(@PathVariable String imdbId) {
		service.removeWatched(imdbId);
		return ResponseEntity.ok().build();
	}
}
