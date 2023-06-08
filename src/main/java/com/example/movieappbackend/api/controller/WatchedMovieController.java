package com.example.movieappbackend.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieappbackend.domain.service.UserMovieListServiceAbstract;
import com.example.movieappbackend.domain.service.WatchedMovieService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/users/my-account/watched-movies")
@AllArgsConstructor
public class WatchedMovieController extends UserMovieListControllerAbstract {
	
	private final WatchedMovieService service;
	
	protected UserMovieListServiceAbstract getService() {
		return this.service;
	}
}
