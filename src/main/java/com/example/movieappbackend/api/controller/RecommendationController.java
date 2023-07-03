package com.example.movieappbackend.api.controller;

import java.util.List;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieappbackend.domain.model.MovieListItem;
import com.example.movieappbackend.domain.service.RecommendationService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/users/my-account/recommendation")
@AllArgsConstructor
public class RecommendationController {
	private final RecommendationService recommendationService;
	
	@GetMapping
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", paramType = "query", dataType = "integer",
		   required = true, value = "number of the page"),

		@ApiImplicitParam(name = "size", paramType = "query", dataType = "integer",
		required = true, value = "size of a page"),
		
		@ApiImplicitParam(name = "Authorization", paramType = "header", dataType = "string",
		   required = true, value = "access token")
	})
	public ResponseEntity<Page<MovieListItem>> getRecommendation (
			@RequestParam("page") int page,
			@RequestParam("size") int size){
		List<MovieListItem> movieList = recommendationService.executarRecomendacao();
		PagedListHolder<MovieListItem> moviesPage = new PagedListHolder<>(movieList);
		moviesPage.setPage(page);
		moviesPage.setPageSize(size);
		Page<MovieListItem> pageImpl = new PageImpl<>(moviesPage.getPageList());
		return ResponseEntity.ok(pageImpl);
	}
}
