package com.example.movieappbackend.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieappbackend.domain.service.MovieListServiceAbstract;
import com.example.movieappbackend.domain.service.WatchLaterMovieService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/users/my-account/watch-later-movies")
@AllArgsConstructor
public class WatchLaterMovieController extends MovieListControllerAbstract {
	
	private final WatchLaterMovieService service;
	
	protected MovieListServiceAbstract getService() {
		return this.service;
	}
}