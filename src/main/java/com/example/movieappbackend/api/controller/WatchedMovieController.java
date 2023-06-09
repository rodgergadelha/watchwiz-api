package com.example.movieappbackend.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieappbackend.domain.service.MovieListServiceAbstract;
import com.example.movieappbackend.domain.service.WatchedMovieService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/users/my-account/watched-movies")
@AllArgsConstructor
public class WatchedMovieController extends MovieListControllerAbstract {
	
	private final WatchedMovieService service;
	
	protected MovieListServiceAbstract getService() {
		return this.service;
	}
}
