package com.example.movieappbackend.api.controller;

import java.util.List;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.movieappbackend.api.dtos.form.MovieListItemForm;
import com.example.movieappbackend.domain.model.MovieListItem;
import com.example.movieappbackend.domain.service.MovieListServiceAbstract;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;


public abstract class MovieListControllerAbstract {
	
	protected abstract MovieListServiceAbstract getService();
	
	@GetMapping
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", paramType = "query", dataType = "integer",
				   required = true, value = "number of the page"),
		
		@ApiImplicitParam(name = "size", paramType = "query", dataType = "integer",
		   required = true, value = "size of a page"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<Page<MovieListItem>> movieList(
			@RequestParam("page") int page,
			@RequestParam("size") int size) {
		List<MovieListItem> movies = getService().getMovieList();
		PagedListHolder<MovieListItem> moviesPage = new PagedListHolder<>(movies);
		moviesPage.setPage(page);
		moviesPage.setPageSize(size);
		Page<MovieListItem> pageImpl = new PageImpl<>(moviesPage.getPageList());
		return ResponseEntity.ok(pageImpl);
	}
	
	@PostMapping
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<?> saveMovie(@RequestBody MovieListItemForm form) {
		getService().saveMovie(form);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{imdbId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "imdbId", paramType = "path", dataType = "String",
				   required = true, value = "imdb_id of the movie to delete from list"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<?> removeMovie(@PathVariable String imdbId) {
		getService().removeMovie(imdbId);
		return ResponseEntity.ok().build();
	}
}
